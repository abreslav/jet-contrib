package org.jetbrains.k2js.test;

import org.junit.Test;

/**
 * @author Pavel Talanov
 */
public final class PropertyAccessTest extends TranslationTest {

    final private static String MAIN = "propertyAccess/";

    @Override
    protected String mainDirectory() {
        return MAIN;
    }

    @Test
    public void accessToInstanceProperty() throws Exception {
        testFooBoxIsTrue("accessToInstanceProperty.kt");
    }

    @Test
    public void twoClassesWithProperties() throws Exception {
        testFooBoxIsTrue("twoClassesWithProperties.kt");
    }

    @Test
    public void setter() throws Exception {
        testFunctionOutput("setter.kt", "foo", "f", 99.0);
    }

    @Test
    public void customGetter() throws Exception {
        testFooBoxIsTrue("customGetter.kt");
    }

    @Test
    public void customSetter() throws Exception {
        testFooBoxIsTrue("customSetter.kt");
    }

    @Test
    public void safeCall() throws Exception {
        testFooBoxIsTrue("safeCall.kt");
    }

    @Test
    public void safeCallReturnsNullIfFails() throws Exception {
        testFooBoxIsTrue("safeCallReturnsNullIfFails.kt");
    }

    @Test
    public void namespacePropertyInitializer() throws Exception {
        testFooBoxIsTrue("namespacePropertyInitializer.kt");
    }

    @Test
    public void namespacePropertySet() throws Exception {
        testFooBoxIsTrue("namespacePropertySet.kt");
    }

    //TODO test
    @Test
    public void namespaceCustomAccessors() throws Exception {
        testFooBoxIsTrue("namespaceCustomAccessors.kt");
    }


    @Test
    public void classUsesNamespaceProperties() throws Exception {
        testFooBoxIsTrue("classUsesNamespaceProperties.kt");
    }

}
