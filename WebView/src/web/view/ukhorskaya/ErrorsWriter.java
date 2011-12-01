package web.view.ukhorskaya;

import org.apache.log4j.Logger;
import web.view.ukhorskaya.server.ServerSettings;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: Natalia.Ukhorskaya
 * Date: 11/9/11
 * Time: 2:16 PM
 */

public class ErrorsWriter {

    public static final Logger LOG_FOR_EXCEPTIONS = Logger.getLogger("exceptionLogger");
    public static final Logger LOG_FOR_INFO = Logger.getLogger("infoLogger");

    public static void writeErrorToConsole(String message) {
        System.err.println(message);
    }

    public static void writeExceptionToConsole(String message, Throwable e) {
        System.err.println(message);
        e.printStackTrace();
    }

    public static void writeExceptionToConsole(Throwable e) {
        e.printStackTrace();
    }

    public static void writeInfoToConsole(String message) {
        System.out.println(message);
    }

    public static String getExceptionForLog(String typeOfRequest, Throwable throwable, String moreinfo) {
        StringBuilder builder = new StringBuilder();
        builder.append("\n<error>");
        builder.append("\n<version>");
        builder.append(ServerSettings.KOTLIN_VERSION);
        builder.append("</version>");
        builder.append("\n<type>");
        builder.append(typeOfRequest);
        builder.append("</type>");
        builder.append("\n<message>");
        builder.append(throwable.getMessage());
        builder.append("</message>");
        builder.append("\n<stack>");
        StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter));
        builder.append(stringWriter.toString());
        builder.append("\n</stack>");
        builder.append("\n<moreinfo>");
        builder.append("\n").append(moreinfo);
        builder.append("\n</moreinfo>");
        builder.append("\n</error>");
        return builder.toString();
    }

    public static String getExceptionForLog(String typeOfRequest, String message, String stackTrace, String moreinfo) {
        StringBuilder builder = new StringBuilder();
        builder.append("\n<error>");
        builder.append("\n<type>");
        builder.append(typeOfRequest);
        builder.append("</type>");
        builder.append("\n<message>");
        builder.append(message);
        builder.append("</message>");
        builder.append("\n<stack>");
        builder.append(stackTrace);
        builder.append("\n</stack>");
        builder.append("\n<moreinfo>");
        builder.append("\n").append(moreinfo);
        builder.append("\n</moreinfo>");
        builder.append("\n</error>");
        return builder.toString();
    }

    public static String getExceptionForLog(String typeOfRequest, String message, String moreinfo) {
        StringBuilder builder = new StringBuilder();
        builder.append("\n<error>");
        builder.append("\n<type>");
        builder.append(typeOfRequest);
        builder.append("</type>");
        builder.append("\n<message>");
        builder.append(message);
        builder.append("</message>");
        builder.append("\n<moreinfo>");
        builder.append("\n").append(moreinfo);
        builder.append("\n</moreinfo>");
        builder.append("\n</error>");
        return builder.toString();
    }

    public static String getInfoForLog(String typeOfRequest, int userId, String message) {
        StringBuilder builder = new StringBuilder();
        builder.append(typeOfRequest);
        builder.append(" ");
        builder.append("userId=");
        builder.append(String.valueOf(userId));
        builder.append(" ");
        builder.append(message);
        return builder.toString();
    }


    public static void sendTextToServer(String text, String request) {
        String urlPath = "";
        if (request.equals("info")) {
            urlPath = "http://localhost/?sessionId=555&writeLog=error";
        } else if (request.equals("error")) {
            urlPath = "http://localhost/?sessionId=555&writeLog=info";
        }

        URL url;
        try {
            url = new URL(urlPath);

            HttpURLConnection urlConnection = null;
            urlConnection = (HttpURLConnection) url.openConnection();

            if (text != null) {
                urlConnection.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                wr.write(text);
                wr.flush();
                wr.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendErrorToServer(String error) {
        sendTextToServer(error, "error");
    }

    public static void sendInfoToServer(String info) {
        sendTextToServer(info, "info");
    }

}
