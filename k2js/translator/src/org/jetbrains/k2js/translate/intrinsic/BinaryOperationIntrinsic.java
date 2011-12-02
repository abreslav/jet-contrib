package org.jetbrains.k2js.translate.intrinsic;

import com.google.dart.compiler.backend.js.ast.JsBinaryOperation;
import com.google.dart.compiler.backend.js.ast.JsBinaryOperator;
import com.google.dart.compiler.backend.js.ast.JsExpression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jet.lexer.JetToken;
import org.jetbrains.k2js.translate.general.TranslationContext;
import org.jetbrains.k2js.translate.operation.OperatorTable;

import java.util.List;

/**
 * @author Talanov Pavel
 */
public class BinaryOperationIntrinsic implements Intrinsic {

    @NotNull
    /*package*/ static BinaryOperationIntrinsic newInstance(@NotNull JetToken token) {
        JsBinaryOperator operator = OperatorTable.getBinaryOperator(token);
        return new BinaryOperationIntrinsic(operator);
    }

    @NotNull
    private final JsBinaryOperator operator;

    private BinaryOperationIntrinsic(@NotNull JsBinaryOperator operator) {
        this.operator = operator;
    }

    @NotNull
    @Override
    public JsExpression apply(@NotNull JsExpression receiver, @NotNull List<JsExpression> arguments,
                              @NotNull TranslationContext context) {
        assert arguments.size() == 1 : "Binary operator should have a receiver and one argument";
        return new JsBinaryOperation(operator, receiver, arguments.get(0));
    }
}
