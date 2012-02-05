package org.jetbrains.k2js.translate.intrinsic;

import com.google.dart.compiler.backend.js.ast.JsExpression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.k2js.translate.context.TranslationContext;

import java.util.List;

/**
 * @author Pavel Talanov
 *         <p/>
 *         Base class for all intrinsics.
 */
public interface Intrinsic {

    @NotNull
    JsExpression apply(@Nullable JsExpression receiver, @NotNull List<JsExpression> arguments,
                       @NotNull TranslationContext context);
}
