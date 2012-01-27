package web.view.ukhorskaya.handlers;

import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.commons.httpclient.HttpStatus;
import org.jetbrains.annotations.Nullable;
import web.view.ukhorskaya.ErrorWriter;
import web.view.ukhorskaya.ErrorWriterOnServer;
import web.view.ukhorskaya.ResponseUtils;
import web.view.ukhorskaya.Statistics;
import web.view.ukhorskaya.authorization.AuthorizationHelper;
import web.view.ukhorskaya.authorization.UserAuthenticator;
import web.view.ukhorskaya.examplesLoader.ExamplesList;
import web.view.ukhorskaya.examplesLoader.ExamplesLoader;
import web.view.ukhorskaya.help.HelpLoader;
import web.view.ukhorskaya.log.LogDownloader;
import web.view.ukhorskaya.session.SessionInfo;
import web.view.ukhorskaya.sessions.HttpSession;

import java.io.*;
import java.net.URLDecoder;
import java.util.Calendar;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Natalia.Ukhorskaya
 * Date: 8/10/11
 * Time: 1:16 PM
 */

public class ServerHandler implements HttpHandler {

    @Override
    public void handle(final HttpExchange exchange) throws IOException {
        if (Statistics.getInstance().isNecessaryToUpdateStatistics()) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    Statistics.getInstance().updateStatistics(false);
                }
            });
            t.start();
        }

        SessionInfo sessionInfo;
        try {
            String param = exchange.getRequestURI().toString();

            if (param.contains("userData=true")) {
                sessionInfo = setSessionInfo(exchange);
                sendUserInformation(exchange, sessionInfo);
            } else if (param.startsWith("/updateExamples")) {
                updateExamples(exchange);
            } else if (param.startsWith("/logs")) {
                ErrorWriterOnServer.LOG_FOR_INFO.info(SessionInfo.TypeOfRequest.GET_LOGS_LIST.name());
                sendListLogs(exchange);
            } else if (param.contains("/sortedExceptions=")) {
                ErrorWriterOnServer.LOG_FOR_INFO.info(SessionInfo.TypeOfRequest.DOWNLOAD_LOG.name());
                sendSortedExceptions(exchange);
            } else if (param.contains("log=")) {
                ErrorWriterOnServer.LOG_FOR_INFO.info(SessionInfo.TypeOfRequest.DOWNLOAD_LOG.name() + " " + exchange.getRequestURI());
                sendLog(exchange);
            } else if (param.contains("allExamples=true")) {
                ErrorWriterOnServer.LOG_FOR_INFO.info(SessionInfo.TypeOfRequest.GET_EXAMPLES_LIST.name());
                sendExamplesList(exchange);
            } else if (param.contains("allHelpExamples=true")) {
                ErrorWriterOnServer.LOG_FOR_INFO.info(SessionInfo.TypeOfRequest.GET_HELP_FOR_EXAMPLES.name());
                sendHelpContentForExamples(exchange);
            } else if (param.contains("allHelpWords=true")) {
                ErrorWriterOnServer.LOG_FOR_INFO.info(SessionInfo.TypeOfRequest.GET_HELP_FOR_WORDS.name());
                sendHelpContentForWords(exchange);
            } else if ((param.contains("/path="))
                    || (param.equals("/"))
                    || (param.startsWith("/?"))
                    || param.contains("testConnection")
                    || param.contains("writeLog=")
                    || param.contains("login.html")) {
                if (!param.contains("testConnection") && !param.contains("writeLog=")) {
                    sessionInfo = setSessionInfo(exchange);
                } else {
                    sessionInfo = new SessionInfo(0);
                }
                HttpSession session = new HttpSession(sessionInfo);

                session.handle(exchange);
            } else {
                ErrorWriterOnServer.LOG_FOR_INFO.info(SessionInfo.TypeOfRequest.GET_RESOURCE.name() + " " + exchange.getRequestURI());
                sendResourceFile(exchange);
            }
        } catch (Throwable e) {
            //Do not stop server
            ErrorWriterOnServer.LOG_FOR_EXCEPTIONS.error(ErrorWriter.getExceptionForLog(exchange.getRequestURI().toString(), e, "null"));
            writeResponse(exchange, "Internal server error".getBytes(), HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void sendAddUser(HttpExchange exchange) {
        String request = exchange.getRequestURI().toString();
        UserAuthenticator authenticator;
        if (request.contains("addManager=")) {
            authenticator = new UserAuthenticator("managers");
        } else {
            authenticator = new UserAuthenticator("users");
        }
        String response = authenticator.addUser(ResponseUtils.substringBetween(request, "&login=", "&"),
                ResponseUtils.substringAfter(request, "&password="));
        writeResponse(exchange, response.getBytes(), HttpStatus.SC_OK);
    }

    private void updateExamples(HttpExchange exchange) {
        ExamplesList.updateList();
        HelpLoader.updateExamplesHelp();
        writeResponse(exchange, "Examples and help was reloaded".getBytes(), HttpStatus.SC_OK);
    }

    private void sendSortedExceptions(HttpExchange exchange) {
        String param = exchange.getRequestURI().toString();
        if (param.contains("download")) {
            exchange.getResponseHeaders().add("Content-type", "application/x-download");
        }
        String from = ResponseUtils.substringBetween(param, "&from=", "&to=");
        String to = ResponseUtils.substringAfter(param, "&to=");

        writeResponse(exchange, new LogDownloader().getSortedExceptions(from, to).getBytes(), 200);
    }

    @Nullable
    private SessionInfo setSessionInfo(HttpExchange exchange) {
        SessionInfo sessionInfo;
        String sessionId = getSessionIdFromCookies(exchange.getRequestHeaders());
        if (sessionId == null || sessionId.isEmpty()) {
            sessionId = getSessionIdFromRequest(exchange.getRequestURI().toString());
            if (!sessionId.equals("")) {
                sessionInfo = new SessionInfo(Integer.parseInt(sessionId));

                ErrorWriterOnServer.LOG_FOR_EXCEPTIONS.info(ErrorWriter.getExceptionForLog("SET_SESSION_ID",
                        "Impossible to read id from cookies", generateStringFromList(exchange.getRequestHeaders().get("Cookie"))));
            } else {
                Statistics.incNumberOfUsers();
                sessionInfo = new SessionInfo(Integer.parseInt(Statistics.getNumberOfUsers()));
                ErrorWriterOnServer.LOG_FOR_INFO.info("Number_of_users_since_start_server : " + Statistics.getNumberOfUsers());
            }
        } else {
            try {
                sessionInfo = new SessionInfo(Integer.parseInt(sessionId));
            } catch (NumberFormatException e) {
                Statistics.incNumberOfUsers();
                sessionInfo = new SessionInfo(Integer.parseInt(Statistics.getNumberOfUsers()));
                ErrorWriterOnServer.LOG_FOR_INFO.info("Number_of_users_since_start_server : " + Statistics.getNumberOfUsers());
                ErrorWriterOnServer.LOG_FOR_EXCEPTIONS.error(ErrorWriter.getExceptionForLog(SessionInfo.TypeOfRequest.SEND_USER_DATA.toString(), e, sessionId));
            }
        }
        return sessionInfo;
    }

    private String getSessionIdFromRequest(String request) {
        if (request.contains("sessionId=")) {
            return ResponseUtils.substringBetween(request, "sessionId", "&");
        }
        return "";
    }

    private String getSessionIdFromCookies(Headers responseHeaders) {
        for (String key : responseHeaders.keySet()) {
            if (key.equals("Cookie")) {
                List<String> cookie = responseHeaders.get(key);
                if (cookie.size() > 0) {
                    String all = generateStringFromList(cookie);
                    ErrorWriterOnServer.LOG_FOR_INFO.info("cookies:" + all);
                    return ResponseUtils.substringBetween(all, "userId=", ";");
                }
            }
        }
        return "";
    }

    private String generateStringFromList(List<String> list) {
        StringBuilder builder = new StringBuilder();
        for (String s : list) {
            builder.append(s);
            builder.append(";");
        }
        return builder.toString();
    }

    private void sendHelpContentForExamples(HttpExchange exchange) {
        writeResponse(exchange, HelpLoader.getInstance().getHelpForExamples().getBytes(), 200);
    }

    private void sendHelpContentForWords(HttpExchange exchange) {
        writeResponse(exchange, HelpLoader.getInstance().getHelpForWords().getBytes(), 200);
    }

    private void sendLog(HttpExchange exchange) {
        String param = exchange.getRequestURI().toString();
        String path;
        if (param.contains("&download")) {
            exchange.getResponseHeaders().add("Content-type", "application/x-download");
        }
        if (param.contains("&view")) {
            path = ResponseUtils.substringBetween(param, "log=", "&view");
        } else {
            path = ResponseUtils.substringBetween(param, "log=", "&download");
        }
        path = path.replaceAll("%5C", "/");
        writeResponse(exchange, new LogDownloader().download(path).getBytes(), 200);
    }

    private void sendListLogs(HttpExchange exchange) {
        if (!exchange.getRequestURI().toString().contains("&statistics")) {
            Authenticator authenticator = new UserAuthenticator("managers");
            Authenticator.Result result = authenticator.authenticate(exchange);
            if (result instanceof Authenticator.Success) {
                System.out.println("OK");
            } else {
                try {
                    writeResponse(exchange,
                            ResponseUtils.readData(ServerHandler.class.getResourceAsStream("/login.html")).getBytes(),
                            HttpStatus.SC_OK);
                } catch (IOException e) {
                    ErrorWriterOnServer.LOG_FOR_EXCEPTIONS.error(ErrorWriter.getExceptionForLog("LOGIN", e, "Login failed"));
                }
                return;
            }
        }

        InputStream is = ServerHandler.class.getResourceAsStream("/logs.html");
        try {
            String response = ResponseUtils.readData(is);
            String links = new LogDownloader().getFilesLinks();
            response = response.replace("$LINKSTOLOGFILES$", links);
            response = response.replace("$CURRENTDATE$", ResponseUtils.getDate(Calendar.getInstance()));

            if (exchange.getRequestURI().toString().contains("&statistics")) {
                Statistics.getInstance().updateStatistics(true);
            }
            response = Statistics.getInstance().writeStatistics(response);
            writeResponse(exchange, response.getBytes(), 200);
        } catch (IOException e) {
            ErrorWriterOnServer.LOG_FOR_EXCEPTIONS.error(ErrorWriter.getExceptionForLog(SessionInfo.TypeOfRequest.GET_LOGS_LIST.name(), e, "null"));
        }
    }

    private void sendExamplesList(HttpExchange exchange) {
        ExamplesLoader loader = new ExamplesLoader();
        writeResponse(exchange, loader.getExamplesList().getBytes(), HttpStatus.SC_OK);
    }

    private void sendUserInformation(HttpExchange exchange, SessionInfo info) {
        StringBuilder reqResponse = new StringBuilder();
        try {
            reqResponse.append(ResponseUtils.readData(exchange.getRequestBody()));
        } catch (IOException e) {
            ErrorWriterOnServer.LOG_FOR_EXCEPTIONS.error(ErrorWriter.getExceptionForLog(SessionInfo.TypeOfRequest.SEND_USER_DATA.name(), e, "null"));
            writeResponse(exchange, "Cannot read data from file".getBytes(), HttpStatus.SC_INTERNAL_SERVER_ERROR);
            return;
        }
        try {
            reqResponse = new StringBuilder(URLDecoder.decode(reqResponse.toString(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            ErrorWriterOnServer.LOG_FOR_EXCEPTIONS.error(ErrorWriter.getExceptionForLog(SessionInfo.TypeOfRequest.SEND_USER_DATA.name(), e, "null"));
        }
        ErrorWriterOnServer.LOG_FOR_INFO.info(ErrorWriter.getInfoForLog(SessionInfo.TypeOfRequest.INC_NUMBER_OF_REQUESTS.name(), info.getId(), SessionInfo.TypeOfRequest.SEND_USER_DATA.name()));
        ErrorWriterOnServer.LOG_FOR_INFO.info(ErrorWriter.getInfoForLog(SessionInfo.TypeOfRequest.SEND_USER_DATA.name(), info.getId(), ResponseUtils.substringAfter(reqResponse.toString(), "text=")));
        writeResponse(exchange, "OK".getBytes(), HttpStatus.SC_OK);
    }

    private void sendResourceFile(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        path = ResponseUtils.substringAfterReturnAll(path, "resources");

        if (path.equals("")) {
            ErrorWriterOnServer.LOG_FOR_EXCEPTIONS.error(ErrorWriter.getExceptionForLog(SessionInfo.TypeOfRequest.GET_RESOURCE.name(), "Path to the file is incorrect.", exchange.getRequestURI().toString()));
            writeResponse(exchange, "Path to the file is incorrect.".getBytes(), HttpStatus.SC_NOT_FOUND);
            return;
        }

        InputStream is = ServerHandler.class.getResourceAsStream(path);
        if (is == null) {
            ErrorWriterOnServer.LOG_FOR_EXCEPTIONS.error(ErrorWriter.getExceptionForLog(SessionInfo.TypeOfRequest.GET_RESOURCE.name(), "Resource not found.", exchange.getRequestURI().toString()));
            writeResponse(exchange, ("Resource not found. " + path).getBytes(), HttpStatus.SC_NOT_FOUND);
            return;
        }

        int length;
        byte[] tmp = new byte[1024];
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            while ((length = is.read(tmp)) >= 0) {
                out.write(tmp, 0, length);
            }
        } catch (IOException e) {
            ErrorWriterOnServer.LOG_FOR_EXCEPTIONS.error(ErrorWriter.getExceptionForLog(SessionInfo.TypeOfRequest.GET_RESOURCE.name(), e, exchange.getRequestURI().toString()));
            writeResponse(exchange, "Could not load the resource from the server".getBytes(), HttpStatus.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        writeResponse(exchange, out.toByteArray(), HttpStatus.SC_OK);
    }

    private void writeResponse(HttpExchange exchange, byte[] responseBody, int errorCode) {
        OutputStream os = null;
        try {
            exchange.sendResponseHeaders(errorCode, responseBody.length);
            os = exchange.getResponseBody();
            os.write(responseBody);
        } catch (IOException e) {
            //This is an exception we can't to send data to client
            ErrorWriterOnServer.LOG_FOR_EXCEPTIONS.error(ErrorWriter.getExceptionForLog("UNKNOWN", e, exchange.getRequestURI().toString()));
        } finally {
            close(os);
        }
    }

    private void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            ErrorWriterOnServer.LOG_FOR_EXCEPTIONS.error(ErrorWriter.getExceptionForLog("UNKNOWN", e, " NULL"));
        }
    }

}

