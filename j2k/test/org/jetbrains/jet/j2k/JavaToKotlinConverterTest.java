package org.jetbrains.jet.j2k;

import com.intellij.codeInsight.daemon.LightDaemonAnalyzerTestCase;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.impl.JavaSdkImpl;
import com.intellij.psi.PsiJavaFile;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

import static org.apache.commons.io.FileUtils.readFileToString;
import static org.apache.commons.io.FileUtils.writeStringToFile;
import static org.jetbrains.jet.j2k.TestCaseBuilder.getTestDataPathBase;

/**
 * @author ignatov
 */
public class JavaToKotlinConverterTest extends LightDaemonAnalyzerTestCase {
  private String myDataPath;
  private String myName;

  public JavaToKotlinConverterTest(String dataPath, String name) {
    myDataPath = dataPath;
    myName = name;
  }

  @Override
  protected void runTest() throws Throwable {
    String javaPath = getTestDataPath() + File.separator + getTestFilePath();
    String kotlinPath = javaPath.replace(".jav", ".kt");

    final File kotlinFile = new File(kotlinPath);
    if (!kotlinFile.exists())
      writeStringToFile(kotlinFile, "");
    final String expected = readFileToString(kotlinFile);
    final File javaFile = new File(javaPath);
    final String javaCode = readFileToString(javaFile);

    String actual = "";
    if (javaFile.getParent().endsWith("/expression")) actual = expressionToKotlin(javaCode);
    else if (javaFile.getParent().endsWith("/statement")) actual = statementToKotlin(javaCode);
    else if (javaFile.getParent().endsWith("/method")) actual = methodToKotlin(javaCode);
    else if (javaFile.getParent().endsWith("/class")) actual = classToKotlin(javaCode);
    else if (javaFile.getParent().endsWith("/file")) actual = fileToKotlin(javaCode);

    assert !actual.equals("");
    if (!expected.equals(actual))
      writeStringToFile(new File(kotlinPath + ".tmp"), actual);
    Assert.assertEquals(expected, actual);
  }

  @NotNull
  protected String getTestFilePath() {
    return myDataPath + File.separator + myName + ".jav";
  }

  @Override
  protected String getTestDataPath() {
    return "testData";
  }

  private static Sdk jdkFromIdeaHome() {
    return new JavaSdkImpl().createJdk("JDK", "jre", true);
  }

  protected Sdk getProjectJDK() {
    return jdkFromIdeaHome();
  }

  @Override
  public String getName() {
    return "test_" + myName;
  }

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTest(TestCaseBuilder.suiteForDirectory(getTestDataPathBase(), "/ast", true, new TestCaseBuilder.NamedTestFactory() {
      @NotNull
      @Override
      public Test createTest(@NotNull String dataPath, @NotNull String name) {
        return new JavaToKotlinConverterTest(dataPath, name);
      }
    }));
    return suite;
  }

  void configureFromText(String text) throws IOException {
    configureFromFileText("test.java", text);
  }

  protected String fileToKotlin(String text) throws IOException {
    configureFromText(text);
    return prettify(Converter.fileToFile((PsiJavaFile) myFile).toKotlin());
  }

  @NotNull
  protected String classToKotlin(String text) throws IOException {
    configureFromText(text);

    PsiJavaFile javaFile = (PsiJavaFile) myFile;

    String result = prettify(Converter.fileToFile(javaFile).toKotlin()).replaceAll("namespace \\{", "");
    result = result.substring(0, result.lastIndexOf("}"));
    return prettify(result);
  }

  @NotNull
  protected String methodToKotlin(String text) throws IOException {
    String result = classToKotlin("final class C {" + text + "}")
      .replaceAll("class C \\{", "");
    result = result.substring(0, result.lastIndexOf("}"));
    return prettify(result);
  }

  @NotNull
  protected String statementToKotlin(String text) throws Exception {
    String result = methodToKotlin("void main() {" + text + "}");
    int pos = result.lastIndexOf("}");
    result = result.substring(0, pos).replaceFirst("fun main\\(\\) : Unit \\{", "");
    return prettify(result);
  }

  @NotNull
  protected String expressionToKotlin(String code) throws Exception {
    String result = statementToKotlin("Object o =" + code + "}");
    result = result.replaceFirst("var o : Any\\? =", "");
    return prettify(result);
  }

  @NotNull
  private static String prettify(String code) {
    if (code == null)
      return "";
    return code
      .trim()
      .replaceAll("\r\n", "\n")
      .replaceAll("\n+", "\n")
      .replaceAll("\n ", "\n")
      .replaceAll(" +", " ")
      .trim()
      ;
  }
}