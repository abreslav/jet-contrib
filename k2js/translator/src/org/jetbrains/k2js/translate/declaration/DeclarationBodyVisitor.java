package org.jetbrains.k2js.translate.declaration;

import com.google.dart.compiler.backend.js.ast.JsPropertyInitializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jet.lang.descriptors.NamespaceDescriptor;
import org.jetbrains.jet.lang.descriptors.PropertyDescriptor;
import org.jetbrains.jet.lang.psi.*;
import org.jetbrains.k2js.translate.context.TranslationContext;
import org.jetbrains.k2js.translate.general.Translation;
import org.jetbrains.k2js.translate.general.TranslatorVisitor;
import org.jetbrains.k2js.translate.utils.BindingUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.jetbrains.k2js.translate.utils.BindingUtils.getDeclarationsForNamespace;
import static org.jetbrains.k2js.translate.utils.BindingUtils.getPropertyDescriptorForObjectDeclaration;

/**
 * @author Pavel Talanov
 */
public final class DeclarationBodyVisitor extends TranslatorVisitor<List<JsPropertyInitializer>> {


    @NotNull
    public List<JsPropertyInitializer> traverseClass(@NotNull JetClassOrObject jetClass,
                                                     @NotNull TranslationContext context) {
        List<JsPropertyInitializer> properties = new ArrayList<JsPropertyInitializer>();
        for (JetDeclaration declaration : jetClass.getDeclarations()) {
            properties.addAll(declaration.accept(this, context));
        }
        return properties;
    }

    @NotNull
    public List<JsPropertyInitializer> traverseNamespace(@NotNull NamespaceDescriptor namespace,
                                                         @NotNull TranslationContext context) {
        List<JsPropertyInitializer> properties = new ArrayList<JsPropertyInitializer>();
        for (JetDeclaration declaration : getDeclarationsForNamespace(context.bindingContext(), namespace)) {
            properties.addAll(declaration.accept(this, context));
        }
        return properties;
    }

    @Override
    @NotNull
    public List<JsPropertyInitializer> visitClass(@NotNull JetClass expression, @NotNull TranslationContext context) {
        return Collections.emptyList();
    }

    @Override
    @NotNull
    public List<JsPropertyInitializer> visitNamedFunction(@NotNull JetNamedFunction expression,
                                                          @NotNull TranslationContext context) {
        return Collections.singletonList(Translation.functionTranslator(expression, context).translateAsMethod());
    }

    @Override
    @NotNull
    public List<JsPropertyInitializer> visitProperty(@NotNull JetProperty expression,
                                                     @NotNull TranslationContext context) {
        PropertyDescriptor propertyDescriptor =
                BindingUtils.getPropertyDescriptor(context.bindingContext(), expression);
        return PropertyTranslator.translateAccessors(propertyDescriptor, context);
    }

    @Override
    @NotNull
    public List<JsPropertyInitializer> visitObjectDeclaration(@NotNull JetObjectDeclaration expression,
                                                              @NotNull TranslationContext context) {
        return Collections.singletonList(ClassTranslator.translateAsProperty(expression, context));
    }

    @Override
    @NotNull
    public List<JsPropertyInitializer> visitObjectDeclarationName(@NotNull JetObjectDeclarationName expression,
                                                                  @NotNull TranslationContext context) {
        PropertyDescriptor propertyDescriptor =
                getPropertyDescriptorForObjectDeclaration(context.bindingContext(), expression);
        return PropertyTranslator.translateAccessors(propertyDescriptor, context);
    }

    @Override
    @NotNull
    public List<JsPropertyInitializer> visitAnonymousInitializer(@NotNull JetClassInitializer expression,
                                                                 @NotNull TranslationContext context) {
        // parsed it in initializer visitor => no additional actions are needed
        return Collections.emptyList();
    }
}
