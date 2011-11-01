package org.jetbrains.jet.j2k.ast;

import junit.framework.Assert;
import org.jetbrains.jet.j2k.JetTestCaseBase;

/**
 * @author ignatov
 */
public class FunctionTest extends JetTestCaseBase {
  public void testEmptyVoidMethod() throws Exception {
    Assert.assertEquals(
      methodToSingleLineKotlin("void main() {}"),
      "fun main() : Unit { }"
    );
  }

  public void testMethodClassType() throws Exception {
    Assert.assertEquals(
      methodToSingleLineKotlin("String main() {}"),
      "fun main() : String? { }"
    );
  }

  public void testMethodPrimitiveType() throws Exception {
    Assert.assertEquals(
      methodToSingleLineKotlin("int main() {}"),
      "fun main() : Int { }"
    );
  }

  public void testMethodPrimitiveType2() throws Exception {
    Assert.assertEquals(
      methodToSingleLineKotlin("boolean main() {}"),
      "fun main() : Boolean { }"
    );
  }

  public void testMethodWithReturnStatement() throws Exception {
    Assert.assertEquals(
      methodToSingleLineKotlin("boolean isTrue() { return true; }"),
      "fun isTrue() : Boolean { return true }"
    );
  }

  public void testClassGenericParam() throws Exception {
    Assert.assertEquals(
      methodToSingleLineKotlin("T getT() {}"),
      "fun getT() : T? { }"
    );
  }

  public void testOwnGenericParam() throws Exception {
    Assert.assertEquals(
      methodToSingleLineKotlin("<U> void putU(U u) {}"),
      "fun putU<U>(u : U?) : Unit { }"
    );
  }

  public void testOwnSeveralGenericParams() throws Exception {
    Assert.assertEquals(
      methodToSingleLineKotlin("<U, V, W> void putUVW(U u, V v, W w) {}"),
      "fun putUVW<U, V, W>(u : U?, v : V?, w : W?) : Unit { }"
    );
  }
}