package web.view.ukhorskaya;

import com.intellij.core.JavaCoreEnvironment;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.Function;
import org.apache.log4j.Logger;
import org.jetbrains.jet.compiler.CompileEnvironment;
import org.jetbrains.jet.lang.parsing.JetParserDefinition;
import org.jetbrains.jet.plugin.JetFileType;
import web.view.ukhorskaya.server.ServerSettings;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by IntelliJ IDEA.
 * User: Natalia.Ukhorskaya
 * Date: 10/14/11
 * Time: 3:49 PM
 */
public class Initializer {
    private static final Logger LOG = Logger.getLogger(Initializer.class);
    private static Initializer initializer = new Initializer();

    public static Initializer getInstance() {
        return initializer;
    }

    private Initializer() {
    }

    private static JavaCoreEnvironment environment;

    public static JavaCoreEnvironment getEnvironment() {
        if (environment != null) {
            return environment;
        }
        ErrorsWriter.LOG_FOR_EXCEPTIONS.error(ErrorsWriter.getExceptionForLog("initialize", "JavaCoreEnvironment is null.", "null"));
        return null;
    }

    public boolean setJavaCoreEnvironment() {
        File rtJar = findRtJar();
        if (rtJar == null) return false;
        environment.addToClasspath(rtJar);
        if (!initializeKotlinRuntime()) {
            ErrorsWriter.writeInfoToConsole("Cannot found Kotlin Runtime library.");
        }
        environment.registerFileType(JetFileType.INSTANCE, "kt");
        environment.registerFileType(JetFileType.INSTANCE, "kts");
        environment.registerFileType(JetFileType.INSTANCE, "ktm");
        environment.registerFileType(JetFileType.INSTANCE, "jet");
        environment.registerParserDefinition(new JetParserDefinition());
        return true;
    }

    public boolean initializeKotlinRuntime() {
        final File unpackedRuntimePath = getUnpackedRuntimePath();
        if (unpackedRuntimePath != null) {
            ServerSettings.PATH_TO_KOTLIN_LIB = unpackedRuntimePath.getAbsolutePath();
            ErrorsWriter.writeInfoToConsole("Kotlin Runtime library found at " + ServerSettings.PATH_TO_KOTLIN_LIB);
            environment.addToClasspath(unpackedRuntimePath);
        } else {
            final File runtimeJarPath = getRuntimeJarPath();
            if (runtimeJarPath != null && runtimeJarPath.exists()) {
                environment.addToClasspath(runtimeJarPath);
                ServerSettings.PATH_TO_KOTLIN_LIB = runtimeJarPath.getAbsolutePath();
                ErrorsWriter.writeInfoToConsole("Kotlin Runtime library found at " + ServerSettings.PATH_TO_KOTLIN_LIB);
            } else {
                return false;
            }
        }
        return true;
    }

    public static File getUnpackedRuntimePath() {
        URL url = CompileEnvironment.class.getClassLoader().getResource("jet/JetObject.class");
        if (url != null && url.getProtocol().equals("file")) {
            return new File(url.getPath()).getParentFile().getParentFile();
        }
        return null;
    }

    public static File getRuntimeJarPath() {
        URL url = CompileEnvironment.class.getClassLoader().getResource("jet/JetObject.class");
        if (url != null && url.getProtocol().equals("jar")) {
            String path = url.getPath();
            return new File(path.substring(path.indexOf(":") + 1, path.indexOf("!/")));
        }
        return null;
    }

    public boolean initJavaCoreEnvironment() {
        if (environment == null) {
            System.setProperty("java.awt.headless", "true");

            Disposable root = new Disposable() {
                @Override
                public void dispose() {
                }
            };
            environment = new JavaCoreEnvironment(root);

            return setJavaCoreEnvironment();
        }
        LOG.error("JavaCoreEnvironment is already initialized.");
        return false;
    }

    private File findRtJar() {
        String java_home = ServerSettings.JAVA_HOME;
        LOG.info("java_home " + ServerSettings.JAVA_HOME + " " + java_home);
        File rtJar;
        if (java_home != null) {
            rtJar = findRtJar(java_home);
            if (rtJar == null) {
                rtJar = findActiveRtJar(true);
            }
        } else {
            rtJar = findActiveRtJar(true);
        }

        if ((rtJar == null || !rtJar.exists())) {
            if (java_home == null) {
                ErrorsWriter.writeInfoToConsole("You can set java_home variable at config.properties file.");
            } else {
                ErrorsWriter.writeErrorToConsole("No rt.jar found under JAVA_HOME=" + java_home);
            }
            return null;
        }
        return rtJar;
    }

    private File findRtJar(String javaHome) {
        File rtJar = new File(javaHome, "jre" + File.separatorChar + "lib" + File.separatorChar + "rt.jar");
        LOG.info(rtJar.getAbsolutePath() + " ex " + rtJar.exists());
        if (rtJar.exists()) {
            return rtJar;
        }
        return null;
    }

    private File findActiveRtJar(boolean failOnError) {
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        if (systemClassLoader instanceof URLClassLoader) {
            URLClassLoader loader = (URLClassLoader) systemClassLoader;
            for (URL url : loader.getURLs()) {
                if ("file".equals(url.getProtocol())) {
                    LOG.info(url.getFile());
                    if (url.getFile().endsWith("/lib/rt.jar")) {
                        return new File(url.getFile().replaceAll("%20", " "));
                    }
                    if (url.getFile().endsWith("/Classes/classes.jar")) {
                        return new File(url.getFile().replaceAll("%20", " ")).getAbsoluteFile();
                    }
                }
            }
            if (failOnError) {
                ErrorsWriter.writeErrorToConsole("Could not find rt.jar in system class loader: " + StringUtil.join(loader.getURLs(), new Function<URL, String>() {
                    @Override
                    public String fun(URL url) {
                        return url.toString();
                    }
                }, ", "));
            }
        } else if (failOnError) {
            ErrorsWriter.writeErrorToConsole("System class loader is not an URLClassLoader: " + systemClassLoader);
        }
        return null;
    }
}


