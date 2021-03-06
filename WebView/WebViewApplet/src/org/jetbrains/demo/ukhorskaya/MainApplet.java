package org.jetbrains.demo.ukhorskaya;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.demo.ukhorskaya.responseHelpers.JsonResponseForCompletion;
import org.jetbrains.demo.ukhorskaya.responseHelpers.JsonResponseForHighlighting;
import org.jetbrains.demo.ukhorskaya.session.SessionInfo;
import org.jetbrains.jet.lang.psi.JetFile;
import org.jetbrains.jet.lang.psi.JetPsiFactory;
import org.jetbrains.jet.lang.resolve.BindingContext;
import org.jetbrains.k2js.facade.K2JSTranslatorUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: Natalia.Ukhorskaya
 * Date: 11/28/11
 * Time: 10:46 AM
 */

public class MainApplet extends JApplet implements ActionListener {
    /*private JButton b1;
    private JButton b2;*/

    private static String EXCEPTION = "exception=";

    public static String request;

    public static SessionInfo SESSION_INFO;

    public void init() {
        InitializerApplet.getInstance().initJavaCoreEnvironment();
        request = getCodeBase().getProtocol() + "://" + getCodeBase().getHost();
        ErrorWriter.ERROR_WRITER = ErrorWriterInApplet.getInstance();

        SESSION_INFO = new SessionInfo("applet" + new Random().nextInt());
        getHighlighting("fun main(args : Array<String>) {\n" +
                "  System.out?.println(\"Hello, world!\"\n" +
                "}");
        /*URL javaScript = null;

        try {
            javaScript = new URL("javascript:onAppletIsReady()");
        } catch (MalformedURLException exception) {
            exception.printStackTrace();
        }
        getAppletContext().showDocument(javaScript, "_self");*/

        /*Container contentPane = this.getContentPane();
        contentPane.setLayout(new FlowLayout());
        b1 = new JButton("highlighting");
        b1.addActionListener(this);
        contentPane.add(b1);
        b2 = new JButton("completion");
        b2.addActionListener(this);
        contentPane.add(b2);*/
    }

    public String getHighlighting(String data) {
        return getHighlighting(data, "java");
    }

    public String getHighlighting(String data, String runConfiguration) {
        SESSION_INFO.setType(SessionInfo.TypeOfRequest.HIGHLIGHT);
        try {
            JetFile currentPsiFile = JetPsiFactory.createFile(InitializerApplet.getEnvironment().getProject(), data);
            SESSION_INFO.setRunConfiguration(runConfiguration);
           /* if (runConfiguration.equals("canvas")) {
                SESSION_INFO.setRunConfiguration(SessionInfo.RunConfiguration.CANVAS);
            }*/
            JsonResponseForHighlighting responseForHighlighting = new JsonResponseForHighlighting(currentPsiFile, SESSION_INFO);
            return responseForHighlighting.getResult();
//            return responseForHighlighting.getResult(InitializerApplet.getEnvironment());

        } catch (Throwable e) {
            ErrorWriter.ERROR_WRITER.writeExceptionToExceptionAnalyzer(e,
                    SESSION_INFO.getType(), data);
            StringWriter writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            return ResponseUtils.getErrorInJson(writer.toString());
        }
    }

    public void checkApplet() {

    }

    @Nullable
    public String translateToJS(@NotNull String code, @NotNull String arguments) {
        try {
            return new K2JSTranslatorUtils().translateToJS(code, arguments);
        } catch (AssertionError e) {
            ErrorWriter.ERROR_WRITER.writeExceptionToExceptionAnalyzer(e, SessionInfo.TypeOfRequest.CONVERT_TO_JS.name(), code);
            return EXCEPTION + "Translation error.";
        } catch (UnsupportedOperationException e) {
            ErrorWriter.ERROR_WRITER.writeExceptionToExceptionAnalyzer(e, SessionInfo.TypeOfRequest.CONVERT_TO_JS.name(), code);
            return EXCEPTION + "Unsupported feature.";
        } catch (Throwable e) {
            ErrorWriter.ERROR_WRITER.writeExceptionToExceptionAnalyzer(e, SessionInfo.TypeOfRequest.CONVERT_TO_JS.name(), code);
            return EXCEPTION + "Unexpected exception.";
        }
    }



    public String getCompletion(String data, String line, String ch, String runConfiguration) {
        SESSION_INFO.setType(SessionInfo.TypeOfRequest.COMPLETE);
        try {
            JetFile currentPsiFile = JetPsiFactory.createFile(InitializerApplet.getEnvironment().getProject(), data);
            SESSION_INFO.setRunConfiguration(runConfiguration);
            JsonResponseForCompletion responseForCompletion = new JsonResponseForCompletion(Integer.parseInt(line),
                    Integer.parseInt(ch), currentPsiFile, SESSION_INFO);
            System.out.println(line + " " + ch);
            return responseForCompletion.getResult();

        } catch (Throwable e) {
            e.printStackTrace();
            ErrorWriter.ERROR_WRITER.writeExceptionToExceptionAnalyzer(e,
                    SESSION_INFO.getType(), data + " line: " + line + " ch: " + ch);
            StringWriter writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            return ResponseUtils.getErrorInJson(writer.toString());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        /*if (e.getActionCommand().equals("highlighting")) {
            getHighlighting("fun main() { val a = java.util.ArrayList<String>(); System.out?.println(\"sss\" + a}");
        } else if (e.getActionCommand().equals("completion")) {
            getCompletion("import fun main() { System.out?.println(\"sss\" + a)}", "0", "7");
        }*/
//        getHighlighting("fun main() { val a = Object()}");
//        getHighlighting("fun main() { val a = String(\"aaa\") }");
    }
}
