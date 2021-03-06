package org.jetbrains.k2js.translate.expression;

import com.google.dart.compiler.backend.js.ast.*;
import com.google.dart.compiler.util.AstUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jet.lang.descriptors.CallableDescriptor;
import org.jetbrains.jet.lang.descriptors.FunctionDescriptor;
import org.jetbrains.jet.lang.psi.JetExpression;
import org.jetbrains.jet.lang.psi.JetForExpression;
import org.jetbrains.jet.lang.psi.JetParameter;
import org.jetbrains.k2js.translate.context.TemporaryVariable;
import org.jetbrains.k2js.translate.context.TranslationContext;
import org.jetbrains.k2js.translate.general.AbstractTranslator;
import org.jetbrains.k2js.translate.general.Translation;
import org.jetbrains.k2js.translate.reference.CallBuilder;

import static org.jetbrains.k2js.translate.utils.BindingUtils.*;
import static org.jetbrains.k2js.translate.utils.PsiUtils.getLoopBody;
import static org.jetbrains.k2js.translate.utils.PsiUtils.getLoopParameter;

/**
 * @author Pavel Talanov
 */
public final class ForTranslator extends AbstractTranslator {

    @NotNull
    public static JsStatement translate(@NotNull JetForExpression expression,
                                        @NotNull TranslationContext context) {
        return (new ForTranslator(expression, context).translate());
    }

    @NotNull
    private final JetForExpression expression;

    private ForTranslator(@NotNull JetForExpression forExpression, @NotNull TranslationContext context) {
        super(context);
        this.expression = forExpression;
    }

    @NotNull
    private JsBlock translate() {
        JsName parameterName = declareParameter();
        TemporaryVariable iterator = context().declareTemporary(iteratorMethodInvocation());
        JsBlock bodyBlock = generateCycleBody(parameterName, iterator);
        JsWhile cycle = new JsWhile(hasNextMethodInvocation(iterator), bodyBlock);
        return AstUtil.newBlock(iterator.assignmentExpression().makeStmt(), cycle);
    }

    @NotNull
    private JsName declareParameter() {
        JetParameter loopParameter = getLoopParameter(expression);
        return context().getNameForElement(loopParameter);
    }

    @NotNull
    private JsBlock generateCycleBody(@NotNull JsName parameterName, @NotNull TemporaryVariable iterator) {
        JsBlock cycleBody = new JsBlock();
        JsStatement parameterAssignment =
                AstUtil.newVar(parameterName, nextMethodInvocation(iterator));
        JsNode originalBody = Translation.translateExpression(getLoopBody(expression), context().innerBlock(cycleBody));
        cycleBody.addStatement(parameterAssignment);
        cycleBody.addStatement(AstUtil.convertToBlock(originalBody));
        return cycleBody;
    }

    @NotNull
    private JsExpression nextMethodInvocation(@NotNull TemporaryVariable iterator) {
        FunctionDescriptor nextFunction = getNextFunction(context().bindingContext(), getLoopRange());
        return translateMethodInvocation(iterator.nameReference(), nextFunction);
    }

    @NotNull
    private JsExpression hasNextMethodInvocation(@NotNull TemporaryVariable iterator) {
        CallableDescriptor hasNextFunction = getHasNextCallable(context().bindingContext(), getLoopRange());
        return translateMethodInvocation(iterator.nameReference(), hasNextFunction);
    }

    @NotNull
    private JsExpression iteratorMethodInvocation() {
        JetExpression rangeExpression = getLoopRange();
        JsExpression range = Translation.translateAsExpression(rangeExpression, context());
        FunctionDescriptor iteratorFunction = getIteratorFunction(context().bindingContext(), rangeExpression);
        return translateMethodInvocation(range, iteratorFunction);
    }

    @NotNull
    private JetExpression getLoopRange() {
        JetExpression rangeExpression = expression.getLoopRange();
        assert rangeExpression != null;
        return rangeExpression;
    }

    @NotNull
    private JsExpression translateMethodInvocation(@Nullable JsExpression receiver,
                                                   @NotNull CallableDescriptor descriptor) {
        return CallBuilder.build(context())
                .receiver(receiver)
                .descriptor(descriptor)
                .translate();
    }
}
