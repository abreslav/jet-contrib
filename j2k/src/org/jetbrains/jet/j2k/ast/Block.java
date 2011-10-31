package org.jetbrains.jet.j2k.ast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.jet.j2k.util.AstUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * @author ignatov
 */
public class Block extends Statement {
  public final static Block EMPTY_BLOCK = new Block();
  private List<Statement> myStatements = new LinkedList<Statement>();
  private boolean myNotEmpty = false;

  private Block() {
  }

  public Block(List<Statement> statements) {
    myStatements = statements;
  }

  public Block(List<Statement> statements, boolean notEmpty) {
    myStatements = statements;
    myNotEmpty = notEmpty;
  }

  public boolean isEmpty() {
    return !myNotEmpty && myStatements.size() == 0;
  }

  @NotNull
  @Override
  public String toKotlin() {
    if (!isEmpty())
      return "{" + N +
        AstUtil.joinNodes(myStatements, N) + N +
        "}";
    return EMPTY;
  }
}
