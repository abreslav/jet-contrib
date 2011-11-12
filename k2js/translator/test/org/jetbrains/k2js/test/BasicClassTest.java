package org.jetbrains.k2js.test;

import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/**
 * @author Talanov Pavel
 */
public class BasicClassTest extends AbstractClassTest {

    final private static String MAIN = "class/";

    @Override
    protected String mainDirectory() {
        return MAIN;
    }

    @Test
    public void kotlinJsLibRunsWithRhino() throws Exception {
        Context context = Context.enter();
        Scriptable scope = context.initStandardObjects();
        runFileWithRhino(kotlinLibrartyPath(), context, scope);
        Context.exit();
    }

    @Test
    public void classInstantiation() throws Exception {
        testFooBoxIsTrue("classInstantiation.kt");
    }

    @Test
    public void methodDeclarationAndCall() throws Exception {
        testFooBoxIsTrue("methodDeclarationAndCall.kt");
    }

    // TODO : excluded test
    @Test
    public void constructorWithParameter() throws Exception {
        //testFooBoxIsTrue("constructorWithParameter.kt");
    }
}
