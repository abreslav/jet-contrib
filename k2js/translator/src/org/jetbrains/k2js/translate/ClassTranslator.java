package org.jetbrains.k2js.translate;

import com.google.dart.compiler.backend.js.ast.*;
import com.google.dart.compiler.util.AstUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jet.lang.descriptors.ClassDescriptor;
import org.jetbrains.jet.lang.psi.JetClass;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Talanov Pavel
 */
//TODO ClassTranslator needs heavy improvement
// did some improvement
public final class ClassTranslator extends AbstractTranslator {

    @NotNull
    private final ClassBodyVisitor classBodyVisitor = new ClassBodyVisitor();

    @NotNull
    public static ClassTranslator newInstance(@NotNull TranslationContext context) {
        return new ClassTranslator(context);
    }

    private ClassTranslator(TranslationContext context) {
        super(context);
    }

    @NotNull
    public JsStatement translateClass(@NotNull JetClass classDeclaration) {
        JsInvocation jsClassDeclaration = new JsInvocation();
        jsClassDeclaration.setQualifier(Namer.creationMethodReference());
        addSuperclassReferences(classDeclaration, jsClassDeclaration);
        addClassOwnDeclarations(classDeclaration, jsClassDeclaration);
        JsName jsClassName = translationContext().getNameForElement(classDeclaration);
        return AstUtil.convertToStatement(
                AstUtil.newAssignment(translationContext().getNamespaceQualifiedReference(jsClassName), jsClassDeclaration));
    }

    private void addClassOwnDeclarations(@NotNull JetClass classDeclaration,
                                         @NotNull JsInvocation jsClassDeclaration) {
        JsObjectLiteral jsClassDescription = translateClassDeclarations(classDeclaration);
        jsClassDeclaration.getArguments().add(jsClassDescription);
    }

    private void addSuperclassReferences(@NotNull JetClass classDeclaration,
                                         @NotNull JsInvocation jsClassDeclaration) {
        for (JsNameRef superClassReference : getSuperclassNameReferences(classDeclaration)) {
            jsClassDeclaration.getArguments().add(superClassReference);
        }
    }

    @NotNull
    private List<JsNameRef> getSuperclassNameReferences(@NotNull JetClass classDeclaration) {
        List<JsNameRef> superclassReferences = new ArrayList<JsNameRef>();
        for (ClassDescriptor superClassDescriptor :
                BindingUtils.getSuperclassDescriptors(translationContext().bindingContext(), classDeclaration)) {
            //TODO should get a full class name here
            superclassReferences.add(translationContext().getNamespaceQualifiedReference
                    (translationContext().getNameForDescriptor(superClassDescriptor)));
        }
        return superclassReferences;
    }

    @NotNull
    private JsObjectLiteral translateClassDeclarations(@NotNull JetClass classDeclaration) {
        List<JsPropertyInitializer> propertyList = new ArrayList<JsPropertyInitializer>();
        propertyList.add(generateInitializeMethod(classDeclaration));
        propertyList.addAll(classDeclaration.accept(classBodyVisitor,
                translationContext().newClass(classDeclaration)));
        return new JsObjectLiteral(propertyList);
    }

    @NotNull
    private JsPropertyInitializer generateInitializeMethod(@NotNull JetClass classDeclaration) {
        JsPropertyInitializer initializer = new JsPropertyInitializer();
        initializer.setLabelExpr(program().getStringLiteral(Namer.INITIALIZE_METHOD_NAME));
        initializer.setValueExpr(generateInitializeMethodBody(classDeclaration));
        return initializer;
    }

    @NotNull
    private JsFunction generateInitializeMethodBody(@NotNull JetClass classDeclaration) {
        InitializerVisitor initializerVisitor = new InitializerVisitor(classDeclaration,
                translationContext().newClass(classDeclaration));
        return initializerVisitor.generateInitializeMethodBody();
    }
}
