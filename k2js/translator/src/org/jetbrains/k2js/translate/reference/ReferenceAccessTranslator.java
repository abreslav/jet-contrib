package org.jetbrains.k2js.translate.reference;

import com.google.dart.compiler.backend.js.ast.JsExpression;
import com.google.dart.compiler.backend.js.ast.JsNameRef;
import com.google.dart.compiler.util.AstUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jet.lang.psi.JetSimpleNameExpression;
import org.jetbrains.k2js.translate.context.TranslationContext;

/**
 * @author Pavel Talanov
 */
public final class ReferenceAccessTranslator extends AccessTranslator {

    @NotNull
    /*package*/ static ReferenceAccessTranslator newInstance(@NotNull JetSimpleNameExpression expression,
                                                             @NotNull TranslationContext context) {
        return new ReferenceAccessTranslator(expression, context);
    }

    @NotNull
    private final JetSimpleNameExpression expression;

    private ReferenceAccessTranslator(@NotNull JetSimpleNameExpression expression,
                                      @NotNull TranslationContext context) {
        super(context);
        this.expression = expression;
    }

    @Override
    @NotNull
    public JsExpression translateAsGet() {
        //TODO: consider evaluating only once
        return ReferenceTranslator.translateSimpleName(expression, context());
    }

    @Override
    @NotNull
    public JsExpression translateAsSet(@NotNull JsExpression toSetTo) {
        //TODO: consider evaluating only once
        JsExpression reference = ReferenceTranslator.translateSimpleName(expression, context());
        assert reference instanceof JsNameRef;
        return AstUtil.newAssignment((JsNameRef) reference, toSetTo);
    }

}
