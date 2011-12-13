package org.jetbrains.jet.j2k.visitors;

import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jet.j2k.ast.*;
import org.jetbrains.jet.j2k.util.AstUtil;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static org.jetbrains.jet.j2k.Converter.typeToType;
import static org.jetbrains.jet.j2k.Converter.typesToTypeList;

/**
 * @author ignatov
 */
public class TypeVisitor extends PsiTypeVisitor<Type> {
  private Type myResult = Type.EMPTY_TYPE;

  @NotNull
  public Type getResult() {
    return myResult;
  }

  @Override
  public Type visitPrimitiveType(@NotNull PsiPrimitiveType primitiveType) {
    final String name = primitiveType.getCanonicalText();
    final IdentifierImpl identifier = new IdentifierImpl(name);

    if (name.equals("void"))
      myResult = new PrimitiveType(new IdentifierImpl("Unit"));
    else if (Node.PRIMITIVE_TYPES.contains(name))
      myResult = new PrimitiveType(new IdentifierImpl(AstUtil.upperFirstCharacter(name)));
    else
      myResult = new PrimitiveType(identifier);
    return super.visitPrimitiveType(primitiveType);
  }

  @Override
  public Type visitArrayType(@NotNull PsiArrayType arrayType) {
    if (myResult == Type.EMPTY_TYPE)
      myResult = new ArrayType(typeToType(arrayType.getComponentType()));
    return super.visitArrayType(arrayType);
  }

  @Override
  public Type visitClassType(@NotNull PsiClassType classType) {
    final IdentifierImpl identifier = constructClassTypeIdentifier(classType);
    final List<Type> resolvedClassTypeParams = createRawTypesForResolvedReference(classType);

    if (classType.getParameterCount() == 0 && resolvedClassTypeParams.size() > 0)
      myResult = new ClassType(identifier, resolvedClassTypeParams);
    else
      myResult = new ClassType(identifier, typesToTypeList(classType.getParameters()));
    return super.visitClassType(classType);
  }

  @NotNull
  private static IdentifierImpl constructClassTypeIdentifier(@NotNull PsiClassType classType) {
    final PsiClass psiClass = classType.resolve();
    if (psiClass != null) {
      String qualifiedName = psiClass.getQualifiedName();
      if (qualifiedName != null) {
        if (qualifiedName.equals("java.lang.Iterable"))
          return new IdentifierImpl("java.lang.Iterable");
        if (qualifiedName.equals("java.util.Iterator"))
          return new IdentifierImpl("java.util.Iterator");
      }
    }
    final String classTypeName = createQualifiedName(classType);

    if (classTypeName.isEmpty())
      return new IdentifierImpl(getClassTypeName(classType));

    return new IdentifierImpl(classTypeName);
  }

  @NotNull
  private static String createQualifiedName(@NotNull PsiClassType classType) {
    String classTypeName = "";
    if (classType instanceof PsiClassReferenceType) {
      final PsiJavaCodeReferenceElement reference = ((PsiClassReferenceType) classType).getReference();
      if (reference.isQualified()) {
        String result = new IdentifierImpl(reference.getReferenceName()).toKotlin();
        PsiElement qualifier = reference.getQualifier();
        while (qualifier != null) {
          final PsiJavaCodeReferenceElement p = (PsiJavaCodeReferenceElement) qualifier;
          result = new IdentifierImpl(p.getReferenceName()).toKotlin() + "." + result; // TODO: maybe need to replace by safe call?
          qualifier = p.getQualifier();
        }
        classTypeName = result;
      }
    }
    return classTypeName;
  }

  @NotNull
  private static List<Type> createRawTypesForResolvedReference(@NotNull PsiClassType classType) {
    final List<Type> typeParams = new LinkedList<Type>();
    if (classType instanceof PsiClassReferenceType) {
      final PsiJavaCodeReferenceElement reference = ((PsiClassReferenceType) classType).getReference();
      final PsiElement resolve = reference.resolve();
      if (resolve != null) {
        if (resolve instanceof PsiClass)
          //noinspection UnusedDeclaration
          for (PsiTypeParameter p : ((PsiClass) resolve).getTypeParameters())
            typeParams.add(new StarProjectionType());
      }
    }
    return typeParams;
  }

  @NotNull
  private static String getClassTypeName(@NotNull PsiClassType classType) {
    String canonicalTypeStr = classType.getCanonicalText();
    if (canonicalTypeStr.equals("java.lang.Object")) return "Any";
    if (canonicalTypeStr.equals("java.lang.Byte")) return "Byte";
    if (canonicalTypeStr.equals("java.lang.Character")) return "Char";
    if (canonicalTypeStr.equals("java.lang.Double")) return "Double";
    if (canonicalTypeStr.equals("java.lang.Float")) return "Float";
    if (canonicalTypeStr.equals("java.lang.Integer")) return "Int";
    if (canonicalTypeStr.equals("java.lang.Long")) return "Long";
    if (canonicalTypeStr.equals("java.lang.Short")) return "Short";
    if (canonicalTypeStr.equals("java.lang.Boolean")) return "Boolean";
    return classType.getClassName() != null ? classType.getClassName() : classType.getCanonicalText();
  }

  @Override
  public Type visitWildcardType(@NotNull PsiWildcardType wildcardType) {
    if (wildcardType.isExtends())
      myResult = new OutProjectionType(typeToType(wildcardType.getExtendsBound()));
    else if (wildcardType.isSuper())
      myResult = new InProjectionType(typeToType(wildcardType.getSuperBound()));
    else
      myResult = new StarProjectionType();
    return super.visitWildcardType(wildcardType);
  }

  @Override
  public Type visitEllipsisType(@NotNull PsiEllipsisType ellipsisType) {
    myResult = new VarArg(typeToType(ellipsisType.getComponentType()));
    return super.visitEllipsisType(ellipsisType);
  }

  @Override
  public Type visitCapturedWildcardType(PsiCapturedWildcardType capturedWildcardType) {
    return super.visitCapturedWildcardType(capturedWildcardType);
  }

  @Override
  public Type visitDisjunctionType(PsiDisjunctionType disjunctionType) {
    return super.visitDisjunctionType(disjunctionType);
  }
}

