package org.jetbrains.demo.ukhorskaya;

import com.sun.xml.internal.xsom.impl.ListSimpleTypeImpl;
import org.jetbrains.demo.ukhorskaya.server.ServerSettings;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Natalia.Ukhorskaya
 * Date: 11/9/11
 * Time: 2:16 PM
 */

public abstract class ErrorWriter {

    public static ErrorWriter ERROR_WRITER;

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
        builder.append(ResponseUtils.escapeString(typeOfRequest));
        builder.append("</type>");
        builder.append("\n<message>");
        builder.append(ResponseUtils.escapeString(throwable.getMessage()));
        builder.append("</message>");
        builder.append("\n<stack>");
        StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter));
        builder.append(ResponseUtils.escapeString(stringWriter.toString()));
        builder.append("\n</stack>");
        builder.append("\n<moreinfo>");
        builder.append("\n").append(ResponseUtils.escapeString(moreinfo));
        builder.append("\n</moreinfo>");
        builder.append("\n</error>");
        return builder.toString();
    }
    
    public static List<String> parseException(String text) {
        ArrayList<String> list = new ArrayList<String>();
        //0
        String str = ResponseUtils.substringBetween(text, "<version>", "</version>");
        list.add(str);
        //1
        str = ResponseUtils.substringBetween(text, "<type>", "</type>");
        list.add(str);
        //2
        str = ResponseUtils.substringBetween(text, "<message>", "</message>");
        list.add(str);
        //3
        str = ResponseUtils.substringBetween(text, "<stack>", "</stack>");
        list.add(str);
         //4
        str = ResponseUtils.substringBetween(text, "<moreinfo>", "</moreinfo>");
        list.add(str);

        return list;
    }

    public static String getExceptionForLog(String typeOfRequest, String message, String stackTrace, String moreinfo) {
        StringBuilder builder = new StringBuilder();
        builder.append("\n<error>");
        builder.append("\n<version>");
        builder.append(ServerSettings.KOTLIN_VERSION);
        builder.append("</version>");
        builder.append("\n<type>");
        builder.append(ResponseUtils.escapeString(typeOfRequest));
        builder.append("</type>");
        builder.append("\n<message>");
        builder.append(ResponseUtils.escapeString(message));
        builder.append("</message>");
        builder.append("\n<stack>");
        builder.append(ResponseUtils.escapeString(stackTrace));
        builder.append("\n</stack>");
        builder.append("\n<moreinfo>");
        builder.append("\n").append(ResponseUtils.escapeString(moreinfo));
        builder.append("\n</moreinfo>");
        builder.append("\n</error>");
        return builder.toString();
    }

    public static String getExceptionForLog(String typeOfRequest, String message, String moreinfo) {
        return getExceptionForLog(typeOfRequest, message, message, moreinfo);
    }

    public static String getInfoForLog(String typeOfRequest, String userId, String message) {
        StringBuilder builder = new StringBuilder();
        builder.append("type=").append(typeOfRequest);
        builder.append(" ");
        builder.append("userId=");
        builder.append(String.valueOf(userId));
        builder.append(" ip=0 ");
        builder.append("message=").append(message);
        return builder.toString();
    }

    public static String getInfoForLogWoIp(String typeOfRequest, String userId, String message) {
        StringBuilder builder = new StringBuilder();
        builder.append("type=").append(typeOfRequest);
        builder.append(" ");
        builder.append("userId=");
        builder.append(String.valueOf(userId));
        builder.append(" ");
        builder.append("message=").append(message);
        return builder.toString();
    }

    public abstract void writeException(String message);

    public abstract void writeExceptionToExceptionAnalyzer(Throwable e, String type, String description);
    
    public abstract void writeExceptionToExceptionAnalyzer(String message, String stackTrace, String type, String description);

    public abstract void writeInfo(String message);

}
