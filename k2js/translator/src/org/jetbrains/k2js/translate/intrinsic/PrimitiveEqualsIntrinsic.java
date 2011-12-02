package org.jetbrains.k2js.translate.intrinsic;

import com.google.dart.compiler.backend.js.ast.JsExpression;
import com.google.dart.compiler.util.AstUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.k2js.translate.general.TranslationContext;

import java.util.List;

/**
 * @author Talanov Pavel
 */
public final class PrimitiveEqualsIntrinsic extends EqualsIntrinsic {


    @NotNull
    /*package*/ static PrimitiveEqualsIntrinsic newInstance() {
        return new PrimitiveEqualsIntrinsic();
    }

    @NotNull
    public JsExpression apply(@NotNull JsExpression receiver, @NotNull List<JsExpression> arguments,
                              @NotNull TranslationContext context) {
        assert arguments.size() == 1 : "Equals operation should have one argument";
        if (isNegated()) {
            return AstUtil.notEqual(receiver, arguments.get(0));
        } else {
            return AstUtil.equals(receiver, arguments.get(0));
        }
    }

}
