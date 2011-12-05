package web.view.ukhorskaya;

import web.view.ukhorskaya.session.SessionInfo;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: Natalia.Ukhorskaya
 * Date: 12/5/11
 * Time: 2:27 PM
 */

public class ErrorsWriterInApplet extends ErrorsWriter {

    private static final ErrorsWriterInApplet writer = new ErrorsWriterInApplet();

    public static ErrorsWriterInApplet getInstance() {
        return writer;
    }

    private ErrorsWriterInApplet() {
    }

    @Override
    public void writeException(String message) {
        sendTextToServer(message, MainApplet.request, "error");
    }

    @Override
    public void writeInfo(String message) {
        //sendTextToServer(message, MainApplet.request, "info");
    }

    public static void sendTextToServer(String text, String request, String type) {
        String urlPath = request + "/?sessionId=" + SessionInfo.SESSION_ID + "&writeLog=" + type;

        URL url;
        try {
            url = new URL(urlPath);

            HttpURLConnection urlConnection;
            urlConnection = (HttpURLConnection) url.openConnection();

            if (text != null) {
                urlConnection.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                wr.write("text=" + text);
                wr.flush();
                wr.close();
            }
            //urlConnection.connect();

            BufferedReader in;

            try {
                in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            } catch (FileNotFoundException e) {
                in = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
            }

            String str;
            StringBuilder result = new StringBuilder();
            while ((str = in.readLine()) != null) {
                result.append(str);
            }
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
