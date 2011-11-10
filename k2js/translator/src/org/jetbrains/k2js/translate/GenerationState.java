/*
 * @author Talanov Pavel
 */
package org.jetbrains.k2js.translate;

import com.google.dart.compiler.backend.js.ast.JsProgram;
import com.intellij.openapi.project.Project;
import org.jetbrains.jet.lang.cfg.pseudocode.JetControlFlowDataTraceFactory;
import org.jetbrains.jet.lang.psi.JetFile;
import org.jetbrains.jet.lang.psi.JetNamespace;
import org.jetbrains.jet.lang.resolve.AnalyzingUtils;
import org.jetbrains.jet.lang.resolve.BindingContext;
import org.jetbrains.jet.lang.resolve.java.JavaDefaultImports;

import java.util.Collections;
import java.util.List;

public final class GenerationState {


    public GenerationState() {
    }

//    public void compile(JetFile psiFile) {
//        final JetNamespace namespace = psiFile.getRootNamespace();
//        final BindingContext bindingContext = AnalyzingUtils.getInstance(JavaDefaultImports.JAVA_DEFAULT_IMPORTS).analyzeNamespace(namespace, JetControlFlowDataTraceFactory.EMPTY);
//        AnalyzingUtils.throwExceptionOnErrors(bindingContext);
//        compileCorrectNamespaces(bindingContext, Collections.singletonList(namespace));
//    }

    public JsProgram compileCorrectNamespaces(BindingContext bindingContext, List<JetNamespace> namespaces) {
        //TODO hardcoded
        JsProgram result = new JsProgram("main");
        JetNamespace namespace = namespaces.get(0);
        (new NamespaceTranslator(TranslationContext.rootContext(result, bindingContext))).generateAst(namespace);
        return result;
    }

}
