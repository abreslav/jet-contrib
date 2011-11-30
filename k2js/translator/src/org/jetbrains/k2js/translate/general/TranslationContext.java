package org.jetbrains.k2js.translate.general;

import com.google.dart.compiler.backend.js.ast.JsName;
import com.google.dart.compiler.backend.js.ast.JsProgram;
import com.google.dart.compiler.backend.js.ast.JsScope;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jet.lang.descriptors.*;
import org.jetbrains.jet.lang.psi.*;
import org.jetbrains.jet.lang.resolve.BindingContext;
import org.jetbrains.jet.lang.types.JetStandardLibrary;
import org.jetbrains.k2js.declarations.Declarations;
import org.jetbrains.k2js.translate.utils.BindingUtils;
import org.jetbrains.k2js.translate.utils.Namer;

/**
 * @author Talanov Pavel
 */
public final class TranslationContext {

    // TODO: extract different parts of this classes into separate classes
    private static class Scopes {
        public Scopes(@NotNull JsScope enclosingScope, @NotNull JsScope functionScope, @NotNull JsScope namespaceScope) {
            this.enclosingScope = enclosingScope;
            this.classScope = functionScope;
            this.namespaceScope = namespaceScope;
        }

        @NotNull
        public final JsScope enclosingScope;
        @NotNull
        public final JsScope classScope;
        @NotNull
        public final JsScope namespaceScope;
    }

    @NotNull
    public static TranslationContext rootContext(@NotNull JsProgram program, @NotNull BindingContext bindingContext,
                                                 @NotNull Declarations extractor, @NotNull Project project) {
        JsScope rootScope = program.getRootScope();
        Scopes scopes = new Scopes(rootScope, rootScope, rootScope);
        Namer namer = Namer.newInstance(rootScope);
        Aliaser aliaser = Aliaser.aliasesForStandardClasses
                (JetStandardLibrary.getJetStandardLibrary(project), namer);
        return new TranslationContext(program, bindingContext,
                scopes, extractor, aliaser, namer);
    }

    @NotNull
    private final JsProgram program;
    @NotNull
    private final BindingContext bindingContext;
    @NotNull
    private final Scopes scopes;
    @NotNull
    private final Declarations declarations;
    @NotNull
    private final Aliaser aliaser;
    @NotNull
    private final Namer namer;


    private TranslationContext(@NotNull JsProgram program,
                               @NotNull BindingContext bindingContext, @NotNull Scopes scopes,
                               @NotNull Declarations declarations, @NotNull Aliaser aliaser,
                               @NotNull Namer namer) {
        this.program = program;
        this.bindingContext = bindingContext;
        this.scopes = scopes;
        this.declarations = declarations;
        this.aliaser = aliaser;
        this.namer = namer;
    }

    @NotNull
    public TranslationContext newNamespace(@NotNull JetNamespace declaration) {
        return newNamespace(BindingUtils.getNamespaceDescriptor(bindingContext, declaration));
    }

    @NotNull
    public TranslationContext newNamespace(@NotNull NamespaceDescriptor descriptor) {
        JsScope namespaceScope = declarations.getScope(descriptor);
        Scopes newScopes = new Scopes(namespaceScope, namespaceScope, namespaceScope);
        return new TranslationContext(program, bindingContext, newScopes,
                declarations, aliaser, namer);
    }

    @NotNull
    public TranslationContext newClass(@NotNull JetClass declaration) {
        return newClass(BindingUtils.getClassDescriptor(bindingContext, declaration));
    }

    @NotNull
    public TranslationContext newClass(@NotNull ClassDescriptor descriptor) {
        JsScope classScope = declarations.getScope(descriptor);
        Scopes newScopes = new Scopes(classScope, classScope, scopes.namespaceScope);
        return new TranslationContext(program, bindingContext,
                newScopes, declarations, aliaser, namer);
    }

    @NotNull
    public TranslationContext newPropertyAccess(@NotNull JetPropertyAccessor declaration) {
        return newPropertyAccess(BindingUtils.getPropertyAccessorDescriptor(bindingContext, declaration));
    }

    @NotNull
    public TranslationContext newPropertyAccess(@NotNull PropertyAccessorDescriptor descriptor) {
        return newFunctionDeclaration(descriptor);
    }

    @NotNull
    public TranslationContext newFunctionDeclaration(@NotNull JetNamedFunction declaration) {
        return newFunctionDeclaration(BindingUtils.getFunctionDescriptor(bindingContext, declaration));
    }

    @NotNull
    public TranslationContext newFunctionDeclaration(@NotNull FunctionDescriptor descriptor) {
        JsScope functionScope = declarations.getScope(descriptor);
        Scopes newScopes = new Scopes(functionScope, scopes.classScope, scopes.namespaceScope);
        return new TranslationContext(program, bindingContext,
                newScopes, declarations, aliaser, namer);
    }

    // Note: Should be used if and only if scope has no corresponding descriptor
    @NotNull
    public TranslationContext newEnclosingScope(@NotNull JsScope enclosingScope) {
        Scopes newScopes = new Scopes(enclosingScope, scopes.classScope, scopes.namespaceScope);
        return new TranslationContext(program, bindingContext,
                newScopes, declarations, aliaser, namer);
    }

    @NotNull
    public BindingContext bindingContext() {
        return bindingContext;
    }

    @NotNull
    public JsProgram program() {
        return program;
    }

    @NotNull
    public JsScope enclosingScope() {
        return scopes.enclosingScope;
    }

    @NotNull
    public JsScope namespaceScope() {
        return scopes.namespaceScope;
    }

    @NotNull
    public JsScope classScope() {
        return scopes.classScope;
    }


    @NotNull
    public JsScope getScopeForDescriptor(@NotNull DeclarationDescriptor descriptor) {
        return declarations.getScope(descriptor);
    }

    @NotNull
    public JsScope getScopeForElement(@NotNull JetElement element) {
        DeclarationDescriptor descriptor = getDescriptorForElement(element);
        return getScopeForDescriptor(descriptor);
    }

    @NotNull
    public JsName getNameForDescriptor(@NotNull DeclarationDescriptor descriptor) {
        return declarations.getName(descriptor);
    }

    @NotNull
    public JsName getNameForElement(@NotNull JetElement element) {
        DeclarationDescriptor descriptor = getDescriptorForElement(element);
        return getNameForDescriptor(descriptor);
    }

    @NotNull
    private DeclarationDescriptor getDescriptorForElement(@NotNull JetElement element) {
        DeclarationDescriptor descriptor = bindingContext.get(BindingContext.DECLARATION_TO_DESCRIPTOR, element);
        assert descriptor != null : "Element should have a descriptor";
        return descriptor;
    }

    public boolean isDeclared(@NotNull DeclarationDescriptor descriptor) {
        return declarations.hasDeclaredName(descriptor);
    }

    @NotNull
    public Aliaser aliaser() {
        return aliaser;
    }

    @NotNull
    public Namer namer() {
        return namer;
    }

    @NotNull
    public Declarations declarations() {
        return declarations;
    }
}
