package org.jetbrains.k2js.translate;

import com.google.dart.compiler.backend.js.ast.JsExpression;
import com.google.dart.compiler.backend.js.ast.JsInvocation;
import com.google.dart.compiler.backend.js.ast.JsName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jet.lang.descriptors.DeclarationDescriptor;
import org.jetbrains.jet.lang.psi.JetSimpleNameExpression;

/**
 * @author Talanov Pavel
 */
public class ReferenceTranslator extends AbstractTranslator {

    @NotNull
    static public ReferenceTranslator newInstance(@NotNull TranslationContext context) {
        return new ReferenceTranslator(context);
    }

    private ReferenceTranslator(@NotNull TranslationContext context) {
        super(context);
    }

    @NotNull
    JsExpression translateSimpleName(@NotNull JetSimpleNameExpression expression) {
        //TODO: this is only a hack for now
        // Problem is that namespace properties do not generate getters and setter actually so they must be referenced
        // by name
        JsExpression result;
        result = resolveAsGlobalReference(expression);
        if (result != null) {
            return result;
        }
        result = resolveAsLocalReference(expression);
        if (result != null) {
            return result;
        }
        JsInvocation getterCall =
                Translation.propertyAccessTranslator(translationContext()).resolveAsPropertyGet(expression);
        if (getterCall != null) {
            return getterCall;
        }
        throw new AssertionError("Undefined name in this scope: " + expression.getReferencedName());

    }

    @Nullable
    private JsExpression resolveAsGlobalReference(@NotNull JetSimpleNameExpression expression) {
        DeclarationDescriptor referencedDescriptor =
                BindingUtils.getDescriptorForReferenceExpression(translationContext().bindingContext(), expression);
        if (referencedDescriptor == null) {
            return null;
        }
        if (!translationContext().isDeclared(referencedDescriptor)) {
            return null;
        }
        JsName referencedName = translationContext().getNameForDescriptor(referencedDescriptor);
        return TranslationUtils.generateCorrectReference(translationContext(), expression, referencedName);
    }

    @Nullable
    private JsExpression resolveAsLocalReference(@NotNull JetSimpleNameExpression expression) {
        JsName localReferencedName = TranslationUtils.getLocalReferencedName(translationContext(), expression);
        if (localReferencedName == null) {
            return null;
        }
        return TranslationUtils.generateCorrectReference(translationContext(), expression, localReferencedName);
    }


}
