package org.jetbrains.k2js.translate.intrinsic.string;


import com.google.dart.compiler.backend.js.ast.JsExpression;
import com.google.dart.compiler.backend.js.ast.JsNameRef;
import com.google.dart.compiler.util.AstUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.k2js.translate.context.TranslationContext;
import org.jetbrains.k2js.translate.intrinsic.FunctionIntrinsic;

import java.util.List;

/**
 * @author Pavel Talanov
 */
public enum LengthIntrinsic implements FunctionIntrinsic {

    INSTANCE;

    @NotNull
    @Override
    public JsExpression apply(@NotNull JsExpression receiver, @NotNull List<JsExpression> arguments,
                              @NotNull TranslationContext context) {
        assert arguments.isEmpty() : "Length expression must have zero arguments.";
        //TODO: provide better way
        JsNameRef lengthProperty = AstUtil.newQualifiedNameRef("length");
        AstUtil.setQualifier(lengthProperty, receiver);
        return lengthProperty;
    }
}