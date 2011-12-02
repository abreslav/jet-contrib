package org.jetbrains.k2js.translate.operation;

import com.google.dart.compiler.backend.js.ast.JsExpression;
import com.google.dart.compiler.backend.js.ast.JsNameRef;
import com.google.dart.compiler.util.AstUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jet.lang.descriptors.DeclarationDescriptor;
import org.jetbrains.jet.lang.psi.JetBinaryExpression;
import org.jetbrains.k2js.translate.context.TranslationContext;

import static org.jetbrains.k2js.translate.utils.BindingUtils.getDescriptorForReferenceExpression;

/**
 * @author Talanov Pavel
 */
public final class OverloadedAssignmentTranslator extends AssignmentTranslator {

    @NotNull
    public static JsExpression translate(@NotNull JetBinaryExpression expression,
                                         @NotNull TranslationContext context) {
        return (new OverloadedAssignmentTranslator(expression, context)).translate();
    }

    @NotNull
    private final JsNameRef operationReference;

    private OverloadedAssignmentTranslator(@NotNull JetBinaryExpression expression,
                                           @NotNull TranslationContext context) {
        super(expression, context);
        //TODO: util
        DeclarationDescriptor overloadedOperationDescriptor = getDescriptorForReferenceExpression
                (context.bindingContext(), expression.getOperation());
        assert overloadedOperationDescriptor != null;
        JsNameRef overloadedOperationReference = context().getNameForDescriptor(overloadedOperationDescriptor).makeRef();
        assert overloadedOperationReference != null;
        this.operationReference = overloadedOperationReference;
    }

    @NotNull
    protected JsExpression translate() {
        if (isVariableReassignment) {
            return reassignment();
        }
        return overloadedMethodInvocation();
    }

    @NotNull
    private JsExpression reassignment() {
        return accessTranslator.translateAsSet(overloadedMethodInvocation());
    }

    @NotNull
    private JsExpression overloadedMethodInvocation() {
        AstUtil.setQualifier(operationReference, accessTranslator.translateAsGet());
        return AstUtil.newInvocation(operationReference, right);
    }

}
