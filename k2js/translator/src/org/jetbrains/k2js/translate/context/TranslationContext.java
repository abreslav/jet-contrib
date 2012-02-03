package org.jetbrains.k2js.translate.context;

import com.google.dart.compiler.backend.js.ast.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jet.lang.descriptors.DeclarationDescriptor;
import org.jetbrains.jet.lang.descriptors.NamespaceDescriptor;
import org.jetbrains.jet.lang.descriptors.PropertyAccessorDescriptor;
import org.jetbrains.jet.lang.psi.JetClass;
import org.jetbrains.jet.lang.psi.JetElement;
import org.jetbrains.jet.lang.psi.JetNamedFunction;
import org.jetbrains.jet.lang.psi.JetPropertyAccessor;
import org.jetbrains.jet.lang.resolve.BindingContext;
import org.jetbrains.k2js.translate.intrinsic.Intrinsics;
import org.jetbrains.k2js.translate.utils.BindingUtils;

/**
 * @author Pavel Talanov
 */
public final class TranslationContext {

    @NotNull
    private final DynamicContext dynamicContext;
    @NotNull
    private final StaticContext staticContext;

    @NotNull
    public static TranslationContext rootContext(@NotNull StaticContext staticContext) {
        JsProgram program = staticContext.getProgram();
        JsBlock globalBlock = program.getGlobalBlock();
        return new TranslationContext(staticContext,
                DynamicContext.rootContext(staticContext.getRootScope(), globalBlock));
    }

    private TranslationContext(@NotNull StaticContext staticContext, @NotNull DynamicContext dynamicContext) {
        this.dynamicContext = dynamicContext;
        this.staticContext = staticContext;
    }

    @NotNull
    public TranslationContext contextWithScope(@NotNull NamingScope newScope) {
        return new TranslationContext(staticContext, DynamicContext.contextWithScope(newScope));
    }

    // Note: Should be used ONLY if scope has no corresponding descriptor
    @NotNull
    public TranslationContext innerJsScope(@NotNull JsScope enclosingScope) {
        return new TranslationContext(staticContext, dynamicContext.innerScope(enclosingScope));
    }

    @NotNull
    public TranslationContext innerBlock(@NotNull JsBlock block) {
        return new TranslationContext(staticContext, dynamicContext.innerBlock(block));
    }

    @NotNull
    public TranslationContext newNamespace(@NotNull NamespaceDescriptor namespace) {
        return newDeclaration(namespace);
    }

    @NotNull
    public TranslationContext newDeclaration(@NotNull DeclarationDescriptor descriptor) {
        return contextWithScope(getScopeForDescriptor(descriptor));
    }

    @NotNull
    public TranslationContext newClass(@NotNull JetClass declaration) {
        return newDeclaration(BindingUtils.getClassDescriptor(staticContext.getBindingContext(), declaration));
    }

    @NotNull
    public TranslationContext newPropertyAccess(@NotNull JetPropertyAccessor declaration) {
        return newDeclaration(BindingUtils.getPropertyAccessorDescriptor(staticContext.getBindingContext(), declaration));
    }

    @NotNull
    public TranslationContext newPropertyAccess(@NotNull PropertyAccessorDescriptor descriptor) {
        return newDeclaration(descriptor);
    }

    @NotNull
    public TranslationContext newFunctionDeclaration(@NotNull JetNamedFunction declaration) {
        return newDeclaration(BindingUtils.getFunctionDescriptor(staticContext.getBindingContext(), declaration));
    }

    @NotNull
    public BindingContext bindingContext() {
        return staticContext.getBindingContext();
    }


    @NotNull
    public NamingScope getScopeForDescriptor(@NotNull DeclarationDescriptor descriptor) {
        return staticContext.getScopeForDescriptor(descriptor);
    }

    @NotNull
    public NamingScope getScopeForElement(@NotNull JetElement element) {
        DeclarationDescriptor descriptorForElement = BindingUtils.getDescriptorForElement(bindingContext(), element);
        return getScopeForDescriptor(descriptorForElement);
    }

    @NotNull
    public JsName getNameForElement(@NotNull JetElement element) {
        DeclarationDescriptor descriptor = BindingUtils.getDescriptorForElement(bindingContext(), element);
        return getNameForDescriptor(descriptor);
    }

    @NotNull
    public JsName getNameForDescriptor(@NotNull DeclarationDescriptor descriptor) {
        return staticContext.getNameForDescriptor(descriptor);
    }

    @Nullable
    public JsNameRef getQualifierForDescriptor(@NotNull DeclarationDescriptor descriptor) {
        return staticContext.getQualifierForDescriptor(descriptor.getOriginal());
    }

    @NotNull
    public TemporaryVariable declareTemporary(@NotNull JsExpression initExpression) {
        return dynamicContext.declareTemporary(initExpression);
    }

    @NotNull
    public Aliaser aliaser() {
        return staticContext.getAliaser();
    }

    @NotNull
    public Namer namer() {
        return staticContext.getNamer();
    }

    @NotNull
    public Intrinsics intrinsics() {
        return staticContext.getIntrinsics();
    }

    @NotNull
    public JsProgram program() {
        return staticContext.getProgram();
    }

    @NotNull
    private StandardClasses standardClasses() {
        return staticContext.getStandardClasses();
    }

    @NotNull
    public JsScope jsScope() {
        return dynamicContext.jsScope();
    }

    @NotNull
    public JsBlock jsBlock() {
        return dynamicContext.jsBlock();
    }
}
