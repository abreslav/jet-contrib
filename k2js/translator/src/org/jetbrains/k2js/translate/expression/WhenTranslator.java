package org.jetbrains.k2js.translate.expression;

import com.google.dart.compiler.backend.js.ast.*;
import com.google.dart.compiler.util.AstUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jet.lang.psi.*;
import org.jetbrains.k2js.translate.context.TranslationContext;
import org.jetbrains.k2js.translate.general.AbstractTranslator;
import org.jetbrains.k2js.translate.general.Translation;

import java.util.ArrayList;
import java.util.List;

import static com.google.dart.compiler.util.AstUtil.convertToStatement;
import static org.jetbrains.k2js.translate.utils.BindingUtils.isStatement;

/**
 * @author Pavel Talanov
 */
//TODO: look into using temporary variable for counter
public class WhenTranslator extends AbstractTranslator {

    @NotNull
    static public JsNode translateWhenExpression(@NotNull JetWhenExpression expression, @NotNull TranslationContext context) {
        WhenTranslator translator = new WhenTranslator(expression, context);
        return translator.translate();
    }

    @NotNull
    private final JetWhenExpression whenExpression;
    @NotNull
    private final JsExpression expressionToMatch;
    @NotNull
    private final JsName dummyCounterName;
    private int currentEntryNumber = 0;

    private WhenTranslator(@NotNull JetWhenExpression expression, @NotNull TranslationContext context) {
        super(context);
        this.whenExpression = expression;
        this.expressionToMatch = translateExpressionToMatch(whenExpression);
        this.dummyCounterName = context.jsScope().declareTemporary();
    }

    boolean shouldWrapInFunction() {
        return !isStatement(context().bindingContext(), whenExpression);
    }

    @NotNull
    JsNode translate() {
        JsFor resultingFor = generateDummyFor();
        List<JsStatement> entries = translateEntries();
        resultingFor.setBody(AstUtil.newBlock(entries));
        return mayBeWrapInFunction(resultingFor);
    }

    @NotNull
    private JsNode mayBeWrapInFunction(@NotNull JsFor resultingFor) {
        if (!shouldWrapInFunction()) return resultingFor;

        JsFunction dummyFunction = JsFunction.getAnonymousFunctionWithScope(context().jsScope());
        dummyFunction.setBody(AstUtil.convertToBlock(resultingFor));
        return AstUtil.newInvocation(dummyFunction);
    }

    @NotNull
    private List<JsStatement> translateEntries() {
        List<JsStatement> entries = new ArrayList<JsStatement>();
        for (JetWhenEntry entry : whenExpression.getEntries()) {
            entries.add(surroundWithDummyIf(translateEntry(entry)));
        }
        return entries;
    }

    @NotNull
    private JsStatement surroundWithDummyIf(@NotNull JsStatement entryStatement) {
        JsExpression stepNumberEqualsCurrentEntryNumber = new JsBinaryOperation(JsBinaryOperator.EQ,
                dummyCounterName.makeRef(), context().program().getNumberLiteral(currentEntryNumber));
        currentEntryNumber++;
        return new JsIf(stepNumberEqualsCurrentEntryNumber, entryStatement, null);
    }

    @NotNull
    private JsFor generateDummyFor() {
        JsFor result = new JsFor();
        result.setInitVars(generateInitStatement());
        result.setIncrExpr(generateIncrementStatement());
        result.setCondition(generateConditionStatement());
        return result;
    }

    @NotNull
    private JsVars generateInitStatement() {
        return AstUtil.newVar(dummyCounterName, context().program().getNumberLiteral(0));
    }

    @NotNull
    private JsBinaryOperation generateConditionStatement() {
        JsNumberLiteral entriesNumber = program().getNumberLiteral(whenExpression.getEntries().size());
        return new JsBinaryOperation(JsBinaryOperator.LT, dummyCounterName.makeRef(), entriesNumber);
    }

    @NotNull
    private JsPrefixOperation generateIncrementStatement() {
        return new JsPrefixOperation(JsUnaryOperator.INC, dummyCounterName.makeRef());
    }

    @NotNull
    private JsStatement translateEntry(@NotNull JetWhenEntry entry) {
        JsStatement statementToExecute = mayBeWrapWithReturn(translateEntryExpression(entry));
        if (entry.isElse()) {
            return statementToExecute;
        }
        JsExpression condition = translateConditions(entry);
        return new JsIf(condition, addDummyBreak(statementToExecute), null);
    }

    @NotNull
    private JsStatement mayBeWrapWithReturn(@NotNull JsNode node) {
        if (shouldWrapInFunction()) {
            assert node instanceof JsExpression :
                    "Statements are not supported in when clauses which are themselves expressions";
            return new JsReturn((JsExpression) node);
        } else {
            return convertToStatement(node);
        }
    }

    @NotNull
    private JsNode translateEntryExpression(@NotNull JetWhenEntry entry) {
        JetExpression expressionToExecute = entry.getExpression();
        assert expressionToExecute != null : "WhenEntry should have whenExpression to execute.";
        return Translation.translateExpression(expressionToExecute, context());
    }

    @NotNull
    private JsExpression translateConditions(@NotNull JetWhenEntry entry) {
        List<JsExpression> conditions = new ArrayList<JsExpression>();
        for (JetWhenCondition condition : entry.getConditions()) {
            conditions.add(translateCondition(condition));
        }
        return anyOfThemIsTrue(conditions);
    }

    @NotNull
    private JsExpression anyOfThemIsTrue(List<JsExpression> conditions) {
        assert !conditions.isEmpty() : "When entry (not else) should have at least one condition";
        JsExpression current = null;
        for (JsExpression condition : conditions) {
            current = addCaseCondition(current, condition);
        }
        assert current != null;
        return current;
    }

    @NotNull
    private JsExpression addCaseCondition(@Nullable JsExpression current, @NotNull JsExpression condition) {
        if (current == null) {
            return condition;
        } else {
            return AstUtil.or(current, condition);
        }
    }

    @NotNull
    private JsExpression translateCondition(@NotNull JetWhenCondition condition) {
        if ((condition instanceof JetWhenConditionIsPattern) || (condition instanceof JetWhenConditionWithExpression)) {
            return translatePatternCondition(condition);
        }
        throw new AssertionError("Unsupported when condition " + condition.getClass());
    }

    @NotNull
    private JsBlock addDummyBreak(@NotNull JsStatement statement) {
        return AstUtil.newBlock(statement, new JsBreak());
    }

    @NotNull
    private JsExpression translatePatternCondition(@NotNull JetWhenCondition condition) {
        JsExpression patternMatchExpression = Translation.patternTranslator(context()).
                translatePattern(getPattern(condition), expressionToMatch);
        if (isNegated(condition)) {
            return AstUtil.negated(patternMatchExpression);
        }
        return patternMatchExpression;
    }

    private boolean isNegated(@NotNull JetWhenCondition condition) {
        if (condition instanceof JetWhenConditionIsPattern) {
            return ((JetWhenConditionIsPattern) condition).isNegated();
        }
        return false;
    }

    @NotNull
    private JetPattern getPattern(@NotNull JetWhenCondition condition) {
        JetPattern pattern;
        if (condition instanceof JetWhenConditionIsPattern) {
            pattern = ((JetWhenConditionIsPattern) condition).getPattern();
        } else if (condition instanceof JetWhenConditionWithExpression) {
            pattern = ((JetWhenConditionWithExpression) condition).getPattern();
        } else {
            throw new AssertionError("Wrong type of JetWhenCondition");
        }
        assert pattern != null : "Condition should have a non null pattern.";
        return pattern;
    }

    @NotNull
    private JsExpression translateExpressionToMatch(@NotNull JetWhenExpression expression) {
        JetExpression subject = expression.getSubjectExpression();
        assert subject != null : "Subject should not be null.";
        return Translation.translateAsExpression(subject, context());
    }
}
