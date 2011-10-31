package org.jetbrains.jet.j2k.ast;

import org.jetbrains.annotations.NotNull;

/**
 * @author ignatov
 */
public class ParenthesizedExpression extends Expression {
  private final Expression myExpression;

  public ParenthesizedExpression(Expression expression) {
    myExpression = expression;
  }

  @NotNull
  @Override
  public String toKotlin() {
    return "(" + myExpression.toKotlin() + ")";
  }
}
