package org.jetbrains.k2js.translate.context;

import com.google.dart.compiler.backend.js.ast.JsName;
import com.google.dart.compiler.backend.js.ast.JsScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jet.lang.descriptors.ClassDescriptor;
import org.jetbrains.jet.lang.descriptors.DeclarationDescriptor;
import org.jetbrains.jet.lang.descriptors.FunctionDescriptor;
import org.jetbrains.jet.lang.descriptors.PropertyDescriptor;
import org.jetbrains.jet.lang.resolve.DescriptorUtils;
import org.jetbrains.jet.lang.types.JetStandardLibrary;

import java.util.HashMap;
import java.util.Map;

import static org.jetbrains.jet.lang.resolve.DescriptorUtils.getFQName;
import static org.jetbrains.k2js.translate.utils.DescriptorUtils.*;

/**
 * @author Talanov Pavel
 */
public final class StandardClasses {

    //TODO: move declaration code to some kind of builder
    @NotNull
    public static StandardClasses bindImplementations(@NotNull JetStandardLibrary standardLibrary,
                                                      @NotNull JsScope kotlinObjectScope) {
        StandardClasses standardClasses = new StandardClasses(kotlinObjectScope);
        declareArray(standardClasses, standardLibrary);
        declareIterator(standardClasses, standardLibrary);
        declareJavaArrayList(standardClasses);
        declareJavaSystem(standardClasses);
        return standardClasses;
    }

    private static void declareJavaSystem(@NotNull StandardClasses standardClasses) {
        String systemFQName = "<java_root>.java.lang.System";
        standardClasses.declareStandardTopLevelObject(systemFQName, "System");
        declareMethods(standardClasses, systemFQName, "out");
        String printStreamFQName = "<java_root>.java.io.PrintStream";
        //TODO:
        standardClasses.declareStandardTopLevelObject(printStreamFQName, "ErrorName");
        declareMethods(standardClasses, printStreamFQName, "print", "println");
    }

    private static void declareJavaArrayList(@NotNull StandardClasses standardClasses) {
        String arrayListFQName = "<java_root>.java.util.ArrayList";
        standardClasses.declareStandardTopLevelObject(arrayListFQName, "ArrayList");
        standardClasses.declareStandardInnerDeclaration(arrayListFQName, "<init>", "ArrayList");
        declareMethods(standardClasses, arrayListFQName, "size", "add", "get",
                "isEmpty", "set", "remove", "addAll");
    }

    private static void declareIterator(@NotNull StandardClasses standardClasses,
                                        @NotNull JetStandardLibrary standardLibrary) {
        ClassDescriptor iteratorClass = (ClassDescriptor)
                standardLibrary.getLibraryScope().getClassifier("Iterator");
        assert iteratorClass != null;
        standardClasses.declareStandardTopLevelObject(iteratorClass, "ArrayIterator");
        declareMethods(standardClasses, getFQName(iteratorClass), "next", "hasNext");
    }

    private static void declareArray(@NotNull StandardClasses standardClasses,
                                     @NotNull JetStandardLibrary standardLibrary) {
        ClassDescriptor arrayClass = standardLibrary.getArray();
        standardClasses.declareStandardTopLevelObject(arrayClass, "Array");
        FunctionDescriptor nullConstructorFunction = getFunctionByName(standardLibrary.getLibraryScope(), "Array");
        standardClasses.declareStandardTopLevelObject(nullConstructorFunction, "array");
        PropertyDescriptor sizeProperty =
                getPropertyByName(arrayClass.getDefaultType().getMemberScope(), "size");
        standardClasses.declareStandardInnerDeclaration(sizeProperty, "size");
    }


    private static void declareMethods(@NotNull StandardClasses standardClasses,
                                       @NotNull String classFQName,
                                       @NotNull String... methodNames) {
        for (String methodName : methodNames) {
            standardClasses.declareStandardInnerDeclaration(classFQName, methodName, methodName);
        }
    }


    @NotNull
    private final JsScope kotlinScope;

    @NotNull
    private final Map<String, JsName> nameMap = new HashMap<String, JsName>();

    @NotNull
    private final Map<String, JsScope> scopeMap = new HashMap<String, JsScope>();

    private StandardClasses(@NotNull JsScope kotlinScope) {
        this.kotlinScope = kotlinScope;
    }

    private void declareStandardTopLevelObject(@NotNull DeclarationDescriptor descriptor,
                                               @NotNull String kotlinLibName) {
        declareStandardTopLevelObject(DescriptorUtils.getFQName(descriptor), kotlinLibName);
    }

    private void declareStandardTopLevelObject(@NotNull String fullQualifiedName, @NotNull String kotlinLibName) {
        nameMap.put(fullQualifiedName, kotlinScope.declareName(kotlinLibName));
        scopeMap.put(fullQualifiedName, new JsScope(kotlinScope, "standard object " + kotlinLibName));
    }

    private void declareStandardInnerDeclaration(@NotNull DeclarationDescriptor descriptor,
                                                 @NotNull String kotlinLibName) {
        String containingFQName = DescriptorUtils.getFQName(getContainingDeclaration(descriptor));
        declareStandardInnerDeclaration(containingFQName, descriptor.getName(), kotlinLibName);
    }

    private void declareStandardInnerDeclaration(@NotNull String fullQualifiedClassName,
                                                 @NotNull String shortMethodName,
                                                 @NotNull String kotlinLibName) {
        JsScope classScope = scopeMap.get(fullQualifiedClassName);
        String fullQualifiedMethodName = fullQualifiedClassName + "." + shortMethodName;
        nameMap.put(fullQualifiedMethodName, classScope.declareName(kotlinLibName));
    }

    public boolean isStandardObject(@NotNull DeclarationDescriptor descriptor) {
        return nameMap.containsKey(getFQName(descriptor));
    }

    @NotNull
    public JsName getStandardObjectName(@NotNull DeclarationDescriptor descriptor) {
        return nameMap.get(getFQName(descriptor));
    }
}
