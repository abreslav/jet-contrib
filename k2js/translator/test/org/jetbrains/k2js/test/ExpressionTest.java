package org.jetbrains.k2js.test;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author Talanov Pavel
 *         <p/>
 *         This class tests basic language features and constructs such as constants, local variables, simple loops,
 *         conditional clauses etc.
 */
public final class ExpressionTest extends TranslationTest {

    private static final String MAIN = "expression/";

    protected String mainDirectory() {
        return MAIN;
    }

    @Override
    protected List<String> generateFilenameList(String inputFile) {
        return Arrays.asList(inputFile);
    }

    @Test
    public void currentTest() throws Exception {
        testFooBoxIsTrue("test.kt");
    }

    @Test
    public void testAssign() throws Exception {
        testFunctionOutput("assign.jet", "foo", "f", 2.0);
    }

    @Test
    public void namespaceProperties() throws Exception {
        testFunctionOutput("localProperty.jet", "foo", "box", 50);
    }

    @Test
    public void comparison() throws Exception {
        testFooBoxIsTrue("comparison.kt");
    }

    @Test
    public void ifElse() throws Exception {
        testFunctionOutput("if.kt", "foo", "box", 5);
    }
    //TODO: test fails due to isStatement issue, include when issue is solved or implement another solution
//    @Test
//    public void ifElseIf() throws Exception {
//        testFunctionOutput("elseif.kt", "foo", "box", 5);
//    }

    @Test
    public void whileSimpleTest() throws Exception {
        testFooBoxIsTrue("while.kt");
    }

    @Test
    public void doWhileSimpleTest() throws Exception {
        testFooBoxIsTrue("doWhile.kt");
    }

    @Test
    public void doWhileExecutesAtLeastOnce() throws Exception {
        testFooBoxIsTrue("doWhile2.kt");
    }

    @Test
    public void whileDoesntExecuteEvenOnceIfConditionIsFalse() throws Exception {
        testFooBoxIsTrue("while2.kt");
    }

    @Test
    public void stringConstant() throws Exception {
        testFooBoxIsTrue("stringConstant.kt");
    }

    @Test
    public void stringAssignment() throws Exception {
        testFooBoxIsTrue("stringAssignment.kt");
    }

    @Test
    public void functionUsedBeforeDeclaration() throws Exception {
        testFooBoxIsTrue("functionUsedBeforeDeclaration.kt");
    }

    @Test
    public void functionWithTwoParametersCall() throws Exception {
        testFooBoxIsTrue("functionWithTwoParametersCall.kt");
    }

    @Test
    public void prefixIntOperations() throws Exception {
        testFooBoxIsTrue("prefixIntOperations.kt");
    }

    @Test
    public void postfixIntOperations() throws Exception {
        testFooBoxIsTrue("postfixIntOperations.kt");
    }

    @Test
    public void notBoolean() throws Exception {
        testFooBoxIsTrue("notBoolean.kt");
    }

    @Test
    public void positiveAndNegativeNumbers() throws Exception {
        testFooBoxIsTrue("positiveAndNegativeNumbers.kt");
    }

    @Test
    public void ifElseAsExpression() throws Exception {
        testFooBoxIsTrue("ifElseAsExpression.kt");
    }

}