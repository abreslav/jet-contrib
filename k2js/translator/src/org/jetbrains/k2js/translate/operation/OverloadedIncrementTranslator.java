package org.jetbrains.k2js.translate.operation;

import com.google.dart.compiler.backend.js.ast.JsExpression;
import com.google.dart.compiler.backend.js.ast.JsNameRef;
import com.google.dart.compiler.util.AstUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jet.lang.descriptors.DeclarationDescriptor;
import org.jetbrains.jet.lang.psi.JetUnaryExpression;
import org.jetbrains.k2js.translate.context.TranslationContext;
import org.jetbrains.k2js.translate.utils.BindingUtils;

/**
 * @author Talanov Pavel
 */
// TODO: provide better increment translator logic
public final class OverloadedIncrementTranslator extends IncrementTranslator {

    @NotNull
    public static JsExpression translate(@NotNull JetUnaryExpression expression,
                                         @NotNull TranslationContext context) {
        return (new OverloadedIncrementTranslator(expression, context))
                .translate();
    }

    @NotNull
    private final JsNameRef operationReference;

    private OverloadedIncrementTranslator(@NotNull JetUnaryExpression expression,
                                          @NotNull TranslationContext context) {
        super(expression, context);
        //TODO: DUBLICATE CODE
        DeclarationDescriptor overloadedOperationDescriptor = BindingUtils.getDescriptorForReferenceExpression
                (context.bindingContext(), expression.getOperation());
        assert overloadedOperationDescriptor != null;
        JsNameRef overloadedOperationReference = context().getNameForDescriptor(overloadedOperationDescriptor).makeRef();
        assert overloadedOperationReference != null;
        this.operationReference = overloadedOperationReference;
    }

    @Override
    @NotNull
    protected JsExpression translate() {
        return translateAsMethodCall();
    }

    @Override
    @NotNull
    protected JsExpression operationExpression(@NotNull JsExpression receiver) {
        AstUtil.setQualifier(operationReference, receiver);
        return AstUtil.newInvocation(operationReference);
    }

}
