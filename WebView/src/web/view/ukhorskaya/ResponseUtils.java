package web.view.ukhorskaya;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.util.Pair;
import org.apache.commons.httpclient.HttpStatus;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Natalia.Ukhorskaya
 * Date: 10/27/11
 * Time: 5:50 PM
 */

public class ResponseUtils {
    public static String escapeString(String string) {
        if (string.contains("<")) {
            string = string.replaceAll("<", "&lt;");
        }
        if (string.contains(">")) {
            string = string.replaceAll(">", "&gt;");
        }
        return string;
    }

    public static Project getProjectByName(String projectName) {
        for (Project p : ProjectManager.getInstance().getOpenProjects()) {
            if (p.getName().equals(projectName)) {
                return p;
            }
        }
        return null;
    }

    //Get Color as String
    public static String getColor(Color color) {
        java.util.List<String> colors = new ArrayList<String>();
        colors.add(Long.toHexString(color.getRed()));
        colors.add(Long.toHexString(color.getGreen()));
        colors.add(Long.toHexString(color.getBlue()));

        StringBuilder buffer = new StringBuilder("#");
        for (String c : colors) {
            if (c.length() == 1) {
                buffer.append("0");
            }
            buffer.append(c);
        }
        return (buffer.toString());
    }

    //Get fontType as String
    public static String getFontType(int fontType) {
        switch (fontType) {
            case 1:
                return "font-weight: bold;";
            case 2:
                return "font-style: italic;";
            case 3:
                return "font-style: italic; font-weight: bold;";
            default:
                return "";

        }
    }

    public static String substringAfter(String str, String after) {
        int pos = str.indexOf(after);
        if (pos != -1) {
            return str.substring(pos + after.length());
        }
        return "";
    }

    //Substring str and return all str if after not found
    public static String substringAfterReturnAll(String str, String after) {
        int pos = str.indexOf(after);
        if (pos != -1) {
            return str.substring(pos + after.length());
        }
        return str;
    }

    public static String substringBefore(String str, String before) {
        int pos = str.indexOf(before);
        if (pos != -1) {
            return str.substring(0, pos);
        }
        return str;
    }


    public static String substringBetween(String str, String before, String after) {
        int fPos = str.indexOf(before);
        int sPos = str.indexOf(after);
        if ((fPos != -1) && (sPos != -1) && (fPos + before.length() < sPos)) {
            return str.substring(fPos + before.length(), sPos);
        }
        if (sPos == -1) {
            return substringAfter(str, before);
        } else if (fPos == -1) {
            return substringBefore(str, after);
        }
        return "";
    }

    public static String addNewLine() {
        return "<br/>";
    }

    public static String readData(InputStream is) throws IOException {
        InputStreamReader reader = new InputStreamReader(is);
        StringBuilder response = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(reader);

        String tmp;
        while ((tmp = bufferedReader.readLine()) != null) {
            response.append(tmp);
        }

        return response.toString();
    }

}