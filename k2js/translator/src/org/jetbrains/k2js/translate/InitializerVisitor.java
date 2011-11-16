package org.jetbrains.k2js.translate;

import com.google.dart.compiler.backend.js.ast.*;
import com.google.dart.compiler.util.AstUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jet.lang.psi.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Talanov Pavel
 *         <p/>
 *         This visitor collects all initializers from a given class into JsFunction object.
 *         Note: we do use visitor pattern to preserve the order in which initializers are executed.
 */
public class InitializerVisitor extends TranslatorVisitor<List<JsStatement>> {

    @NotNull
    private final JsScope initializerMethodScope;
    @NotNull
    private final JetClass classDeclaration;
    @NotNull
    private final TranslationContext initializerMethodContext;

    public InitializerVisitor(@NotNull JetClass classDeclaration, @NotNull TranslationContext context) {
        this.initializerMethodScope = new JsScope(context.getScopeForElement(classDeclaration),
                "initializer " + classDeclaration.getName());
        this.classDeclaration = classDeclaration;
        this.initializerMethodContext = context.newEnclosingScope(initializerMethodScope);

    }

    @NotNull
    public JsFunction generateInitializeMethod() {
        JsFunction result = JsFunction.getAnonymousFunctionWithScope(initializerMethodScope);
        result.setParameters(translatePrimaryConstructorParameters(classDeclaration));
        result.setBody(generateInitializerMethodBody(classDeclaration, initializerMethodContext));
        return result;
    }

    @NotNull
    private JsBlock generateInitializerMethodBody(@NotNull JetClass classDeclaration,
                                                  @NotNull TranslationContext context) {

        List<JsStatement> initializerStatements = generateCallToSuperMethod(classDeclaration, context);
        initializerStatements.addAll(classDeclaration.accept(this, context));
        return AstUtil.newBlock(initializerStatements);
    }

    @NotNull
    private List<JsStatement> generateCallToSuperMethod(@NotNull JetClass classDeclaration,
                                                        @NotNull TranslationContext context) {
        List<JsStatement> result = new ArrayList<JsStatement>();
        if (BindingUtils.hasAncestor(context.bindingContext(), classDeclaration)) {
            JsName superMethodName = initializerMethodScope.declareName(Namer.SUPER_METHOD_NAME);
            List<JsExpression> arguments = translateArguments(classDeclaration, context);
            result.add(AstUtil.convertToStatement
                    (AstUtil.newInvocation(AstUtil.thisQualifiedReference(superMethodName), arguments)));
        }
        return result;
    }

    @NotNull
    private List<JsExpression> translateArguments(@NotNull JetClass classDeclaration,
                                                  @NotNull TranslationContext context) {
        JetDelegatorToSuperCall superCall = getSuperCall(classDeclaration);
        return translateArgumentList(superCall.getValueArguments(), context);
    }

    @NotNull
    private JetDelegatorToSuperCall getSuperCall(@NotNull JetClass classDeclaration) {
        JetDelegatorToSuperCall result = null;
        for (JetDelegationSpecifier specifier : classDeclaration.getDelegationSpecifiers()) {
            if (specifier instanceof JetDelegatorToSuperCall) {
                result = (JetDelegatorToSuperCall) specifier;
            }
        }
        assert result != null : "Class must call ancestor's constructor.";
        return result;
    }


    @Override
    @NotNull
    public List<JsStatement> visitClass(@NotNull JetClass expression, @NotNull TranslationContext context) {
        List<JsStatement> initializerStatements = new ArrayList<JsStatement>();
        for (JetDeclaration declaration : expression.getDeclarations()) {
            initializerStatements.addAll(declaration.accept(this, context));
        }
        return initializerStatements;
    }

    @NotNull
    List<JsParameter> translatePrimaryConstructorParameters(@NotNull JetClass expression) {
        List<JsParameter> result = new ArrayList<JsParameter>();
        List<JetParameter> parameters = expression.getPrimaryConstructorParameters();
        for (JetParameter parameter : parameters) {
            JsName parameterName = initializerMethodScope.declareName(parameter.getName());
            result.add(new JsParameter(parameterName));
        }
        return result;
    }

    @Override
    @NotNull
    public List<JsStatement> visitProperty(@NotNull JetProperty expression, @NotNull TranslationContext context) {
        JetExpression initializer = expression.getInitializer();
        declareBackingFieldName(expression);
        if (initializer == null) {
            return new ArrayList<JsStatement>();
        }
        return Arrays.asList(translateInitializer(expression, context, initializer));
    }

    private void declareBackingFieldName(@NotNull JetProperty property) {
        initializerMethodScope.declareName(Namer.getKotlinBackingFieldName(property.getName()));
    }

    @NotNull
    private JsStatement translateInitializer(@NotNull JetProperty property, @NotNull TranslationContext context,
                                             @NotNull JetExpression initializer) {
        JsExpression initExpression = AstUtil.convertToExpression(Translation.expressionTranslator(context)
                .translate(initializer));
        return assignmentToBackingField(property, initExpression, context);
    }

    @NotNull
    JsStatement assignmentToBackingField(@NotNull JetProperty property, @NotNull JsExpression initExpression,
                                         @NotNull TranslationContext context) {
        String propertyName = property.getName();
        assert propertyName != null : "Named property expected.";
        JsName backingFieldName = getBackingFieldName(propertyName, context);
        JsNameRef backingFieldRef = backingFieldName.makeRef();
        backingFieldRef.setQualifier(new JsThisRef());
        return AstUtil.convertToStatement(AstUtil.newAssignment(backingFieldRef, initExpression));
    }

    @Override
    @NotNull
    public List<JsStatement> visitAnonymousInitializer(@NotNull JetClassInitializer initializer,
                                                       @NotNull TranslationContext context) {
        return Arrays.asList(AstUtil.convertToStatement
                (Translation.expressionTranslator(context).translate(initializer.getBody())));
    }

    @Override
    @NotNull
    // Not interested in other types of declarations, they do not contain initializers.
    public List<JsStatement> visitDeclaration(@NotNull JetDeclaration expression, @NotNull TranslationContext context) {
        return new ArrayList<JsStatement>();
    }

}
