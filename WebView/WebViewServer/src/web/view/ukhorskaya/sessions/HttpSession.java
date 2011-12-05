package web.view.ukhorskaya.sessions;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang.math.RandomUtils;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jet.lang.psi.JetPsiFactory;
import web.view.ukhorskaya.*;
import web.view.ukhorskaya.examplesLoader.ExamplesLoader;
import web.view.ukhorskaya.handlers.ServerHandler;
import web.view.ukhorskaya.responseHelpers.CompileAndRunExecutor;
import web.view.ukhorskaya.responseHelpers.JsonResponseForCompletion;
import web.view.ukhorskaya.responseHelpers.JsonResponseForHighlighting;
import web.view.ukhorskaya.server.ServerSettings;
import web.view.ukhorskaya.session.SessionInfo;

import java.io.*;
import java.net.URLDecoder;

/**
 * Created by IntelliJ IDEA.
 * User: Natalia.Ukhorskaya
 * Date: 9/6/11
 * Time: 12:14 PM
 */

public class HttpSession {
    //    private static Logger LOG;
    protected Project currentProject;
    protected PsiFile currentPsiFile;

    private HttpExchange exchange;

    public HttpSession() {
        SessionInfo.TIME_MANAGER = new TimeManager();
    }

    public void handle(final HttpExchange exchange) {
        try {
            this.exchange = exchange;
            String param = exchange.getRequestURI().toString();
            ErrorsWriterOnServer.LOG_FOR_INFO.info("request: " + param);
            //FOR TEST ONLY
            if (param.contains("testConnection")) {
                sendTestConnection();
                return;
            }

            String sId = ResponseUtils.substringBetween(param, "?sessionId=", "&");
            if (sId.equals("") || sId.equals("undefined")) {
                SessionInfo.SESSION_ID = RandomUtils.nextInt();
                ServerHandler.numberOfUsers++;
                ErrorsWriterOnServer.LOG_FOR_INFO.info("Number of users since start server: " + ServerHandler.numberOfUsers);
            } else {
                SessionInfo.SESSION_ID = Integer.parseInt(sId);
            }

            if (param.contains("compile=true") || param.contains("run=true")) {
                SessionInfo.TYPE = SessionInfo.TypeOfRequest.RUN;
                sendExecutorResult();
            } else if (param.contains("writeLog=")) {
                SessionInfo.TYPE = SessionInfo.TypeOfRequest.WRITE_LOG;
                String type = ResponseUtils.substringAfter(param, "writeLog=");
                if (type.equals("info")) {
                    String tmp = getPostDataFromRequest(true).text;
                    ErrorsWriterOnServer.LOG_FOR_INFO.info(tmp);
                } else {
                    String tmp = getPostDataFromRequest(true).text;
                    ErrorsWriterOnServer.LOG_FOR_EXCEPTIONS.error(tmp);
                }
                writeResponse("Data sended", HttpStatus.SC_OK, true);
            } else if (param.contains("complete=true")) {
                SessionInfo.TYPE = SessionInfo.TypeOfRequest.COMPLETE;
                sendCompletionResult();
            } else if (param.contains("exampleId=")) {
                SessionInfo.TYPE = SessionInfo.TypeOfRequest.LOAD_EXAMPLE;
                sendExampleContent();
            } else {
                SessionInfo.TYPE = SessionInfo.TypeOfRequest.HIGHLIGHT;
                sendProjectSourceFile();
            }
        } catch (Throwable e) {
            ErrorsWriterOnServer.LOG_FOR_EXCEPTIONS.error(ErrorsWriter.getExceptionForLog(SessionInfo.TYPE.name(), e, currentPsiFile.getText()));
            writeResponse("Internal server error", HttpStatus.SC_INTERNAL_SERVER_ERROR, true);
        }
    }

    private void sendExampleContent() {
        ExamplesLoader loader = new ExamplesLoader();
        String idStr = ResponseUtils.substringBetween(exchange.getRequestURI().toString(), "exampleId=", "&head=");
        String headName = ResponseUtils.substringAfter(exchange.getRequestURI().toString(), "&head=");
        writeResponse(loader.getResult(Integer.parseInt(idStr), headName), HttpStatus.SC_OK, true);

    }

    //FOR TEST ONLY
    private void sendTestConnection() {
        if (exchange.getRequestURI().toString().contains("stopTest=true")) {
            try {
                String response = getPostDataFromRequest().text;
                File file = new File(ServerSettings.TEST_CONNECTION_OUTPUT + File.separator + "testConnection" + System.nanoTime() + ".csv");
                file.createNewFile();
                FileWriter writer = new FileWriter(file);
                writer.write(response);
                writer.close();
            } catch (IOException e) {
                ErrorsWriterOnServer.LOG_FOR_EXCEPTIONS.error(e, e);
            }

            writeResponse("Response sended", HttpStatus.SC_OK, true);
        } else {
            StringBuilder response = new StringBuilder();
            OutputStream os = null;
            String path = "/testConnection.html";
            InputStream is = ServerHandler.class.getResourceAsStream(path);
            try {
                response.append(ResponseUtils.readData(is));
            } catch (IOException e) {
                ErrorsWriterOnServer.LOG_FOR_EXCEPTIONS.error("Cannot read data from file", e);
                writeResponse("Cannot read data from file", HttpStatus.SC_INTERNAL_SERVER_ERROR, true);
                return;
            }

            try {
                exchange.sendResponseHeaders(400, response.toString().length());
                os = exchange.getResponseBody();
                os.write(response.toString().getBytes());
            } catch (IOException e) {
                //This is an exception we can't send data to client
            } finally {
                try {
                    if (os != null) {
                        os.close();
                    }
                } catch (IOException e) {
                    ErrorsWriterOnServer.LOG_FOR_EXCEPTIONS.error(e);
                }
            }
        }
    }

    private void setGlobalVariables(@Nullable String text) {
        currentProject = Initializer.getEnvironment().getProject();
        if (text == null) {
            /* namespace demo

            import java.math.BigDecimal

            fun main(args : Array<String>) {
                // Easy to make BigDecimals user-friendly
                System.out?.println(
                "2.00".bd - "1.00"
                )
            }

            val String.bd : BigDecimal get() = BigDecimal(this)

            fun BigDecimal.minus(other : BigDecimal) = this.subtract(other)
            fun BigDecimal.minus(other : String) = subtract(other.bd) // this can be omitted*/
            /*text = "fun main(args : Array<String>) {\n" +
                    "    System.out?.println(\"Hello, world!\")\n" +
                    "namespace for while object when this val var" +
                    " fun is in if vararg inline" +
                    "}";*/
            text = "fun main(args : Array<String>) {\n" +
                    "    System.out?.println(\"Hello, world!\")\n" +
                    "}";

        }
        SessionInfo.TIME_MANAGER.saveCurrentTime();
        currentPsiFile = JetPsiFactory.createFile(currentProject, text);
        ErrorsWriterOnServer.LOG_FOR_INFO.info(ErrorsWriter.getInfoForLog(SessionInfo.TYPE.name(), SessionInfo.SESSION_ID, "PARSER " + SessionInfo.TIME_MANAGER.getMillisecondsFromSavedTime() + " size: = " + currentPsiFile.getTextLength()));
    }


    private void sendCompletionResult() {

        String param = exchange.getRequestURI().getQuery();
        String[] position = ResponseUtils.substringBetween(param, "cursorAt=", "&time=").split(",");

        setGlobalVariables(getPostDataFromRequest().text);

        JsonResponseForCompletion jsonResponseForCompletion = new JsonResponseForCompletion(Integer.parseInt(position[0]), Integer.parseInt(position[1]), currentPsiFile);
        writeResponse(jsonResponseForCompletion.getResult(), HttpStatus.SC_OK, true);
    }

    private void sendProjectSourceFile() {
        String param = exchange.getRequestURI().getQuery();

        if ((param != null) && (param.contains("sendData=true"))) {
            setGlobalVariables(getPostDataFromRequest().text);
            JsonResponseForHighlighting responseForHighlighting = new JsonResponseForHighlighting(currentPsiFile);

            String response = responseForHighlighting.getResult();
            response = response.replaceAll("\\n", "");
            writeResponse(response, HttpStatus.SC_OK, true);
        } else {
            setGlobalVariables(null);
            writeResponse(currentPsiFile.getText(), HttpStatus.SC_OK);
        }
    }


    private PostData getPostDataFromRequest() {
        return getPostDataFromRequest(false);
    }

    private PostData getPostDataFromRequest(boolean withNewLines) {
        StringBuilder reqResponse = new StringBuilder();
        try {
            reqResponse.append(ResponseUtils.readData(exchange.getRequestBody(), withNewLines));
        } catch (IOException e) {
            ErrorsWriterOnServer.LOG_FOR_EXCEPTIONS.error(ErrorsWriter.getExceptionForLog(SessionInfo.TYPE.name(), e, "getPostDataFromRequest " + exchange.getRequestURI()));
            writeResponse("Cannot read data from file", HttpStatus.SC_INTERNAL_SERVER_ERROR, true);
            return new PostData("", "");
        }

        String finalResponse = null;
        try {
            finalResponse = URLDecoder.decode(reqResponse.toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            ErrorsWriterOnServer.LOG_FOR_EXCEPTIONS.error(ErrorsWriter.getExceptionForLog(SessionInfo.TYPE.name(), e, "null"));
        }
        if (finalResponse != null) {
            finalResponse = finalResponse.replaceAll("<br>", "\n");
            if (finalResponse.length() > 5) {
                if (finalResponse.contains("&consoleArgs=")) {
                    return new PostData(ResponseUtils.substringBetween(finalResponse, "text=", "&consoleArgs="), ResponseUtils.substringAfter(finalResponse, "&consoleArgs="));
                } else {
                    return new PostData(ResponseUtils.substringAfter(finalResponse, "text="));
                }
            } else {
                writeResponse("Post request is too short", HttpStatus.SC_BAD_REQUEST);
            }
        } else {
            ErrorsWriterOnServer.LOG_FOR_EXCEPTIONS.error(ErrorsWriter.getExceptionForLog(SessionInfo.TYPE.name(), "Cannot read data from post request.", currentPsiFile.getText()));
            writeResponse("Cannot read data from post request: ", HttpStatus.SC_BAD_REQUEST, true);
        }

        return null;
    }

    private void sendExecutorResult() {
        PostData data = getPostDataFromRequest();
        setGlobalVariables(data.text);
        boolean isOnlyCompilation = true;
        if (exchange.getRequestURI().getQuery().contains("compile=true")) {
            isOnlyCompilation = true;
        } else if (exchange.getRequestURI().getQuery().contains("run=true")) {
            isOnlyCompilation = false;
        }

        CompileAndRunExecutor responseForCompilation = new CompileAndRunExecutor(isOnlyCompilation, currentPsiFile, data.arguments);
        writeResponse(responseForCompilation.getResult(), HttpStatus.SC_OK, true);
    }

    private void writeResponse(String responseBody, int errorCode) {
        writeResponse(responseBody, errorCode, false);
    }

    //Send Response
    //disableHeaders - disable html header for answer
    private void writeResponse(String responseBody, int errorCode, boolean disableHeaders) {
        OutputStream os = null;
        StringBuilder response = new StringBuilder();

        String path;
        if (!disableHeaders) {
            /*try {
                response.append(ResponseUtils.readData(new FileReader("c:\\Development\\git-contrib\\jet-contrib\\WebView\\out\\artifacts\\WebView_jar\\header.html"), true));
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            path = "/header.html";
            InputStream is = ServerHandler.class.getResourceAsStream(path);
            if (is == null) {
                ErrorsWriterOnServer.LOG_FOR_EXCEPTIONS.error(ErrorsWriter.getExceptionForLog(SessionInfo.TYPE.name(), "Cannot find header.html for request.", currentPsiFile.getText()));
                writeResponse("File not found", HttpStatus.SC_NOT_FOUND);
                return;
            }
            try {
                response.append(ResponseUtils.readData(is));
            } catch (IOException e) {
                ErrorsWriterOnServer.LOG_FOR_EXCEPTIONS.error(ErrorsWriter.getExceptionForLog(SessionInfo.TYPE.name(), e, currentPsiFile.getText()));
                writeResponse("Cannot read data from file", HttpStatus.SC_INTERNAL_SERVER_ERROR, true);
                return;
            }
        } else {
            response.append("$RESPONSEBODY$");
        }

        String finalResponse = response.toString();
        finalResponse = finalResponse.replace("$RESPONSEBODY$", responseBody);
        if (!disableHeaders) {
            finalResponse = finalResponse.replace("$SESSIONID$", String.valueOf(SessionInfo.SESSION_ID));
            finalResponse = finalResponse.replace("$KOTLINVERSION$", "'" + ServerSettings.KOTLIN_VERSION + "'");
        }
        try {
            //ONLY FOR TEST
            if (exchange.getRequestURI().toString().contains("&time=")) {
                String query = exchange.getRequestURI().getQuery();
                query = query.substring(query.indexOf("&time=") + 6);
                exchange.getResponseHeaders().add("time", query);
            }
            byte[] bytes = finalResponse.getBytes();
            exchange.sendResponseHeaders(errorCode, bytes.length);
            os = exchange.getResponseBody();
            os.write(bytes);
            ErrorsWriterOnServer.LOG_FOR_INFO.info(ErrorsWriter.getInfoForLog(SessionInfo.TYPE.name(), SessionInfo.SESSION_ID, "ALL " + SessionInfo.TIME_MANAGER.getMillisecondsFromStart() + " request=" + exchange.getRequestURI()));
        } catch (IOException e) {
            //This is an exception we can't send data to client
            ErrorsWriterOnServer.LOG_FOR_EXCEPTIONS.error(ErrorsWriter.getExceptionForLog(SessionInfo.TYPE.name(), e, currentPsiFile.getText()));
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                ErrorsWriterOnServer.LOG_FOR_EXCEPTIONS.error(ErrorsWriter.getExceptionForLog(SessionInfo.TYPE.name(), e, currentPsiFile.getText()));
            }
        }
    }

    private class PostData {
        public final String text;
        public String arguments = null;

        private PostData(String text) {
            this.text = text;
        }

        private PostData(String text, String arguments) {
            this.text = text;
            this.arguments = arguments;
        }
    }

}
