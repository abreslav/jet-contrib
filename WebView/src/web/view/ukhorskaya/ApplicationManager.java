package web.view.ukhorskaya;

import org.apache.log4j.Logger;
import web.view.ukhorskaya.server.KotlinHttpServer;
import web.view.ukhorskaya.server.ServerSettings;

/**
 * Created by IntelliJ IDEA.
 * User: Natalia.Ukhorskaya
 * Date: 11/14/11
 * Time: 11:54 AM
 */

public class ApplicationManager {
    private static final Logger LOG = Logger.getLogger(ApplicationManager.class);
    
    public static void runCommand(String command) {
        if (command.equals("stop")) {
            KotlinHttpServer.stopServer();
        } else if (command.equals("exit")) {
            KotlinHttpServer.stopServer();
            System.exit(0);
        } else if (command.equals("restart")) {
            KotlinHttpServer.stopServer();
            KotlinHttpServer.startServer();
        } else if (command.equals("start")) {
            KotlinHttpServer.startServer();
        } else if (command.startsWith("output")) {
            ServerSettings.OUTPUT_DIRECTORY = ResponseUtils.substringAfter(command, "output ");
            ApplicationErrorsWriter.writeInfoToConsole("done: " + ServerSettings.OUTPUT_DIRECTORY);
        } else if (command.startsWith("get port")) {
            ApplicationErrorsWriter.writeInfoToConsole("server port " + KotlinHttpServer.getPort());
        } else if (command.startsWith("get host")) {
            ApplicationErrorsWriter.writeInfoToConsole("host: " + KotlinHttpServer.getHost());
        } else if (command.startsWith("timeout")) {
            ServerSettings.TIMEOUT_FOR_EXECUTION = ResponseUtils.substringAfter(command, "timeout ");
            ApplicationErrorsWriter.writeInfoToConsole("done: " + ServerSettings.TIMEOUT_FOR_EXECUTION);
        } else if (command.startsWith("kotlinLib")) {
            ServerSettings.PATH_TO_KOTLIN_LIB = ResponseUtils.substringAfter(command, "kotlinLib ");
            ApplicationErrorsWriter.writeInfoToConsole("done: " + ServerSettings.PATH_TO_KOTLIN_LIB);
        } else if (command.equals("help")) {
            StringBuilder builder = new StringBuilder("List of commands:\n");
            //builder.append("java_home pathToJavaHome - without \"\"\n");
            builder.append("timeout int - set maximum running time for user program\n");
            builder.append("output pathToOutputDir - without \"\", set directory to create a class files until compilation\n");
            builder.append("kotlinLib pathToKotlinLibDir - without \"\", set path to kotlin library jar files\n");
            //builder.append("host String - without \"\", set hostname for server\n");
            builder.append("get host - get host for server\n");
            //builder.append("port int - without \"\", set port for server\n");
            builder.append("get port - get port for server\n");
            builder.append("start - to start server after stop\n");
            builder.append("stop - to stop server\n");
            builder.append("exit - to stop server and exit application\n");
            builder.append("restart - to restart server\n");
            builder.append("help - help\n");
            ApplicationErrorsWriter.writeInfoToConsole(builder.toString());
        } else {
            ApplicationErrorsWriter.writeInfoToConsole("Incorrect command: help to look at all options");
        }
    }

    public static void setServerSetting(String setting) {
        if (setting.startsWith("java_home")) {
            ServerSettings.JAVA_HOME = ResponseUtils.substringAfter(setting, "java_home ");
        } else if (setting.startsWith("host")) {
            ServerSettings.HOST = ResponseUtils.substringAfter(setting, "host ");
        } else if (setting.startsWith("port")) {
            ServerSettings.PORT = ResponseUtils.substringAfter(setting, "port ");
        } else if (setting.startsWith("timeout")) {
            ServerSettings.TIMEOUT_FOR_EXECUTION = ResponseUtils.substringAfter(setting, "timeout ");
        } else if (setting.startsWith("output")) {
            ServerSettings.OUTPUT_DIRECTORY = ResponseUtils.substringAfter(setting, "output ");
        } else if (setting.startsWith("examples")) {
            ServerSettings.EXAMPLES_ROOT = ResponseUtils.substringAfter(setting, "examples ");
        } else {
            ApplicationErrorsWriter.writeErrorToConsole("Incorrect setting in config.properties file");
        }

    }
}
