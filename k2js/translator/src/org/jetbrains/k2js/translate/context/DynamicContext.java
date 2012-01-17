package org.jetbrains.k2js.translate.context;

import com.google.dart.compiler.backend.js.ast.*;
import com.google.dart.compiler.util.AstUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jet.lang.descriptors.DeclarationDescriptor;

//TODO: track that temporary declarations is in the scope of the block where they are declared

public class DynamicContext {

    @NotNull
    public static DynamicContext rootContext(@NotNull NamingScope rootScope, @NotNull JsBlock globalBlock) {
        return new DynamicContext(rootScope, rootScope, globalBlock);
    }

    @NotNull
    public static DynamicContext contextWithScope(@NotNull NamingScope scope) {
        return new DynamicContext(scope, scope, new JsBlock());
    }

    @NotNull
    private NamingScope currentScope;
    @NotNull
    private NamingScope blockScope;
    @NotNull
    private JsBlock currentBlock;

    private DynamicContext(@NotNull NamingScope scope, @NotNull NamingScope blockScope, @NotNull JsBlock block) {
        this.currentScope = scope;
        this.currentBlock = block;
        this.blockScope = blockScope;
    }

    @NotNull
    public DynamicContext innerScope(@NotNull JsScope scope) {
        return new DynamicContext(currentScope.innerScope(scope), blockScope, currentBlock);
    }

    @NotNull
    public DynamicContext innerBlock(@NotNull JsBlock block) {
        return new DynamicContext(currentScope, currentScope, block);
    }

    @NotNull
    public JsName getLocalName(@NotNull DeclarationDescriptor descriptor) {
        JsName name = currentScope.getName(descriptor);
        assert name != null : descriptor.getName() + " is not declared. Use isDeclared to check.";
        return name;
    }

    public boolean isDeclared(@NotNull DeclarationDescriptor descriptor) {
        return currentScope.isDeclared(descriptor);
    }

    @NotNull
    public TemporaryVariable declareTemporary(@NotNull JsExpression initExpression) {
        JsName temporaryName = blockScope.declareTemporary();
        JsVars temporaryDeclaration = AstUtil.newVar(temporaryName, null);
        jsBlock().addVarDeclaration(temporaryDeclaration);
        return new TemporaryVariable(temporaryName, initExpression);
    }

    @NotNull
    public JsName declareLocalVariable(@NotNull DeclarationDescriptor descriptor) {
        return currentScope.declareVariable(descriptor, descriptor.getName());
    }

    @NotNull
    public JsScope jsScope() {
        return currentScope.jsScope();
    }

    @NotNull
    public JsBlock jsBlock() {
        return currentBlock;
    }
}