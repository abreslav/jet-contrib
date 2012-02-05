package org.jetbrains.k2js.test;

/**
 * @author Pavel Talanov
 */
public final class NameClashesTest extends TranslationTest {

    private static final String MAIN = "nameClashes/";

    @Override
    protected String mainDirectory() {
        return MAIN;
    }

    public void testMethodOverload() throws Exception {
        testFooBoxIsTrue("methodOverload.kt");
    }
}