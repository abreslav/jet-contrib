package org.jetbrains.demo.ukhorskaya.database;

import com.mysql.jdbc.jdbc2.optional.JDBC4ConnectionWrapper;
import org.apache.commons.lang.math.RandomUtils;
import org.jetbrains.demo.ukhorskaya.ErrorWriter;
import org.jetbrains.demo.ukhorskaya.ErrorWriterOnServer;
import org.jetbrains.demo.ukhorskaya.ResponseUtils;
import org.jetbrains.demo.ukhorskaya.handlers.ServerHandler;
import org.jetbrains.demo.ukhorskaya.server.ServerSettings;
import org.jetbrains.demo.ukhorskaya.session.SessionInfo;
import org.jetbrains.demo.ukhorskaya.session.UserInfo;
import org.json.JSONArray;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Natalia.Ukhorskaya
 * Date: 1/25/12
 * Time: 10:10 AM
 */

public class MySqlConnector {
    private Connection connection;

    private static final MySqlConnector connector = new MySqlConnector();

    private MySqlConnector() {
        if (!connect() || !createTablesIfNecessary()) {
            System.exit(1);
        }
    }

    private boolean connect() {
        String url = "";
        try {
            url = "jdbc:mysql://" + ServerSettings.MYSQL_HOST + ":" + ServerSettings.MYSQL_PORT + "/" + ServerSettings.MYSQL_DATABASE_NAME + "";
            connection = DriverManager.getConnection(url, ServerSettings.MYSQL_USERNAME, ServerSettings.MYSQL_PASSWORD);
            ErrorWriter.writeInfoToConsole("Connected to database: " + url);
            ErrorWriter.getInfoForLog("CONNECT_TO_DATABASE", "-1", "Connected to database: " + url);
            checkDatabaseVersion();
            return true;
        } catch (Throwable e) {
            ErrorWriter.writeErrorToConsole("Cannot connect to database: " + url);
            ErrorWriter.ERROR_WRITER.writeExceptionToExceptionAnalyzer(e, SessionInfo.TypeOfRequest.WORK_WITH_DATABASE.name(), url);
            e.printStackTrace();
            return false;
        }
    }

    private boolean checkConnection() {
        try {
            return connection.isValid(1000) || connect();
        } catch (SQLException e) {
            ErrorWriter.ERROR_WRITER.writeExceptionToExceptionAnalyzer(e,
                    SessionInfo.TypeOfRequest.WORK_WITH_DATABASE.name(),
                    "jdbc:mysql://" + ServerSettings.MYSQL_HOST + "/" + ServerSettings.MYSQL_DATABASE_NAME + "");
            return false;
        }
    }

    private boolean createTablesIfNecessary() {
        try {
            if (!checkConnection()) {
                return false;
            }
            PreparedStatement st = connection.prepareStatement("SHOW TABLES");
            ResultSet rs = st.executeQuery();
            if (!rs.next()) {
                st = connection.prepareStatement("CREATE TABLE databaseinfo (" +
                        "  VERSION VARCHAR(45)" +
                        ")" +
                        "ENGINE = InnoDB;");
                st.execute();
                st = connection.prepareStatement("CREATE TABLE users (" +
                        "  USER_ID VARCHAR(45) NOT NULL DEFAULT ''," +
                        "  USER_TYPE VARCHAR(45) NOT NULL DEFAULT ''," +
                        "  USER_NAME VARCHAR(45) NOT NULL DEFAULT ''" +
                        ")" +
                        "ENGINE = InnoDB;");
                st.execute();
                st = connection.prepareStatement("CREATE TABLE programs (" +
                        "  PROGRAM_ID VARCHAR(45) NOT NULL DEFAULT ''," +
                        "  PROGRAM_NAME VARCHAR(45) NOT NULL DEFAULT ''," +
                        "  PROGRAM_TEXT LONGTEXT," +
                        "  PROGRAM_ARGS VARCHAR(45)," +
                        "  PROGRAM_LINK VARCHAR(150)," +
                        "  RUN_CONF VARCHAR(45) NOT NULL DEFAULT ''," +
                        "  PRIMARY KEY(PROGRAM_ID)" +
                        ")" +
                        "ENGINE = InnoDB;");
                st.execute();

                st = connection.prepareStatement("CREATE TABLE userprogramid (" +
                        "  USER_ID VARCHAR(45) NOT NULL DEFAULT ''," +
                        "  USER_TYPE VARCHAR(45) NOT NULL DEFAULT ''," +
                        "  PROGRAM_ID VARCHAR(45) NOT NULL DEFAULT ''" +
                        ")" +
                        "ENGINE = InnoDB;");
                st.execute();
                st = connection.prepareStatement("INSERT INTO databaseinfo (VERSION) VALUES (?)");
                st.setString(1, ServerSettings.DATABASE_VERSION);
                st.executeUpdate();
            }
            closeStatementAndResultSet(st, rs);
            return true;
        } catch (Throwable e) {
            ErrorWriter.writeErrorToConsole("Cannot create tables in database: " + "jdbc:mysql://" + ServerSettings.MYSQL_HOST + "/" + ServerSettings.MYSQL_DATABASE_NAME + "");
            ErrorWriter.ERROR_WRITER.writeExceptionToExceptionAnalyzer(e,
                    SessionInfo.TypeOfRequest.WORK_WITH_DATABASE.name(),
                    "jdbc:mysql://" + ServerSettings.MYSQL_HOST + "/" + ServerSettings.MYSQL_DATABASE_NAME + "");
            e.printStackTrace();
            return false;
        }
    }

    private boolean compareVersion() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = connection.prepareStatement("SHOW TABLES");
            rs = st.executeQuery();
            if (!checkIfDatabaseInfoExists(rs)) {
                st = connection.prepareStatement("CREATE TABLE databaseinfo (" +
                        "  VERSION VARCHAR(45)" +
                        ") " +
                        "ENGINE = InnoDB;");
                st.execute();
                System.out.println("Create table databaseInfo");
                st = connection.prepareStatement("INSERT databaseinfo (VERSION) SET VERSION=?");
                st.setString(1, ServerSettings.DATABASE_VERSION);
                st.executeUpdate();
                System.out.println("add database version");
                return false;
            }
            if (rs.next()) {
                st = connection.prepareStatement("SELECT * FROM databaseinfo");
                rs = st.executeQuery();
                if (rs.next()) {
                    String version = rs.getString("VERSION");
                    return version.equals(ServerSettings.DATABASE_VERSION);
                }
            }
        } catch (SQLException e) {
            ErrorWriter.ERROR_WRITER.writeExceptionToExceptionAnalyzer(e,
                    SessionInfo.TypeOfRequest.WORK_WITH_DATABASE.name(),
                    "Cannot read database version");
        } finally {
            closeStatementAndResultSet(st, rs);
        }
        return false;
    }

    private boolean checkIfDatabaseInfoExists(ResultSet rs) {
        try {
            while (rs.next()) {
                if (rs.getString(1).equals("databaseinfo")) {
                    return true;
                }
            }
            return false;
        } catch (SQLException e) {
            ErrorWriter.ERROR_WRITER.writeExceptionToExceptionAnalyzer(e,
                    SessionInfo.TypeOfRequest.WORK_WITH_DATABASE.name(),
                    "Cannot read database version");
            return false;
        }
    }

    private void checkDatabaseVersion() {
        if (!compareVersion()) {
            PreparedStatement st = null;
            try {
                st = connection.prepareStatement("UPDATE databaseinfo SET VERSION=?");
                st.setString(1, ServerSettings.DATABASE_VERSION);
                st.executeUpdate();

                //st = connection.prepareStatement("ALTER TABLE programs ADD COLUMN RUN_CONF VARCHAR(45) NOT NULL DEFAULT '' AFTER PROGRAM_LINK");
                //st.execute();
                //System.out.println("add column run_conf");
            } catch (SQLException e) {
                ErrorWriter.ERROR_WRITER.writeExceptionToExceptionAnalyzer(e,
                        SessionInfo.TypeOfRequest.WORK_WITH_DATABASE.name(),
                        "Cannot update database");
            } finally {
                closeStatement(st);
            }
        }
    }

    public static MySqlConnector getInstance() {
        return connector;
    }


    public boolean addNewUser(UserInfo userInfo) {
        if (!checkConnection()) {
            return false;
        }
        if (!findUser(userInfo)) {
            PreparedStatement st = null;
            try {
                st = connection.prepareStatement("INSERT INTO users (USER_ID, USER_TYPE, USER_NAME) VALUES (?, ?, ?)");
                st.setString(1, userInfo.getId());
                st.setString(2, userInfo.getType());
                st.setString(3, userInfo.getName());
                st.executeUpdate();
                return true;
            } catch (Throwable e) {
                ErrorWriter.ERROR_WRITER.writeExceptionToExceptionAnalyzer(e,
                        SessionInfo.TypeOfRequest.WORK_WITH_DATABASE.name(),
                        userInfo.getId() + " " + userInfo.getType() + " " + userInfo.getName());
            } finally {
                closeStatement(st);
            }
        }
        return false;
    }

    private void closeStatement(PreparedStatement st) {
        try {
            if (st != null) {
                st.close();
            }
        } catch (SQLException e) {
            ErrorWriter.ERROR_WRITER.writeExceptionToExceptionAnalyzer(e, SessionInfo.TypeOfRequest.WORK_WITH_DATABASE.name(), "null");
        }
    }

    public boolean findUser(UserInfo userInfo) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = connection.prepareStatement("SELECT * FROM users WHERE USER_ID=?");
            st.setString(1, userInfo.getId());
            rs = st.executeQuery();
            while (rs.next()) {
                if (rs.getString("USER_TYPE").equals(userInfo.getType())) {
                    return true;
                }
            }
        } catch (Throwable e) {
            ErrorWriter.ERROR_WRITER.writeExceptionToExceptionAnalyzer(e,
                    SessionInfo.TypeOfRequest.WORK_WITH_DATABASE.name(),
                    userInfo.getId() + " " + userInfo.getType() + " " + userInfo.getName());
        } finally {
            closeStatementAndResultSet(st, rs);
        }
        return false;

    }

    public String saveProgram(UserInfo userInfo, String programName, String programText, String args, String runConfiguration) {
        if (!checkConnection()) {
            return ResponseUtils.getJsonString("exception", "Cannot connect to database for save your program.");
        }
        PreparedStatement st = null;
        try {
            if (findUser(userInfo)) {

                if (!checkCountOfPrograms(userInfo)) {
                    return ResponseUtils.getJsonString("exception", "You can save only 100 programs");
                }
                if (findProgramByName(userInfo, programName)) {
                    return ResponseUtils.getJsonString("exception", "Program with same name already exists. Please choose the another one.");
                }

                String programId = userInfo.getId() + RandomUtils.nextInt();
                st = connection.prepareStatement(
                        "INSERT INTO programs (PROGRAM_ID, PROGRAM_NAME, PROGRAM_TEXT, PROGRAM_ARGS, PROGRAM_LINK, RUN_CONF) VALUES " +
                                "(?, ?, ?, ?, ?, ?)");
                st.setString(1, programId);
                st.setString(2, programName);
                st.setString(3, programText);
                st.setString(4, args);
                st.setString(5, "");
                st.setString(6, runConfiguration);
                st.executeUpdate();

                st = connection.prepareStatement("INSERT INTO userprogramid (USER_ID, USER_TYPE, PROGRAM_ID) VALUES " +
                        "(?, ?, ?)");
                st.setString(1, userInfo.getId());
                st.setString(2, userInfo.getType());
                st.setString(3, programId);
                st.executeUpdate();

                return ResponseUtils.getJsonString("programName", programName + "&id=" + programId);
            } else {
//                ErrorWriter.ERROR_WRITER.writeExceptionToExceptionAnalyzer(e, SessionInfo.TypeOfRequest.WORK_WITH_DATABASE.name(), url);
                ErrorWriterOnServer.LOG_FOR_EXCEPTIONS.error(ErrorWriter.getExceptionForLog(
                        SessionInfo.TypeOfRequest.SAVE_PROGRAM.name(), "Cannot find user at userIdUserInfo table",
                        userInfo.getId() + " " + userInfo.getType() + " " + userInfo.getName()));
                return ResponseUtils.getJsonString("exception", "Please, login.");
            }
        } catch (Throwable e) {
            if (e.getMessage().contains("Data too long")) {
                return ResponseUtils.getJsonString("exception", "Data is too long.");
            }
            ErrorWriter.ERROR_WRITER.writeExceptionToExceptionAnalyzer(e,
                    SessionInfo.TypeOfRequest.WORK_WITH_DATABASE.name(),
                    userInfo.getId() + " " + userInfo.getType() + " " + userInfo.getName());
            return ResponseUtils.getJsonString("exception", "Unknown error while saving your program");
        } finally {
            closeStatement(st);
        }
    }
    
    private String escape(String str) {
        return str.replaceAll("'", "\'");
    }

    public boolean checkCountOfPrograms(UserInfo userInfo) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = connection.prepareStatement("SELECT count(*) FROM users WHERE USER_ID=?");
            st.setString(1, userInfo.getId());
            rs = st.executeQuery();
            if (!rs.next()) {
                return false;
            }
            int count = Integer.parseInt(rs.getString("count(*)"));
            return count < 100;
        } catch (Throwable e) {
            ErrorWriter.ERROR_WRITER.writeExceptionToExceptionAnalyzer(e,
                    SessionInfo.TypeOfRequest.WORK_WITH_DATABASE.name(),
                    userInfo.getId() + " " + userInfo.getType() + " " + userInfo.getName());
            return false;
        } finally {
            closeStatementAndResultSet(st, rs);
        }
    }

    public String generatePublicLink(String programId) {
        if (!checkConnection()) {
            return ResponseUtils.getJsonString("exception", "Cannot connect to database for generate public link.");
        }
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = connection.prepareStatement("SELECT * FROM programs WHERE PROGRAM_ID=?");
            st.setString(1, programId);
            rs = st.executeQuery();
            if (!rs.next()) {
                return ResponseUtils.getJsonString("exception", "Cannot find the program.");
            }

            String publicLink = rs.getString("PROGRAM_LINK");
            if (publicLink == null || publicLink.isEmpty()) {
                publicLink = "http://" + ServerSettings.AUTH_REDIRECT + "/?publicLink=" + programId;
                st = connection.prepareStatement("UPDATE programs  SET PROGRAM_LINK=? WHERE PROGRAM_ID=?");
                st.setString(1, publicLink);
                st.setString(2, programId);
                st.executeUpdate();
            }

            return ResponseUtils.getJsonString("text", publicLink);
        } catch (Throwable e) {
            ErrorWriter.ERROR_WRITER.writeExceptionToExceptionAnalyzer(e, SessionInfo.TypeOfRequest.WORK_WITH_DATABASE.name(), programId);
            return ResponseUtils.getJsonString("exception", "Cannot generate public link");
        } finally {
            closeStatementAndResultSet(st, rs);
        }
    }

    public String getProgramTextByPublicLink(String programId) {
        if (!checkConnection()) {
            return ResponseUtils.getJsonString("exception", "Cannot connect to database for load program by link.");
        }
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = connection.prepareStatement("SELECT * FROM programs WHERE PROGRAM_ID=?");
            st.setString(1, programId);
            rs = st.executeQuery();
            if (!rs.next()) {
//                ErrorWriter.ERROR_WRITER.writeExceptionToExceptionAnalyzer(e, SessionInfo.TypeOfRequest.WORK_WITH_DATABASE.name(), url);
                ErrorWriterOnServer.LOG_FOR_EXCEPTIONS.error(ErrorWriter.getExceptionForLog(
                        SessionInfo.TypeOfRequest.SAVE_PROGRAM.name(), "Cannot find program by id in programIdProgramInfo",
                        programId));
                return ResponseUtils.getJsonString("exception", "Cannot find program by this link");
            }
            String link = rs.getString("PROGRAM_LINK");
            if (link == null || link.isEmpty()) {
                return ResponseUtils.getJsonString("exception", "Link for this program is not public");
            }

            JSONArray array = new JSONArray();
            Map<String, String> map = new HashMap<String, String>();
            map.put("type", "text");
            map.put("text", rs.getString("PROGRAM_TEXT"));
            String args = rs.getString("PROGRAM_ARGS");
            if (args == null) {
                args = "";
            }
            map.put("args", args);
            map.put("runCong", rs.getString("RUN_CONF"));
            array.put(map);
            return array.toString();

        } catch (Throwable e) {
            ErrorWriter.ERROR_WRITER.writeExceptionToExceptionAnalyzer(e, SessionInfo.TypeOfRequest.WORK_WITH_DATABASE.name(), programId);
            return ResponseUtils.getJsonString("exception", "Unknown error while loading program by link");
        } finally {
            closeStatementAndResultSet(st, rs);
        }
    }

    public String updateProgram(String programId, String programText, String args, String runConfiguration) {
        if (!checkConnection()) {
            return ResponseUtils.getJsonString("exception", "Cannot connect to database for save your program.");
        }
        PreparedStatement st = null;
        try {
            st = connection.prepareStatement("UPDATE programs SET PROGRAM_TEXT=? WHERE PROGRAM_ID=?");
            st.setString(1, programText);
            st.setString(2, programId);
            st.executeUpdate();

            st = connection.prepareStatement("UPDATE programs SET PROGRAM_ARGS=? WHERE PROGRAM_ID=?");
            st.setString(1, args);
            st.setString(2, programId);
            st.executeUpdate();

            st = connection.prepareStatement("UPDATE programs SET RUN_CONF=? WHERE PROGRAM_ID=?");
            st.setString(1, runConfiguration);
            st.setString(2, programId);
            st.executeUpdate();

            return ResponseUtils.getJsonString("programId", programId);
        } catch (Throwable e) {
            ErrorWriter.ERROR_WRITER.writeExceptionToExceptionAnalyzer(e, SessionInfo.TypeOfRequest.WORK_WITH_DATABASE.name(), programId);
            return ResponseUtils.getJsonString("exception", "Unknown error while saving your program");
        } finally {
            closeStatement(st);
        }
    }

    public boolean findProgramByName(UserInfo userInfo, String programName) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = connection.prepareStatement("SELECT * FROM programs WHERE PROGRAM_NAME=?");
            st.setString(1, programName);
            rs = st.executeQuery();

            ArrayList<String> programIds = new ArrayList<String>();
            while (rs.next()) {
                programIds.add(rs.getString("PROGRAM_ID"));
            }


            for (String programId : programIds) {
                st = connection.prepareStatement("SELECT * FROM userprogramid WHERE PROGRAM_ID=?");
                st.setString(1, programId);
                rs = st.executeQuery();
                if (!rs.next()) {
                    return false;
                }

                if (rs.getString("USER_ID").equals(userInfo.getId()) && rs.getString("USER_TYPE").equals(userInfo.getType())) {
                    return true;
                }
            }
            return false;
        } catch (Throwable e) {
            ErrorWriter.ERROR_WRITER.writeExceptionToExceptionAnalyzer(e,
                    SessionInfo.TypeOfRequest.WORK_WITH_DATABASE.name(),
                    userInfo.getId() + " " + userInfo.getType() + " " + userInfo.getName());
            return false;
        } finally {
            closeStatementAndResultSet(st, rs);
        }
    }

    public String getProgramText(String programId) {
        if (!checkConnection()) {
            return ResponseUtils.getJsonString("exception", "Cannot connect to database for load your program.");
        }
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = connection.prepareStatement("SELECT * FROM programs WHERE PROGRAM_ID=?");
            st.setString(1, programId);
            rs = st.executeQuery();
            if (!rs.next()) {
                return ResponseUtils.getJsonString("exception", "Cannot find the program.");
            }
            JSONArray array = new JSONArray();
            Map<String, String> map = new HashMap<String, String>();
            map.put("type", "text");
            map.put("text", rs.getString("PROGRAM_TEXT"));
            String args = rs.getString("PROGRAM_ARGS");
            if (args == null) {
                args = "";
            }
            map.put("args", args);
            map.put("runConf", rs.getString("RUN_CONF"));
            array.put(map);
            return array.toString();
        } catch (Throwable e) {
            ErrorWriter.ERROR_WRITER.writeExceptionToExceptionAnalyzer(e, SessionInfo.TypeOfRequest.WORK_WITH_DATABASE.name(), programId);
            return ResponseUtils.getJsonString("exception", "Unknown error while loading your program");
        } finally {
            closeStatementAndResultSet(st, rs);
        }
    }

    public String getListOfProgramsForUser(UserInfo userInfo) {
        if (!checkConnection()) {
            return ResponseUtils.getJsonString("exception", "Cannot connect to database to load list of your programs.");
        }
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = connection.prepareStatement("SELECT * FROM userprogramid WHERE USER_ID=?");
            st.setString(1, userInfo.getId());
            rs = st.executeQuery();
            JSONArray result = new JSONArray();
            ArrayList<String> programIds = new ArrayList<String>();
            while (rs.next()) {
                if (rs.getString("USER_TYPE").equals(userInfo.getType())) {
                    programIds.add(rs.getString("PROGRAM_ID"));
                }
            }

            st = connection.prepareStatement("SELECT * FROM programs WHERE PROGRAM_ID=?");
            for (String id : programIds) {
                st.setString(1, id);
                rs = st.executeQuery();
                if (!rs.next()) {
                    ErrorWriterOnServer.LOG_FOR_EXCEPTIONS.error(ErrorWriter.getExceptionForLog(
                            SessionInfo.TypeOfRequest.SAVE_PROGRAM.name(), "Cannot find program with in programs table",
                            userInfo.getId() + " " + userInfo.getType() + " " + userInfo.getName() + " " + id));
                    continue;
                }
                Map<String, String> map = new HashMap<String, String>();
                map.put("id", rs.getString("PROGRAM_ID"));
                map.put("name", rs.getString("PROGRAM_NAME"));
                result.put(map);
            }

            return result.toString();
        } catch (Throwable e) {
            ErrorWriter.ERROR_WRITER.writeExceptionToExceptionAnalyzer(e,
                    SessionInfo.TypeOfRequest.WORK_WITH_DATABASE.name(),
                    userInfo.getId() + " " + userInfo.getType() + " " + userInfo.getName());
            return ResponseUtils.getJsonString("exception", "Unknown error while loading list of your programs");
        } finally {
            closeStatementAndResultSet(st, rs);
        }
    }

    private void closeStatementAndResultSet(PreparedStatement st, ResultSet rs) {
        try {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            ErrorWriter.ERROR_WRITER.writeExceptionToExceptionAnalyzer(e, SessionInfo.TypeOfRequest.WORK_WITH_DATABASE.name(), "null");
        }
    }

    public String deleteProgram(UserInfo userInfo, String programId) {
        if (!checkConnection()) {
            return ResponseUtils.getJsonString("exception", "Cannot connect to database to delete your program.");
        }
        PreparedStatement st = null;
        try {
            if (findUser(userInfo)) {
                st = connection.prepareStatement("DELETE FROM programs WHERE PROGRAM_ID=?");
                st.setString(1, programId);
                st.executeUpdate();
                st = connection.prepareStatement("DELETE FROM userprogramid WHERE USER_ID=? AND USER_TYPE=? AND PROGRAM_ID=?");
                st.setString(1, userInfo.getId());
                st.setString(2, userInfo.getType());
                st.setString(3, programId);
                st.executeUpdate();
                return ResponseUtils.getJsonString("text", "Program was successfully deleted.", programId);
            } else {
//                ErrorWriter.ERROR_WRITER.writeExceptionToExceptionAnalyzer(e, SessionInfo.TypeOfRequest.WORK_WITH_DATABASE.name(), url);
                ErrorWriterOnServer.LOG_FOR_EXCEPTIONS.error(ErrorWriter.getExceptionForLog(
                        SessionInfo.TypeOfRequest.SAVE_PROGRAM.name(), "Cannot find user at userIdUserInfo table",
                        userInfo.getId() + " " + userInfo.getType() + " " + userInfo.getName()));
                return ResponseUtils.getJsonString("exception", "Unknown error while deleting your program");
            }
        } catch (Throwable e) {
            ErrorWriter.ERROR_WRITER.writeExceptionToExceptionAnalyzer(e, SessionInfo.TypeOfRequest.WORK_WITH_DATABASE.name(), userInfo.getId() + " " + userInfo.getType() + " " + userInfo.getName() + " " + programId);
            return ResponseUtils.getJsonString("exception", "Unknown error while deleting your program");
        } finally {
            closeStatement(st);
        }
    }


}
