package org.jetbrains.jet.j2k;

import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jet.j2k.ast.*;
import org.jetbrains.jet.j2k.ast.Class;
import org.jetbrains.jet.j2k.ast.Enum;
import org.jetbrains.jet.j2k.visitors.ElementVisitor;
import org.jetbrains.jet.j2k.visitors.ExpressionVisitor;
import org.jetbrains.jet.j2k.visitors.StatementVisitor;
import org.jetbrains.jet.j2k.visitors.TypeVisitor;

import java.util.LinkedList;
import java.util.List;

/**
 * @author ignatov
 */
public class Converter {
  @NotNull
  public static File fileToFile(PsiJavaFile javaFile) {
    final PsiImportList importList = javaFile.getImportList();
    List<Import> imports = importList == null ?
      new LinkedList<Import>() :
      importsToImportList(importList.getAllImportStatements());
    return new File(javaFile.getPackageName(), imports, classesToClassList(javaFile.getClasses())); // TODO: use identifier
  }

  @NotNull
  private static List<Class> classesToClassList(PsiClass[] classes) {
    List<Class> result = new LinkedList<Class>();
    for (PsiClass t : classes) {
      result.add(classToClass(t));
    }
    return result;
  }

  public static Class classToClass(PsiClass psiClass) {
    final List<Function> methods = methodsToFunctionList(psiClass.getMethods(), true);

    final List<Class> innerClasses = classesToClassList(psiClass.getAllInnerClasses());

    final List<Field> fields = fieldsToFieldList(psiClass.getAllFields());

    final IdentifierImpl name = new IdentifierImpl(psiClass.getName());
    if (psiClass.isInterface())
      return new Trait(name, innerClasses, methods, fields);
    if (psiClass.isEnum())
      return new Enum(name, innerClasses, methods, fields);
    return new Class(name, innerClasses, methods, fields);
  }

  private static List<Field> fieldsToFieldList(PsiField[] fields) {
    List<Field> result = new LinkedList<Field>();
    for (PsiField f : fields) {
      result.add(fieldToField(f));
    }
    return result;
  }

  private static Field fieldToField(PsiField field) {
    return new Field(
      new IdentifierImpl(field.getName()), // TODO
      typeToType(field.getType()),
      expressionToExpression(field.getInitializer()) // TODO: add modifiers
    );
  }

  @NotNull
  private static List<Function> methodsToFunctionList(PsiMethod[] methods, boolean notEmpty) {
    List<Function> result = new LinkedList<Function>();
    for (PsiMethod t : methods) {
      result.add(methodToFunction(t, notEmpty));
    }
    return result;
  }

  @NotNull
  private static Function methodToFunction(PsiMethod t, boolean notEmpty) {
    return new Function(new IdentifierImpl(t.getName()), typeToType(t.getReturnType()), bodyToBlock(t.getBody(), notEmpty));
  }

  @NotNull
  private static Block bodyToBlock(PsiCodeBlock body, boolean notEmpty) {
    if (body == null)
      return new Block(new LinkedList<Statement>(), false);
    return new Block(statementsToStatementList(body.getStatements()), notEmpty);
  }

  @NotNull
  public static List<Statement> statementsToStatementList(PsiStatement[] statements) {
    List<Statement> result = new LinkedList<Statement>();
    for (PsiStatement t : statements) {
      result.add(statementToStatement(t));
    }
    return result;
  }

  @NotNull
  public static Statement statementToStatement(PsiStatement t) {
    final StatementVisitor statementVisitor = new StatementVisitor();
    t.accept(statementVisitor);
    System.out.println(t.getClass());
    return statementVisitor.getStatement();
  }

  @NotNull
  public static List<Expression> expressionsToExpressionList(PsiExpression[] expressions) {
    List<Expression> result = new LinkedList<Expression>();
    for (PsiExpression e : expressions) {
      result.add(expressionToExpression(e));
    }
    return result;
  }

  @NotNull
  public static Expression expressionToExpression(PsiExpression e) {
    if (e == null)
      return new EmptyExpression();
    final ExpressionVisitor expressionVisitor = new ExpressionVisitor();
    e.accept(expressionVisitor);
    System.out.println(e.getClass());
    return expressionVisitor.getResult();
  }

  @NotNull
  public static Element elementToElement(PsiElement e) {
    final ElementVisitor elementVisitor = new ElementVisitor();
    e.accept(elementVisitor);
    System.out.println(e.getClass());
    return elementVisitor.getResult();
  }

  @NotNull
  public static List<Element> elementsToElementList(PsiElement[] elements) {
    List<Element> result = new LinkedList<Element>();
    for (PsiElement e : elements) {
      result.add(elementToElement(e));
    }
    return result;
  }

  @NotNull
  public static Type typeToType(PsiType type) {
    if (type == null)
      return new EmptyType(); // TODO
    TypeVisitor typeVisitor = new TypeVisitor();
    type.accept(typeVisitor);
    return typeVisitor.getResult();
  }

  @NotNull
  public static List<Type> typesToTypeList(PsiType[] types) {
    List<Type> result = new LinkedList<Type>();
    for (PsiType t : types) {
      result.add(typeToType(t));
    }
    return result;
  }

  @NotNull
  private static List<Import> importsToImportList(PsiImportStatementBase[] imports) {
    List<Import> result = new LinkedList<Import>();
    for (PsiImportStatementBase t : imports) {
      result.add(importToImport(t));
    }
    return result;
  }

  @NotNull
  private static Import importToImport(PsiImportStatementBase t) {
    if (t.getImportReference() != null)
      return new Import(t.getImportReference().getQualifiedName()); // TODO: use identifier
    return new Import("");
  }
}
