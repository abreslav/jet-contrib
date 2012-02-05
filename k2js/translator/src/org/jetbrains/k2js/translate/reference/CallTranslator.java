package org.jetbrains.k2js.translate.reference;

import com.google.dart.compiler.backend.js.ast.*;
import com.google.dart.compiler.util.AstUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jet.lang.descriptors.*;
import org.jetbrains.jet.lang.psi.JetBinaryExpression;
import org.jetbrains.jet.lang.psi.JetCallExpression;
import org.jetbrains.jet.lang.psi.JetExpression;
import org.jetbrains.jet.lang.psi.JetUnaryExpression;
import org.jetbrains.jet.lang.resolve.calls.*;
import org.jetbrains.jet.lang.resolve.scopes.receivers.ReceiverDescriptor;
import org.jetbrains.k2js.translate.context.TranslationContext;
import org.jetbrains.k2js.translate.general.AbstractTranslator;
import org.jetbrains.k2js.translate.general.Translation;
import org.jetbrains.k2js.translate.intrinsic.FunctionIntrinsic;
import org.jetbrains.k2js.translate.utils.TranslationUtils;

import java.util.*;

import static com.google.dart.compiler.util.AstUtil.not;
import static org.jetbrains.k2js.translate.utils.BindingUtils.*;
import static org.jetbrains.k2js.translate.utils.DescriptorUtils.*;
import static org.jetbrains.k2js.translate.utils.PsiUtils.*;
import static org.jetbrains.k2js.translate.utils.TranslationUtils.*;

/**
 * @author Pavel Talanov
 */
//TODO: write tests on calling backing fields as functions
//TODO: constructor receives too many parameters
//TODO: reorder methods properly
public final class CallTranslator extends AbstractTranslator {

    private static final class Builder {

        @NotNull
        private final TranslationContext context;

        private Builder(@NotNull TranslationContext context) {
            this.context = context;
        }

        @NotNull
        private CallTranslator buildFromUnary(@NotNull JetUnaryExpression unaryExpression) {
            JsExpression receiver = TranslationUtils.translateBaseExpression(context, unaryExpression);
            List<JsExpression> arguments = Collections.emptyList();
            ResolvedCall<?> resolvedCall =
                    getResolvedCall(context.bindingContext(), unaryExpression.getOperationReference());
            return new CallTranslator(receiver, null, arguments, resolvedCall, null, context);
        }

        //TODO: method too long
        @NotNull
        private CallTranslator buildFromBinary(@NotNull JetBinaryExpression binaryExpression,
                                               boolean swapReceiverAndArgument) {

            JsExpression leftExpression = translateLeftExpression(context, binaryExpression);
            JsExpression rightExpression = translateRightExpression(context, binaryExpression);

            JsExpression receiver;
            List<JsExpression> arguments;
            if (swapReceiverAndArgument) {
                receiver = rightExpression;
                arguments = Arrays.asList(leftExpression);
            } else {
                receiver = leftExpression;
                arguments = Arrays.asList(rightExpression);
            }

            ResolvedCall<?> resolvedCall =
                    getResolvedCall(context.bindingContext(), binaryExpression.getOperationReference());
            return new CallTranslator(receiver, null, arguments, resolvedCall, null, context);
        }

        @NotNull
        private CallTranslator buildFromCallExpression(@NotNull JetCallExpression callExpression,
                                                       @Nullable JsExpression receiver) {
            ResolvedCall<?> resolvedCall = getResolvedCallForCallExpression(context.bindingContext(), callExpression);
            List<JsExpression> arguments = translateArgumentsForCallExpression(callExpression, context);
            JsExpression callee = null;
            if (resolvedCall.getCandidateDescriptor() instanceof ExpressionAsFunctionDescriptor) {
                callee = Translation.translateAsExpression(getCallee(callExpression), context);
            }
            return new CallTranslator(receiver, callee, arguments, resolvedCall, null, context);
        }

        @NotNull
        private List<JsExpression> translateArgumentsForCallExpression(@NotNull JetCallExpression callExpression,
                                                                       @NotNull TranslationContext context) {
            List<JsExpression> result = new ArrayList<JsExpression>();
            ResolvedCall<?> resolvedCall = getResolvedCallForCallExpression(context.bindingContext(), callExpression);
            Map<ValueParameterDescriptor, ResolvedValueArgument> formalToActualArguments = resolvedCall.getValueArguments();
            for (ValueParameterDescriptor parameterDescriptor : resolvedCall.getResultingDescriptor().getValueParameters()) {
                ResolvedValueArgument actualArgument = formalToActualArguments.get(parameterDescriptor);
                result.add(translateSingleArgument(actualArgument, parameterDescriptor));
            }
            return result;
        }


        //TODO: refactor
        @NotNull
        private JsExpression translateSingleArgument(@NotNull ResolvedValueArgument actualArgument,
                                                     @NotNull ValueParameterDescriptor parameterDescriptor) {
            List<JetExpression> argumentExpressions = actualArgument.getArgumentExpressions();
            if (actualArgument instanceof VarargValueArgument) {
                return translateVarargArgument(argumentExpressions);
            }
            if (actualArgument instanceof DefaultValueArgument) {
                JetExpression defaultArgument = getDefaultArgument(context.bindingContext(), parameterDescriptor);
                return Translation.translateAsExpression(defaultArgument, context);
            }
            assert actualArgument instanceof ExpressionValueArgument;
            assert argumentExpressions.size() == 1;
            return Translation.translateAsExpression(argumentExpressions.get(0), context);
        }

        @NotNull
        private JsExpression translateVarargArgument(@NotNull List<JetExpression> arguments) {
            JsArrayLiteral varargArgument = new JsArrayLiteral();
            for (JetExpression argument : arguments) {
                varargArgument.getExpressions().add(Translation.translateAsExpression(argument, context));
            }
            return varargArgument;
        }
    }

    @NotNull
    public static JsExpression translate(@Nullable JsExpression receiver,
                                         @NotNull CallableDescriptor descriptor,
                                         @NotNull TranslationContext context) {
        return translate(receiver, Collections.<JsExpression>emptyList(),
                ResolvedCallImpl.create(descriptor), null, context);
    }

    @NotNull
    public static JsExpression translate(@Nullable JsExpression receiver,
                                         @NotNull ResolvedCall<?> resolvedCall,
                                         @Nullable CallableDescriptor descriptorToCall,
                                         @NotNull TranslationContext context) {
        return translate(receiver, Collections.<JsExpression>emptyList(), resolvedCall, descriptorToCall, context);
    }

    @NotNull
    public static JsExpression translate(@Nullable JsExpression receiver, @NotNull List<JsExpression> arguments,
                                         @NotNull ResolvedCall<?> resolvedCall,
                                         @Nullable CallableDescriptor descriptorToCall,
                                         @NotNull TranslationContext context) {
        return (new CallTranslator(receiver, null, arguments, resolvedCall, descriptorToCall, context)).translate();
    }

    @NotNull
    public static JsExpression translate(@NotNull JetUnaryExpression unaryExpression,
                                         @NotNull TranslationContext context) {
        return (new Builder(context).buildFromUnary(unaryExpression)).translate();
    }

    @NotNull
    public static JsExpression translate(@NotNull JetCallExpression callExpression,
                                         @Nullable JsExpression receiver,
                                         @NotNull TranslationContext context) {
        return (new Builder(context).buildFromCallExpression(callExpression, receiver)).translate();
    }

    @NotNull
    public static JsExpression translate(@NotNull JetBinaryExpression binaryExpression,
                                         @NotNull TranslationContext context) {
        boolean shouldSwapReceiverAndArgument = isInOrNotInOperation(binaryExpression);
        JsExpression result = (new Builder(context))
                .buildFromBinary(binaryExpression, shouldSwapReceiverAndArgument).translate();
        if (isInOrNotInOperation(binaryExpression)) {
            return mayBeWrapWithNegation(result, isNotInOperation(binaryExpression));
        }
        return result;
    }

    @NotNull
    private static JsExpression mayBeWrapWithNegation(@NotNull JsExpression expression, boolean shouldWrap) {
        if (shouldWrap) {
            return not(expression);
        } else {
            return expression;
        }
    }

    @Nullable
    private /*var*/ JsExpression receiver;

    @Nullable
    private final JsExpression callee;

    @NotNull
    private final List<JsExpression> arguments;

    @NotNull
    private final ResolvedCall<?> resolvedCall;

    @NotNull
    private final CallableDescriptor descriptor;

    private CallTranslator(@Nullable JsExpression receiver, @Nullable JsExpression callee,
                           @NotNull List<JsExpression> arguments,
                           @NotNull ResolvedCall<? extends CallableDescriptor> resolvedCall,
                           @Nullable CallableDescriptor descriptorToCall,
                           @NotNull TranslationContext context) {
        super(context);
        this.receiver = receiver;
        this.arguments = arguments;
        this.resolvedCall = resolvedCall;
        if (descriptorToCall != null) {
            this.descriptor = descriptorToCall;
        } else {
            this.descriptor = resolvedCall.getCandidateDescriptor().getOriginal();
        }
        this.callee = callee;
    }

    @NotNull
    private JsExpression translate() {
        if (isIntrinsic()) {
            return intrinsicInvocation();
        }
        if (isConstructor()) {
            return constructorCall();
        }
        if (isExtensionFunctionLiteral()) {
            return extensionFunctionLiteralCall();
        }
        if (isExtensionFunction()) {
            return extensionFunctionCall();
        }
        return methodCall();
    }

    @NotNull
    private JsExpression extensionFunctionLiteralCall() {
        receiver = getExtensionFunctionCallReceiver();
        List<JsExpression> callArguments = generateExtensionCallArgumentList();
        JsInvocation callMethodInvocation = generateCallMethodInvocation();
        callMethodInvocation.setArguments(callArguments);
        return callMethodInvocation;
    }

    private JsInvocation generateCallMethodInvocation() {
        JsNameRef callMethodNameRef = AstUtil.newQualifiedNameRef("call");
        JsInvocation callMethodInvocation = new JsInvocation();
        callMethodInvocation.setQualifier(callMethodNameRef);
        AstUtil.setQualifier(callMethodInvocation, calleeReference());
        return callMethodInvocation;
    }

    private boolean isExtensionFunctionLiteral() {
        boolean isLiteral = descriptor instanceof VariableAsFunctionDescriptor
                || descriptor instanceof ExpressionAsFunctionDescriptor;
        return isExtensionFunction() && isLiteral;
    }

    @NotNull
    private JsExpression extensionFunctionCall() {
        receiver = getExtensionFunctionCallReceiver();
        List<JsExpression> argumentList = generateExtensionCallArgumentList();
        return AstUtil.newInvocation(calleeReference(), argumentList);
    }

    @NotNull
    private List<JsExpression> generateExtensionCallArgumentList() {
        List<JsExpression> argumentList = new ArrayList<JsExpression>();
        argumentList.add(receiver);
        argumentList.addAll(arguments);
        //Now the rest of the code can work as if it was simple method invocation
        receiver = null;
        return argumentList;
    }

    @NotNull
    private JsExpression getExtensionFunctionCallReceiver() {
        if (receiver != null) {
            return receiver;
        }
        DeclarationDescriptor expectedReceiverDescriptor = getExpectedReceiverDescriptor(descriptor);
        assert expectedReceiverDescriptor != null;
        return getThisObject(context(), expectedReceiverDescriptor);
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    private boolean isExtensionFunction() {
        boolean hasReceiver = resolvedCall.getReceiverArgument().exists();
        return hasReceiver;
    }

    @NotNull
    private JsExpression intrinsicInvocation() {
        assert descriptor instanceof FunctionDescriptor;
        FunctionIntrinsic functionIntrinsic =
                context().intrinsics().getFunctionIntrinsic((FunctionDescriptor) descriptor);
        JsExpression receiverExpression = thisObject();
        return functionIntrinsic.apply(receiverExpression, arguments, context());
    }

    @NotNull
    private JsInvocation methodCall() {
        return AstUtil.newInvocation(calleeReference(), arguments);
    }

    private boolean isConstructor() {
        return isConstructorDescriptor(descriptor);
    }

    private boolean isIntrinsic() {
        return context().intrinsics().isIntrinsic(descriptor);
    }

    @NotNull
    private JsExpression calleeReference() {
        if (callee != null) {
            return callee;
        }
        if (isVariableDescriptor(descriptor)) {
            //TODO: write tests on this cases
            VariableDescriptor variableDescriptor =
                    getVariableDescriptorForVariableAsFunction((VariableAsFunctionDescriptor) descriptor);
            if (variableDescriptor instanceof PropertyDescriptor) {
                return getterCall((PropertyDescriptor) variableDescriptor);
            }
            return qualifiedMethodReference(variableDescriptor);
        }
        return qualifiedMethodReference(descriptor);
    }

    @NotNull
    private JsExpression getterCall(@NotNull PropertyDescriptor variableDescriptor) {
        return PropertyAccessTranslator.translateAsPropertyGetterCall(variableDescriptor, resolvedCall, context());
    }

    @NotNull
    private JsExpression qualifiedMethodReference(@NotNull DeclarationDescriptor descriptor) {
        JsExpression thisObject = thisObject();
        if (thisObject == null) {
            return ReferenceTranslator.translateAsFQReference(descriptor, context());
        }
        JsExpression methodReference = ReferenceTranslator.translateAsLocalNameReference(descriptor, context());
        AstUtil.setQualifier(methodReference, thisObject);
        return methodReference;
    }

    @Nullable
    private JsExpression thisObject() {
        if (receiver != null) {
            return receiver;
        }
        ReceiverDescriptor thisObject = resolvedCall.getThisObject();
        if (!thisObject.exists()) {
            return null;
        }
        DeclarationDescriptor expectedThisDescriptor = getDeclarationDescriptorForReceiver(thisObject);
        return TranslationUtils.getThisObject(context(), expectedThisDescriptor);
    }

    @NotNull
    private JsExpression constructorCall() {
        JsExpression constructorReference = ReferenceTranslator.translateAsFQReference(descriptor, context());
        JsNew constructorCall = new JsNew(constructorReference);
        constructorCall.setArguments(arguments);
        return constructorCall;
    }
}
