package org.jetbrains.k2js.translate;

import com.google.dart.compiler.backend.js.ast.JsName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jet.lang.psi.JetElement;
import org.jetbrains.jet.lang.psi.JetProperty;
import org.jetbrains.jet.lang.psi.JetVisitor;

/**
 * @author Talanov Pavel
 *
 * This class is a base class for all visitors. It contains code that is shared among them.
 */
public class TranslatorVisitor<T> extends JetVisitor<T, TranslationContext> {

    @Override
    @NotNull
    public T visitJetElement(JetElement expression, TranslationContext context) {
        throw new RuntimeException("Unsupported expression encountered:" + expression.toString());
    }

    @NotNull
    protected String getPropertyName(@NotNull JetProperty expression) {
        String propertyName = expression.getName();
        if (propertyName == null) {
            throw new AssertionError("Property with no name encountered!");
        }
        return propertyName;
    }

    @NotNull
    protected JsName getBackingFieldName(@NotNull String propertyName, @NotNull TranslationContext context) {
        String backingFieldName = Namer.getKotlinBackingFieldName(propertyName);
        return context.enclosingScope().findExistingName(backingFieldName);
    }
}
