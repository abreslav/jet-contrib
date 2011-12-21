package org.jetbrains.jet.j2k.visitors;

import com.intellij.psi.*;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jet.j2k.ast.*;

import java.util.Collections;
import java.util.List;

import static org.jetbrains.jet.j2k.Converter.*;
import static org.jetbrains.jet.j2k.visitors.TypeVisitor.*;

/**
 * @author ignatov
 */
public class ExpressionVisitor extends StatementVisitor {
  @NotNull
  Expression myResult = Expression.EMPTY_EXPRESSION;

  @Override
  public void visitExpression(final PsiExpression expression) {
    myResult = Expression.EMPTY_EXPRESSION;
  }

  @NotNull
  @Override
  public Expression getResult() {
    return myResult;
  }

  @Override
  public void visitArrayAccessExpression(@NotNull PsiArrayAccessExpression expression) {
    super.visitArrayAccessExpression(expression);
    myResult = new ArrayAccessExpression(
      expressionToExpression(expression.getArrayExpression()),
      expressionToExpression(expression.getIndexExpression())
    );
  }

  @Override
  public void visitArrayInitializerExpression(@NotNull PsiArrayInitializerExpression expression) {
    super.visitArrayInitializerExpression(expression);
    myResult = new ArrayInitializerExpression(
      typeToType(expression.getType()),
      expressionsToExpressionList(expression.getInitializers())
    );
  }

  @Override
  public void visitAssignmentExpression(@NotNull PsiAssignmentExpression expression) {
    super.visitAssignmentExpression(expression);

    // TODO: simplify

    final IElementType tokenType = expression.getOperationSign().getTokenType();

    String secondOp = "";
    if (tokenType == JavaTokenType.GTGTEQ) secondOp = "shr";
    if (tokenType == JavaTokenType.LTLTEQ) secondOp = "shl";
    if (tokenType == JavaTokenType.XOREQ) secondOp = "xor";
    if (tokenType == JavaTokenType.ANDEQ) secondOp = "and";
    if (tokenType == JavaTokenType.OREQ) secondOp = "or";
    if (tokenType == JavaTokenType.GTGTGTEQ) secondOp = "ushr";

    if (!secondOp.isEmpty()) // if not Kotlin operators
      myResult = new AssignmentExpression(
        expressionToExpression(expression.getLExpression()),
        new BinaryExpression(
          expressionToExpression(expression.getLExpression()),
          expressionToExpression(expression.getRExpression()),
          secondOp
        ),
        "="
      );
    else
      myResult = new AssignmentExpression(
        expressionToExpression(expression.getLExpression()),
        expressionToExpression(expression.getRExpression()),
        expression.getOperationSign().getText() // TODO
      );
  }

  @NotNull
  private static String getOperatorString(@NotNull IElementType tokenType) {
    if (tokenType == JavaTokenType.PLUS) return "+";
    if (tokenType == JavaTokenType.MINUS) return "-";
    if (tokenType == JavaTokenType.ASTERISK) return "*";
    if (tokenType == JavaTokenType.DIV) return "/";
    if (tokenType == JavaTokenType.PERC) return "%";
    if (tokenType == JavaTokenType.GTGT) return "shr";
    if (tokenType == JavaTokenType.LTLT) return "shl";
    if (tokenType == JavaTokenType.XOR) return "xor";
    if (tokenType == JavaTokenType.AND) return "and";
    if (tokenType == JavaTokenType.OR) return "or";
    if (tokenType == JavaTokenType.GTGTGT) return "ushr";
    if (tokenType == JavaTokenType.GT) return ">";
    if (tokenType == JavaTokenType.LT) return "<";
    if (tokenType == JavaTokenType.GE) return ">=";
    if (tokenType == JavaTokenType.LE) return "<=";
    if (tokenType == JavaTokenType.EQEQ) return "==";
    if (tokenType == JavaTokenType.NE) return "!=";
    if (tokenType == JavaTokenType.ANDAND) return "&&";
    if (tokenType == JavaTokenType.OROR) return "||";
    if (tokenType == JavaTokenType.PLUSPLUS) return "++";
    if (tokenType == JavaTokenType.MINUSMINUS) return "--";
    if (tokenType == JavaTokenType.EXCL) return "!";

    System.out.println("UNSUPPORTED TOKEN TYPE: " + tokenType.toString());
    return "";
  }

  @Override
  public void visitBinaryExpression(@NotNull PsiBinaryExpression expression) {
    super.visitBinaryExpression(expression);

    if (expression.getOperationSign().getTokenType() == JavaTokenType.GTGTGT)
      myResult = new DummyMethodCallExpression(
        expressionToExpression(expression.getLOperand()),
        "ushr",
        expressionToExpression(expression.getROperand()));
    else
      myResult =
        new BinaryExpression(
          expressionToExpression(expression.getLOperand()),
          expressionToExpression(expression.getROperand()),
          getOperatorString(expression.getOperationSign().getTokenType())
        );
  }

  @Override
  public void visitClassObjectAccessExpression(@NotNull PsiClassObjectAccessExpression expression) {
    super.visitClassObjectAccessExpression(expression);
    myResult = new ClassObjectAccessExpression(elementToElement(expression.getOperand()));
  }

  @Override
  public void visitConditionalExpression(@NotNull PsiConditionalExpression expression) {
    super.visitConditionalExpression(expression);
    PsiExpression condition = expression.getCondition();
    PsiType type = condition.getType();
    Expression e = type != null ?
      createSureCallOnlyForChain(condition, type) :
      expressionToExpression(condition);
    myResult = new ParenthesizedExpression(
      new IfStatement(
        e,
        expressionToExpression(expression.getThenExpression()),
        expressionToExpression(expression.getElseExpression())
      )
    );
  }

  @Override
  public void visitExpressionList(@NotNull PsiExpressionList list) {
    super.visitExpressionList(list);
    myResult = new ExpressionList(expressionsToExpressionList(list.getExpressions()));
  }

  @Override
  public void visitInstanceOfExpression(@NotNull PsiInstanceOfExpression expression) {
    super.visitInstanceOfExpression(expression);
    myResult = new IsOperator(
      expressionToExpression(expression.getOperand()),
      elementToElement(expression.getCheckType()));
  }

  @Override
  public void visitLiteralExpression(@NotNull PsiLiteralExpression expression) {
    super.visitLiteralExpression(expression);

    final Object value = expression.getValue();
    String text = expression.getText();
    boolean isQuotingNeeded = true;

    final PsiType type = expression.getType();
    if (type != null) {
      String canonicalTypeStr = type.getCanonicalText();
      if (canonicalTypeStr.equals("double") || canonicalTypeStr.equals(JAVA_LANG_DOUBLE))
        text = text.replace("D", "").replace("d", "");
      if (canonicalTypeStr.equals("float") || canonicalTypeStr.equals(JAVA_LANG_FLOAT))
        text = text.replace("F", "").replace("f", "") + "." + "flt";
      if (canonicalTypeStr.equals("long") || canonicalTypeStr.equals(JAVA_LANG_LONG))
        text = text.replace("L", "").replace("l", "");
      if (canonicalTypeStr.equals("int") || canonicalTypeStr.equals(JAVA_LANG_INTEGER)) // need for hex support
        text = value != null ? value.toString() : text;

      if (canonicalTypeStr.equals(JAVA_LANG_STRING))
        isQuotingNeeded = false;
      if (canonicalTypeStr.equals("char") || canonicalTypeStr.equals(JAVA_LANG_CHARACTER))
        isQuotingNeeded = false;
    }
    myResult = new LiteralExpression(new IdentifierImpl(text, false, isQuotingNeeded));
  }

  @Override
  public void visitMethodCallExpression(@NotNull PsiMethodCallExpression expression) {
    super.visitMethodCallExpression(expression);
    if (!SuperVisitor.isSuper(expression.getMethodExpression()) || !isInsidePrimaryConstructor(expression)) {
      myResult = // TODO: not resolved
        new MethodCallExpression(
          expressionToExpression(expression.getMethodExpression()),
          expressionsToExpressionList(expression.getArgumentList().getExpressions()),
          createConversions(expression),
          typeToType(expression.getType()).isNullable(),
          typesToTypeList(expression.getTypeArguments())
        );
    }
  }

  @Override
  public void visitCallExpression(PsiCallExpression callExpression) {
    super.visitCallExpression(callExpression);
  }

  @Override
  public void visitNewExpression(@NotNull PsiNewExpression expression) {
    super.visitNewExpression(expression);
    if (expression.getArrayInitializer() != null) // new Foo[] {Foo(1), Foo(2)}
      myResult = createNewEmptyArray(expression);
    else if (expression.getArrayDimensions().length > 0) { // new Foo[5]
      myResult = createNewEmptyArrayWithoutInitialization(expression);
    } else { // new Class(): common case
      myResult = createNewClassExpression(expression);
    }
  }

  @NotNull
  private static Expression createNewClassExpression(@NotNull PsiNewExpression expression) {
    final PsiAnonymousClass anonymousClass = expression.getAnonymousClass();
    final PsiMethod constructor = expression.resolveMethod();
    PsiJavaCodeReferenceElement classReference = expression.getClassOrAnonymousClassReference();
    final boolean isNotConvertedClass = classReference != null && !getClassIdentifiers().contains(classReference.getQualifiedName());
    PsiExpressionList argumentList = expression.getArgumentList();
    PsiExpression[] arguments = argumentList != null ? argumentList.getExpressions() : new PsiExpression[]{};
    if (constructor == null || isConstructorPrimary(constructor) || isNotConvertedClass) {
      return new NewClassExpression(
        expressionToExpression(expression.getQualifier()),
        elementToElement(classReference),
        expressionsToExpressionList(arguments),
        createConversions(expression),
        anonymousClass != null ? anonymousClassToAnonymousClass(anonymousClass) : null
      );
    }
    // is constructor secondary
    final PsiJavaCodeReferenceElement reference = expression.getClassReference();
    final List<Type> typeParameters = reference != null ? typesToTypeList(reference.getTypeParameters()) : Collections.<Type>emptyList();
    return new CallChainExpression(
      new IdentifierImpl(constructor.getName(), false),
      new MethodCallExpression(
        new IdentifierImpl("init"),
        expressionsToExpressionList(arguments),
        typeParameters));
  }

  @NotNull
  private static Expression createNewEmptyArrayWithoutInitialization(@NotNull PsiNewExpression expression) {
    return new ArrayWithoutInitializationExpression(
      typeToType(expression.getType(), true),
      expressionsToExpressionList(expression.getArrayDimensions())
    );
  }

  @NotNull
  private static Expression createNewEmptyArray(@NotNull PsiNewExpression expression) {
    return expressionToExpression(expression.getArrayInitializer());
  }

  @Override
  public void visitParenthesizedExpression(@NotNull PsiParenthesizedExpression expression) {
    super.visitParenthesizedExpression(expression);
    myResult = new ParenthesizedExpression(
      expressionToExpression(expression.getExpression())
    );
  }

  @Override
  public void visitPostfixExpression(@NotNull PsiPostfixExpression expression) {
    super.visitPostfixExpression(expression);
    myResult = new PostfixOperator(
      getOperatorString(expression.getOperationSign().getTokenType()),
      expressionToExpression(expression.getOperand())
    );
  }

  @Override
  public void visitPrefixExpression(@NotNull PsiPrefixExpression expression) {
    super.visitPrefixExpression(expression);
    if (expression.getOperationTokenType() == JavaTokenType.TILDE)
      myResult = new DummyMethodCallExpression(
        new ParenthesizedExpression(expressionToExpression(expression.getOperand())), "inv", Expression.EMPTY_EXPRESSION
      );
    else
      myResult = new PrefixOperator(
        getOperatorString(expression.getOperationSign().getTokenType()),
        expressionToExpression(expression.getOperand())
      );
  }

  @Override
  public void visitReferenceExpression(@NotNull PsiReferenceExpression expression) {
    super.visitReferenceExpression(expression);

    final boolean isFieldReference = isFieldReference(expression, getContainingClass(expression));
    final boolean insideSecondaryConstructor = isInsideSecondaryConstructor(expression);
    final boolean hasReceiver = isFieldReference && insideSecondaryConstructor;
    final boolean isThis = isThisExpression(expression);
    final boolean isNullable = typeToType(expression.getType()).isNullable();
    final String className = getClassNameWithConstructor(expression);

    Expression identifier = new IdentifierImpl(expression.getReferenceName(), isNullable);

    final String temporaryObject = "__";
    if (hasReceiver)
      identifier = new CallChainExpression(new IdentifierImpl(temporaryObject, false), new IdentifierImpl(expression.getReferenceName(), isNullable));
    else if (insideSecondaryConstructor && isThis)
      identifier = new IdentifierImpl("val " + temporaryObject + " = " + className); // TODO: hack

    myResult = new CallChainExpression(
      expressionToExpression(expression.getQualifierExpression()),
      identifier // TODO: if type exists so identifier is nullable
    );
  }

  @NotNull
  private static String getClassNameWithConstructor(@NotNull PsiReferenceExpression expression) {
    PsiElement context = expression.getContext();
    while (context != null) {
      if (context instanceof PsiMethod && ((PsiMethod) context).isConstructor()) {
        final PsiClass containingClass = ((PsiMethod) context).getContainingClass();
        if (containingClass != null) {
          final PsiIdentifier identifier = containingClass.getNameIdentifier();
          if (identifier != null)
            return identifier.getText();
        }
      }
      context = context.getContext();
    }
    return "";
  }

  @NotNull
  static String getClassName(@NotNull PsiExpression expression) {
    PsiElement context = expression.getContext();
    while (context != null) {
      if (context instanceof PsiClass) {
        final PsiClass containingClass = (PsiClass) context;
        final PsiIdentifier identifier = containingClass.getNameIdentifier();
        if (identifier != null)
          return identifier.getText();
      }
      context = context.getContext();
    }
    return "";
  }

  private static boolean isFieldReference(@NotNull PsiReferenceExpression expression, PsiClass currentClass) {
    final PsiReference reference = expression.getReference();
    if (reference != null) {
      final PsiElement resolvedReference = reference.resolve();
      if (resolvedReference != null) {
        if (resolvedReference instanceof PsiField) {
          return ((PsiField) resolvedReference).getContainingClass() == currentClass;
        }
      }
    }
    return false;
  }

  private static boolean isInsideSecondaryConstructor(@NotNull PsiReferenceExpression expression) {
    PsiElement context = expression.getContext();
    while (context != null) {
      if (context instanceof PsiMethod && ((PsiMethod) context).isConstructor())
        return !isConstructorPrimary((PsiMethod) context);
      context = context.getContext();
    }
    return false;
  }

  private static boolean isInsidePrimaryConstructor(@NotNull PsiExpression expression) {
    PsiElement context = expression.getContext();
    while (context != null) {
      if (context instanceof PsiMethod && ((PsiMethod) context).isConstructor())
        return isConstructorPrimary((PsiMethod) context);
      context = context.getContext();
    }
    return false;
  }

  @Nullable
  private static PsiClass getContainingClass(@NotNull PsiExpression expression) {
    PsiElement context = expression.getContext();
    while (context != null) {
      if (context instanceof PsiMethod && ((PsiMethod) context).isConstructor())
        return ((PsiMethod) context).getContainingClass();
      context = context.getContext();
    }
    return null;
  }

  private static boolean isThisExpression(@NotNull PsiReferenceExpression expression) {
    for (PsiReference r : expression.getReferences())
      if (r.getCanonicalText().equals("this")) {
        final PsiElement res = r.resolve();
        if (res != null && res instanceof PsiMethod && ((PsiMethod) res).isConstructor())
          return true;
      }
    return false;
  }

  @Override
  public void visitSuperExpression(@NotNull PsiSuperExpression expression) {
    super.visitSuperExpression(expression);
    final PsiJavaCodeReferenceElement qualifier = expression.getQualifier();
    myResult = new SuperExpression(
      qualifier != null ?
        new IdentifierImpl(qualifier.getQualifiedName()) :
        Identifier.EMPTY_IDENTIFIER
    );
  }

  @Override
  public void visitThisExpression(@NotNull PsiThisExpression expression) {
    super.visitThisExpression(expression);
    final PsiJavaCodeReferenceElement qualifier = expression.getQualifier();
    myResult = new ThisExpression(
      qualifier != null ?
        new IdentifierImpl(qualifier.getQualifiedName()) :
        Identifier.EMPTY_IDENTIFIER
    );
  }

  @Override
  public void visitTypeCastExpression(@NotNull PsiTypeCastExpression expression) {
    super.visitTypeCastExpression(expression);

    final PsiTypeElement castType = expression.getCastType();
    if (castType != null) {
      myResult = new TypeCastExpression(
        typeToType(castType.getType()),
        expressionToExpression(expression.getOperand())
      );
    }
  }

  @Override
  public void visitPolyadicExpression(@NotNull PsiPolyadicExpression expression) {
    super.visitPolyadicExpression(expression);
    myResult = new PolyadicExpression(
      expressionsToExpressionList(expression.getOperands()),
      getOperatorString(expression.getOperationTokenType())
    );
  }
}