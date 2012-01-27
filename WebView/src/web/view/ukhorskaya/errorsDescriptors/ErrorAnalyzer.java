package web.view.ukhorskaya.errorsDescriptors;

import com.google.common.base.Predicates;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.jet.lang.cfg.pseudocode.JetControlFlowDataTraceFactory;
import org.jetbrains.jet.lang.diagnostics.Diagnostic;
import org.jetbrains.jet.lang.diagnostics.DiagnosticFactory;
import org.jetbrains.jet.lang.diagnostics.Severity;
import org.jetbrains.jet.lang.diagnostics.UnresolvedReferenceDiagnostic;
import org.jetbrains.jet.lang.psi.JetFile;
import org.jetbrains.jet.lang.resolve.BindingContext;
import org.jetbrains.jet.lang.resolve.java.AnalyzerFacade;
import web.view.ukhorskaya.ErrorWriter;
import web.view.ukhorskaya.Interval;
import web.view.ukhorskaya.exceptions.KotlinCoreException;
import web.view.ukhorskaya.session.SessionInfo;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Natalia.Ukhorskaya
 * Date: 10/28/11
 * Time: 11:24 AM
 */

public class ErrorAnalyzer {
    private final PsiFile currentPsiFile;
    private final Document currentDocument;
    private final Project currentProject;

    private final SessionInfo sessionInfo;

    public ErrorAnalyzer(PsiFile currentPsiFile, SessionInfo info) {
        this.currentPsiFile = currentPsiFile;
        this.currentProject = currentPsiFile.getProject();
        this.currentDocument = currentPsiFile.getViewProvider().getDocument();
        this.sessionInfo = info;
    }

    public List<ErrorDescriptor> getAllErrors() {

        final List<PsiErrorElement> errorElements = new ArrayList<PsiErrorElement>();
        PsiElementVisitor visitor = new PsiElementVisitor() {
            @Override
            public void visitElement(PsiElement element) {
                element.acceptChildren(this);
            }

            @Override
            public void visitErrorElement(PsiErrorElement element) {
                errorElements.add(element);
            }

        };

        visitor.visitFile(currentPsiFile);
        final List<ErrorDescriptor> errors = new ArrayList<ErrorDescriptor>();
        for (PsiErrorElement errorElement : errorElements) {
            int start = errorElement.getTextRange().getStartOffset();
            int end = errorElement.getTextRange().getEndOffset();
            Interval interval = new Interval(start, end, currentDocument);
            errors.add(new ErrorDescriptor(interval, errorElement.getErrorDescription(), Severity.ERROR, "red_wavy_line"));
        }
        sessionInfo.getTimeManager().saveCurrentTime();
        BindingContext bindingContext;
        try {
            /*bindingContext = AnalyzingUtils.getInstance(JavaDefaultImports.JAVA_DEFAULT_IMPORTS).analyzeNamespaces(
                    currentProject,
                    Collections.singletonList(((JetFile) currentPsiFile).getRootNamespace()),
                    Predicates.<PsiFile>equalTo(currentPsiFile),
                    JetControlFlowDataTraceFactory.EMPTY);*/
            /*bindingContext = AnalyzerFacade.analyzeNamespacesWithJavaIntegration(
                                            currentProject,
                                            Collections.singletonList(((JetFile) currentPsiFile).getRootNamespace()),
                                            Predicates.<PsiFile>equalTo(currentPsiFile),
                                            JetControlFlowDataTraceFactory.EMPTY);*/
            bindingContext = AnalyzerFacade.analyzeOneFileWithJavaIntegration(
                                (JetFile) currentPsiFile, JetControlFlowDataTraceFactory.EMPTY);
        } catch (Throwable e) {
            String exception = ErrorWriter.getExceptionForLog(sessionInfo.getType(), e, currentPsiFile.getText());
            ErrorWriter.ERROR_WRITER.writeException(exception);
            throw new KotlinCoreException(e);
        }
        String info = ErrorWriter.getInfoForLogWoIp(sessionInfo.getType(), sessionInfo.getId(),
                "ANALYZE namespaces " + sessionInfo.getTimeManager().getMillisecondsFromSavedTime() + " size: " + currentPsiFile.getTextLength());
        ErrorWriter.ERROR_WRITER.writeInfo(info);
        Collection<Diagnostic> diagnostics = bindingContext.getDiagnostics();

        for (Diagnostic diagnostic : diagnostics) {
            if (diagnostic.getSeverity() != Severity.INFO) {
                DiagnosticFactory factory = diagnostic.getFactory();
                int start = factory.getTextRange(diagnostic).getStartOffset();
                int end = factory.getTextRange(diagnostic).getEndOffset();
                String className = diagnostic.getSeverity().name();
                if (!(diagnostic instanceof UnresolvedReferenceDiagnostic) && (diagnostic.getSeverity() == Severity.ERROR)) {
                    className = "red_wavy_line";
                }
                errors.add(new ErrorDescriptor(new Interval(start, end, currentDocument),
                        diagnostic.getMessage(), diagnostic.getSeverity(), className));
            }
        }

        Collections.sort(errors, new Comparator<ErrorDescriptor>() {
            @Override
            public int compare(ErrorDescriptor o1, ErrorDescriptor o2) {
                if (o1.getInterval().startPoint.line > o2.getInterval().startPoint.line) {
                    return 1;
                } else if (o1.getInterval().startPoint.line < o2.getInterval().startPoint.line) {
                    return -1;
                } else if (o1.getInterval().startPoint.line == o2.getInterval().startPoint.line) {
                    if (o1.getInterval().startPoint.charNumber > o2.getInterval().startPoint.charNumber) {
                        return 1;
                    } else if (o1.getInterval().startPoint.charNumber < o2.getInterval().startPoint.charNumber) {
                        return -1;
                    } else if (o1.getInterval().startPoint.charNumber == o2.getInterval().startPoint.charNumber) {
                        return 0;
                    }
                }
                return -1;
            }
        });

        return errors;
    }
}
