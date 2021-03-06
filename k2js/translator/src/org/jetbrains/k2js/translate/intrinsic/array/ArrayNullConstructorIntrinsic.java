package org.jetbrains.k2js.translate.intrinsic.array;

import com.google.dart.compiler.backend.js.ast.JsExpression;
import com.google.dart.compiler.backend.js.ast.JsNameRef;
import com.google.dart.compiler.util.AstUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.k2js.translate.context.TranslationContext;
import org.jetbrains.k2js.translate.intrinsic.FunctionIntrinsic;

import java.util.List;

/**
 * @author Pavel Talanov
 */
public enum ArrayNullConstructorIntrinsic implements FunctionIntrinsic {

    INSTANCE;

    //TODO: implement function passing to array constructor
    @NotNull
    @Override
    public JsExpression apply(@Nullable JsExpression receiver, @NotNull List<JsExpression> arguments,
                              @NotNull TranslationContext context) {
        assert receiver == null;
        assert arguments.size() == 1;
        //TODO: provide better mechanism
        JsNameRef nullArrayFunName = AstUtil.newQualifiedNameRef("Kotlin.nullArray");
        return AstUtil.newInvocation(nullArrayFunName, arguments);
    }
}
