package org.jetbrains.jet.j2k.ast;

import junit.framework.Assert;
import org.jetbrains.jet.j2k.JetTestCaseBase;

/**
 * @author ignatov
 */
public class EnumTest extends JetTestCaseBase {
  public void testEmptyEnum() throws Exception {
    Assert.assertEquals(
      classToSingleLineKotlin("enum A {}"),
      "enum A { }"
    );
  }

  public void testTypeSafeEnum() throws Exception {
    Assert.assertEquals(
      classToKotlin("enum Coin { PENNY, NICKEL, DIME, QUARTER; }"),
      "enum Coin {\n" +
        "PENNY\n" +
        "NICKEL\n" +
        "DIME\n" +
        "QUARTER\n" +
        "}"
    );
  }

  public void testOverrideToString() throws Exception {
    Assert.assertEquals(
      classToKotlin(
        "enum Color {" +
          " WHITE, BLACK, RED, YELLOW, BLUE;" +
          "@Override String toString() {" +
          "  return \"COLOR\";" +
          "}" +
          "}"),
      "enum Color {\n" +
        "WHITE\n" +
        "BLACK\n" +
        "RED\n" +
        "YELLOW\n" +
        "BLUE\n" +
        "override fun toString() : String? {\n" +
        "return \"COLOR\"\n" +
        "}\n" +
        "}"
    );
  }

  public void testFieldsWithPrimaryPrivateConstructor() throws Exception {
    Assert.assertEquals(
      classToKotlin(
        "enum Color {\n" +
          " WHITE(21), BLACK(22), RED(23), YELLOW(24), BLUE(25);\n" +
          "\n" +
          " private int code;\n" +
          "\n" +
          " private Color(int c) {\n" +
          "   code = c;\n" +
          " }\n" +
          "\n" +
          " public int getCode() {\n" +
          "   return code;\n" +
          " }"),
      "enum Color {\n" +
        "WHITE(21)\n" +
        "BLACK(22)\n" +
        "RED(23)\n" +
        "YELLOW(24)\n" +
        "BLUE(25)\n" +
        "private var code : Int\n" +
        "private (c : Int) {\n" +
        "code = c\n" +
        "}\n" +
        "public fun getCode() : Int {\n" +
        "return code\n" +
        "}\n" +
        "}"
    );
  }

  public void testEnumImplementsOneInterface() throws Exception {
    Assert.assertEquals(
      classToSingleLineKotlin("enum A implements I {}"),
      "enum A : I { }"
    );
  }

  public void testEnumWithNameField() throws Exception {
    Assert.assertEquals(
      classToKotlin("enum E { I; private String name; }"),
      "enum E {\nI\nprivate var name : String?\n}"
    );
  }

  public void testEnumImplementsSeveralInterfaces() throws Exception {
    Assert.assertEquals(
      classToSingleLineKotlin("enum A implements I0, I1, I2 {}"),
      "enum A : I0, I1, I2 { }"
    );
  }

  public void testPublicEnum() throws Exception {
    Assert.assertEquals(classToSingleLineKotlin("public enum Test {}"), "public enum Test { }");
  }

  public void testProtectedEnum() throws Exception {
    Assert.assertEquals(classToSingleLineKotlin("protected enum Test {}"), "protected enum Test { }");
  }

  public void testPrivateEnum() throws Exception {
    Assert.assertEquals(classToSingleLineKotlin("private enum Test {}"), "private enum Test { }");
  }

  public void testInternalEnum() throws Exception {
    Assert.assertEquals(classToSingleLineKotlin("enum Test {}"), "enum Test { }");
  }

//  public void testTwoConstructors() throws Exception {
//    Assert.assertEquals(
//      classToKotlin(
//        "enum MultEnum {\n" +
//          "    GREMLIN(\"UTILITY\"),\n" +
//          "    MORT(30);\n" +
//          "  \n" +
//          "    MultEnum(String s) {\n" +
//          "    }\n" +
//          "  \n" +
//          "    MultEnum(int dmg) {\n" +
//          "    }"),
//      "" // TODO: will fail
//    );
//  }

  public void testRunnableImplementation() throws Exception {
    Assert.assertEquals(
      classToKotlin(
        "enum Color implements Runnable {\n" +
          " WHITE, BLACK, RED, YELLOW, BLUE;\n" +
          "\n" +
          " public void run() {\n" +
          "   System.out.println(\"name()=\" + name() +\n" +
          "       \", toString()=\" + toString());\n" +
          " }\n" +
          "}"),
      "enum Color : Runnable {\n" +
        "WHITE\n" +
        "BLACK\n" +
        "RED\n" +
        "YELLOW\n" +
        "BLUE\n" +
        "override public fun run() : Unit {\n" +
        "System.out.println((\"name()=\" + name() + \", toString()=\" + toString()))\n" +
        "}\n" +
        "}"
    );
  }
}
