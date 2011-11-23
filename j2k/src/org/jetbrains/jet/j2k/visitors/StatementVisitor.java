package org.jetbrains.jet.j2k.visitors;

import com.intellij.psi.*;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jet.j2k.ast.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.jetbrains.jet.j2k.Converter.*;

/**
 * @author ignatov
 */
public class StatementVisitor extends ElementVisitor {
  private Statement myResult = Statement.EMPTY_STATEMENT;

  @NotNull
  @Override
  public Statement getResult() {
    return myResult;
  }

  @Override
  public void visitAssertStatement(PsiAssertStatement statement) {
    super.visitAssertStatement(statement);
    myResult = new AssertStatement(
      expressionToExpression(statement.getAssertCondition()),
      expressionToExpression(statement.getAssertDescription())
    );
  }

  @Override
  public void visitBlockStatement(PsiBlockStatement statement) {
    super.visitBlockStatement(statement);
    myResult = new Block(
      statementsToStatementList(statement.getCodeBlock().getStatements()),
      true
    );
  }

  @Override
  public void visitBreakStatement(PsiBreakStatement statement) {
    super.visitBreakStatement(statement);
    if (statement.getLabelIdentifier() == null)
      myResult = new BreakStatement();
    else
      myResult = new BreakStatement(
        identifierToIdentifier(statement.getLabelIdentifier())
      );
  }

  @Override
  public void visitContinueStatement(PsiContinueStatement statement) {
    super.visitContinueStatement(statement);
    if (statement.getLabelIdentifier() == null)
      myResult = new ContinueStatement();
    else
      myResult = new ContinueStatement(
        identifierToIdentifier(statement.getLabelIdentifier())
      );
  }

  @Override
  public void visitDeclarationStatement(PsiDeclarationStatement statement) {
    super.visitDeclarationStatement(statement);
    myResult = new DeclarationStatement(
      elementsToElementList(statement.getDeclaredElements())
    );
  }

  @Override
  public void visitDoWhileStatement(PsiDoWhileStatement statement) {
    super.visitDoWhileStatement(statement);
    myResult = new DoWhileStatement(
      expressionToExpression(statement.getCondition()),
      statementToStatement(statement.getBody())
    );
  }

  @Override
  public void visitExpressionStatement(PsiExpressionStatement statement) {
    super.visitExpressionStatement(statement);
    myResult = expressionToExpression(statement.getExpression());
  }

  @Override
  public void visitExpressionListStatement(PsiExpressionListStatement statement) {
    super.visitExpressionListStatement(statement);
    myResult =
      new ExpressionListStatement(expressionsToExpressionList(statement.getExpressionList().getExpressions()));
  }

  @Override
  public void visitForStatement(@NotNull PsiForStatement statement) {
    super.visitForStatement(statement);

    final PsiStatement initialization = statement.getInitialization();
    final PsiStatement update = statement.getUpdate();
    final PsiExpression condition = statement.getCondition();
    final PsiLocalVariable firstChild = initialization != null && initialization.getFirstChild() instanceof PsiLocalVariable ?
      (PsiLocalVariable) initialization.getFirstChild() : null;

    final IElementType operationTokenType = condition != null && condition instanceof PsiBinaryExpression ?
      ((PsiBinaryExpression) condition).getOperationTokenType() : null;
    if (
      initialization != null &&
        initialization instanceof PsiDeclarationStatement
        && initialization.getFirstChild() == initialization.getLastChild()
        && condition != null
        && update != null
        && update.getChildren().length == 1
        && update.getChildren()[0] instanceof PsiPostfixExpression
        && ((PsiPostfixExpression) update.getChildren()[0]).getOperationTokenType() == JavaTokenType.PLUSPLUS
        && operationTokenType != null
        && (operationTokenType == JavaTokenType.LT || operationTokenType == JavaTokenType.LE)
        && initialization.getFirstChild() != null
        && initialization.getFirstChild() instanceof PsiLocalVariable
        && firstChild != null
        && firstChild.getNameIdentifier() != null
        && isOnceWritableIterator(firstChild)
      ) {
      final Expression end = expressionToExpression(((PsiBinaryExpression) condition).getROperand());
      final Expression endExpression = operationTokenType == JavaTokenType.LT ?
        new BinaryExpression(end, new IdentifierImpl("1"), "-"):
        end;
      myResult = new ForeachWithRangeStatement(
        new IdentifierImpl(firstChild.getName()),
        expressionToExpression(firstChild.getInitializer()),
        endExpression,
        statementToStatement(statement.getBody())
      );
    } else { // common case: while loop instead of for loop
      List<Statement> forStatements = new LinkedList<Statement>();
      forStatements.add(statementToStatement(initialization));
      forStatements.add(new WhileStatement(
        expressionToExpression(condition),
        new Block(
          Arrays.asList(statementToStatement(statement.getBody()),
            new Block(Arrays.asList(statementToStatement(update)))))));
      myResult = new Block(forStatements);
    }
  }

  private static boolean isOnceWritableIterator(PsiLocalVariable firstChild) {
    int counter = 0;
    if (firstChild != null)
      for (PsiReference r : (ReferencesSearch.search(firstChild))) {
        if (r instanceof PsiExpression) {
          if (PsiUtil.isAccessedForWriting((PsiExpression) r))
            counter++;
        }
      }
    return counter == 1; // only increment usage
  }

  @Override
  public void visitForeachStatement(PsiForeachStatement statement) {
    super.visitForeachStatement(statement);
    myResult = new ForeachStatement(
      parameterToParameter(statement.getIterationParameter()),
      expressionToExpression(statement.getIteratedValue()),
      statementToStatement(statement.getBody())
    );
  }

  @Override
  public void visitIfStatement(PsiIfStatement statement) {
    super.visitIfStatement(statement);
    myResult = new IfStatement(
      expressionToExpression(statement.getCondition()),
      statementToStatement(statement.getThenBranch()),
      statementToStatement(statement.getElseBranch())
    );
  }

  @Override
  public void visitLabeledStatement(PsiLabeledStatement statement) {
    super.visitLabeledStatement(statement);
    myResult = new LabelStatement(
      identifierToIdentifier(statement.getLabelIdentifier()),
      statementToStatement(statement.getStatement())
    );
  }

  @Override
  public void visitSwitchLabelStatement(PsiSwitchLabelStatement statement) {
    super.visitSwitchLabelStatement(statement);
  }

  @Override
  public void visitSwitchStatement(PsiSwitchStatement statement) {
    super.visitSwitchStatement(statement);
  }

  @Override
  public void visitSynchronizedStatement(PsiSynchronizedStatement statement) {
    super.visitSynchronizedStatement(statement);
    myResult = new SynchronizedStatement(
      expressionToExpression(statement.getLockExpression()),
      blockToBlock(statement.getBody())
    );
  }

  @Override
  public void visitThrowStatement(PsiThrowStatement statement) {
    super.visitThrowStatement(statement);
    myResult = new ThrowStatement(
      expressionToExpression(statement.getException())
    );
  }

  @Override
  public void visitTryStatement(PsiTryStatement statement) {
    super.visitTryStatement(statement);

    List<CatchStatement> catches = new LinkedList<CatchStatement>();
    for (int i = 0; i < statement.getCatchBlocks().length; i++) {
      catches.add(new CatchStatement(
        parameterToParameter(statement.getCatchBlockParameters()[i]),
        blockToBlock(statement.getCatchBlocks()[i], true)
      ));
    }

    myResult = new TryStatement(
      blockToBlock(statement.getTryBlock(), true),
      catches,
      blockToBlock(statement.getFinallyBlock(), true)
    );
  }

  @Override
  public void visitWhileStatement(PsiWhileStatement statement) {
    super.visitWhileStatement(statement);
    myResult = new WhileStatement(
      expressionToExpression(statement.getCondition()),
      statementToStatement(statement.getBody())
    );
  }

  @Override
  public void visitReturnStatement(PsiReturnStatement statement) {
    super.visitReturnStatement(statement);
    myResult = new ReturnStatement(
      expressionToExpression(statement.getReturnValue())
    );
  }
}
