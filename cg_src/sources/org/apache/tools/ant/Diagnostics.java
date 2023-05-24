package org.apache.tools.ant;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.Calendar;
import java.util.Objects;
import java.util.Properties;
import java.util.TimeZone;
import java.util.stream.Stream;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import org.apache.tools.ant.launch.Launcher;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.JAXPUtils;
import org.apache.tools.ant.util.JavaEnvUtils;
import org.apache.tools.ant.util.ProxySetup;
import org.apache.tools.ant.util.java15.ProxyDiagnostics;
import org.xml.sax.XMLReader;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/Diagnostics.class */
public final class Diagnostics {
    private static final int BIG_DRIFT_LIMIT = 10000;
    private static final int TEST_FILE_SIZE = 32;
    private static final int KILOBYTE = 1024;
    private static final int SECONDS_PER_MILLISECOND = 1000;
    private static final int SECONDS_PER_MINUTE = 60;
    private static final int MINUTES_PER_HOUR = 60;
    protected static final String ERROR_PROPERTY_ACCESS_BLOCKED = "Access to this property blocked by a security manager";

    private Diagnostics() {
    }

    @Deprecated
    public static boolean isOptionalAvailable() {
        return true;
    }

    @Deprecated
    public static void validateVersion() throws BuildException {
    }

    public static File[] listLibraries() {
        String home = System.getProperty("ant.home");
        if (home == null) {
            return null;
        }
        return listJarFiles(new File(home, Launcher.ANT_PRIVATELIB));
    }

    private static File[] listJarFiles(File libDir) {
        return libDir.listFiles(dir, name -> {
            return name.endsWith(".jar");
        });
    }

    public static void main(String[] args) {
        doReport(System.out);
    }

    private static String getImplementationVersion(Class<?> clazz) {
        return clazz.getPackage().getImplementationVersion();
    }

    private static URL getClassLocation(Class<?> clazz) {
        if (clazz.getProtectionDomain().getCodeSource() == null) {
            return null;
        }
        return clazz.getProtectionDomain().getCodeSource().getLocation();
    }

    private static String getXMLParserName() {
        SAXParser saxParser = getSAXParser();
        if (saxParser == null) {
            return "Could not create an XML Parser";
        }
        return saxParser.getClass().getName();
    }

    private static String getXSLTProcessorName() {
        Transformer transformer = getXSLTProcessor();
        if (transformer == null) {
            return "Could not create an XSLT Processor";
        }
        return transformer.getClass().getName();
    }

    private static SAXParser getSAXParser() {
        try {
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = null;
            try {
                saxParser = saxParserFactory.newSAXParser();
            } catch (Exception e) {
                ignoreThrowable(e);
            }
            return saxParser;
        } catch (Exception e2) {
            ignoreThrowable(e2);
            return null;
        }
    }

    private static Transformer getXSLTProcessor() {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        if (transformerFactory != null) {
            try {
                return transformerFactory.newTransformer();
            } catch (Exception e) {
                ignoreThrowable(e);
                return null;
            }
        }
        return null;
    }

    private static String getXMLParserLocation() {
        URL location;
        SAXParser saxParser = getSAXParser();
        if (saxParser == null || (location = getClassLocation(saxParser.getClass())) == null) {
            return null;
        }
        return location.toString();
    }

    private static String getNamespaceParserName() {
        try {
            XMLReader reader = JAXPUtils.getNamespaceXMLReader();
            return reader.getClass().getName();
        } catch (BuildException e) {
            ignoreThrowable(e);
            return null;
        }
    }

    private static String getNamespaceParserLocation() {
        try {
            XMLReader reader = JAXPUtils.getNamespaceXMLReader();
            URL location = getClassLocation(reader.getClass());
            if (location != null) {
                return location.toString();
            }
            return null;
        } catch (BuildException e) {
            ignoreThrowable(e);
            return null;
        }
    }

    private static String getXSLTProcessorLocation() {
        URL location;
        Transformer transformer = getXSLTProcessor();
        if (transformer == null || (location = getClassLocation(transformer.getClass())) == null) {
            return null;
        }
        return location.toString();
    }

    private static void ignoreThrowable(Throwable thrown) {
    }

    public static void doReport(PrintStream out) {
        doReport(out, 2);
    }

    public static void doReport(PrintStream out, int logLevel) {
        out.println("------- Ant diagnostics report -------");
        out.println(Main.getAntVersion());
        header(out, "Implementation Version");
        out.println("core tasks     : " + getImplementationVersion(Main.class) + " in " + getClassLocation(Main.class));
        header(out, "ANT PROPERTIES");
        doReportAntProperties(out);
        header(out, "ANT_HOME/lib jar listing");
        doReportAntHomeLibraries(out);
        header(out, "USER_HOME/.ant/lib jar listing");
        doReportUserHomeLibraries(out);
        header(out, "Tasks availability");
        doReportTasksAvailability(out);
        header(out, "org.apache.env.Which diagnostics");
        doReportWhich(out);
        header(out, "XML Parser information");
        doReportParserInfo(out);
        header(out, "XSLT Processor information");
        doReportXSLTProcessorInfo(out);
        header(out, "System properties");
        doReportSystemProperties(out);
        header(out, "Temp dir");
        doReportTempDir(out);
        header(out, "Locale information");
        doReportLocale(out);
        header(out, "Proxy information");
        doReportProxy(out);
        out.println();
    }

    private static void header(PrintStream out, String section) {
        out.println();
        out.println("-------------------------------------------");
        out.print(Instruction.argsep);
        out.println(section);
        out.println("-------------------------------------------");
    }

    private static void doReportSystemProperties(PrintStream out) {
        try {
            Properties sysprops = System.getProperties();
            Stream<R> map = sysprops.stringPropertyNames().stream().map(key -> {
                return key + " : " + getProperty(key);
            });
            Objects.requireNonNull(out);
            map.forEach(this::println);
        } catch (SecurityException e) {
            ignoreThrowable(e);
            out.println("Access to System.getProperties() blocked by a security manager");
        }
    }

    private static String getProperty(String key) {
        String value;
        try {
            value = System.getProperty(key);
        } catch (SecurityException e) {
            value = ERROR_PROPERTY_ACCESS_BLOCKED;
        }
        return value;
    }

    private static void doReportAntProperties(PrintStream out) {
        Project p = new Project();
        p.initProperties();
        out.println("ant.version: " + p.getProperty(MagicNames.ANT_VERSION));
        out.println("ant.java.version: " + p.getProperty(MagicNames.ANT_JAVA_VERSION));
        out.println("Is this the Apache Harmony VM? " + (JavaEnvUtils.isApacheHarmony() ? "yes" : "no"));
        out.println("Is this the Kaffe VM? " + (JavaEnvUtils.isKaffe() ? "yes" : "no"));
        out.println("Is this gij/gcj? " + (JavaEnvUtils.isGij() ? "yes" : "no"));
        out.println("ant.core.lib: " + p.getProperty(MagicNames.ANT_LIB));
        out.println("ant.home: " + p.getProperty("ant.home"));
    }

    private static void doReportAntHomeLibraries(PrintStream out) {
        out.println("ant.home: " + System.getProperty("ant.home"));
        printLibraries(listLibraries(), out);
    }

    private static void doReportUserHomeLibraries(PrintStream out) {
        String home = System.getProperty(Launcher.USER_HOMEDIR);
        out.println("user.home: " + home);
        File libDir = new File(home, Launcher.USER_LIBDIR);
        printLibraries(listJarFiles(libDir), out);
    }

    private static void printLibraries(File[] libs, PrintStream out) {
        if (libs == null) {
            out.println("No such directory.");
            return;
        }
        for (File lib : libs) {
            out.println(lib.getName() + " (" + lib.length() + " bytes)");
        }
    }

    private static void doReportWhich(PrintStream out) {
        Throwable error = null;
        try {
            Class<?> which = Class.forName("org.apache.env.Which");
            Method method = which.getMethod("main", String[].class);
            method.invoke(null, new String[0]);
        } catch (ClassNotFoundException e) {
            out.println("Not available.");
            out.println("Download it at https://xml.apache.org/commons/");
        } catch (InvocationTargetException e2) {
            error = e2.getTargetException() == null ? e2 : e2.getTargetException();
        } catch (Throwable e3) {
            error = e3;
        }
        if (error != null) {
            out.println("Error while running org.apache.env.Which");
            error.printStackTrace(out);
        }
    }

    private static void doReportTasksAvailability(PrintStream out) {
        InputStream is = Main.class.getResourceAsStream(MagicNames.TASKDEF_PROPERTIES_RESOURCE);
        if (is == null) {
            out.println("None available");
            return;
        }
        Properties props = new Properties();
        try {
            props.load(is);
            for (String key : props.stringPropertyNames()) {
                String classname = props.getProperty(key);
                try {
                    try {
                        try {
                            Class.forName(classname);
                            props.remove(key);
                        } catch (LinkageError e) {
                            out.println(key + " : Initialization error");
                        }
                    } catch (ClassNotFoundException e2) {
                        out.println(key + " : Not Available (the implementation class is not present)");
                    }
                } catch (NoClassDefFoundError e3) {
                    String pkg = e3.getMessage().replace('/', '.');
                    out.println(key + " : Missing dependency " + pkg);
                }
            }
            if (props.size() == 0) {
                out.println("All defined tasks are available");
            } else {
                out.println("A task being missing/unavailable should only matter if you are trying to use it");
            }
        } catch (IOException e4) {
            out.println(e4.getMessage());
        }
    }

    private static void doReportParserInfo(PrintStream out) {
        String parserName = getXMLParserName();
        String parserLocation = getXMLParserLocation();
        printParserInfo(out, "XML Parser", parserName, parserLocation);
        printParserInfo(out, "Namespace-aware parser", getNamespaceParserName(), getNamespaceParserLocation());
    }

    private static void doReportXSLTProcessorInfo(PrintStream out) {
        String processorName = getXSLTProcessorName();
        String processorLocation = getXSLTProcessorLocation();
        printParserInfo(out, "XSLT Processor", processorName, processorLocation);
    }

    private static void printParserInfo(PrintStream out, String parserType, String parserName, String parserLocation) {
        if (parserName == null) {
            parserName = "unknown";
        }
        if (parserLocation == null) {
            parserLocation = "unknown";
        }
        out.println(parserType + " : " + parserName);
        out.println(parserType + " Location: " + parserLocation);
    }

    private static void doReportTempDir(PrintStream out) {
        String tempdir = System.getProperty("java.io.tmpdir");
        if (tempdir == null) {
            out.println("Warning: java.io.tmpdir is undefined");
            return;
        }
        out.println("Temp dir is " + tempdir);
        File tempDirectory = new File(tempdir);
        if (!tempDirectory.exists()) {
            out.println("Warning, java.io.tmpdir directory does not exist: " + tempdir);
            return;
        }
        long now = System.currentTimeMillis();
        File tempFile = null;
        OutputStream fileout = null;
        InputStream filein = null;
        try {
            try {
                tempFile = File.createTempFile("diag", "txt", tempDirectory);
                OutputStream fileout2 = Files.newOutputStream(tempFile.toPath(), new OpenOption[0]);
                byte[] buffer = new byte[1024];
                for (int i = 0; i < 32; i++) {
                    fileout2.write(buffer);
                }
                fileout2.close();
                fileout = null;
                Thread.sleep(1000L);
                InputStream filein2 = Files.newInputStream(tempFile.toPath(), new OpenOption[0]);
                int total = 0;
                while (true) {
                    int read = filein2.read(buffer, 0, 1024);
                    if (read <= 0) {
                        break;
                    }
                    total += read;
                }
                filein2.close();
                filein = null;
                long filetime = tempFile.lastModified();
                long drift = filetime - now;
                tempFile.delete();
                out.print("Temp dir is writeable");
                if (total != 32768) {
                    out.println(", but seems to be full.  Wrote 32768but could only read " + total + " bytes.");
                } else {
                    out.println();
                }
                out.println("Temp dir alignment with system clock is " + drift + " ms");
                if (Math.abs(drift) > 10000) {
                    out.println("Warning: big clock drift -maybe a network filesystem");
                }
                FileUtils.close((OutputStream) null);
                FileUtils.close((InputStream) null);
                if (tempFile == null || !tempFile.exists()) {
                    return;
                }
                tempFile.delete();
            } catch (IOException e) {
                ignoreThrowable(e);
                out.println("Failed to create a temporary file in the temp dir " + tempdir);
                out.println("File  " + tempFile + " could not be created/written to");
                FileUtils.close(fileout);
                FileUtils.close(filein);
                if (tempFile == null || !tempFile.exists()) {
                    return;
                }
                tempFile.delete();
            } catch (InterruptedException e2) {
                ignoreThrowable(e2);
                out.println("Failed to check whether tempdir is writable");
                FileUtils.close(fileout);
                FileUtils.close(filein);
                if (tempFile == null || !tempFile.exists()) {
                    return;
                }
                tempFile.delete();
            }
        } catch (Throwable th) {
            FileUtils.close(fileout);
            FileUtils.close(filein);
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
            throw th;
        }
    }

    private static void doReportLocale(PrintStream out) {
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        out.println("Timezone " + tz.getDisplayName() + " offset=" + tz.getOffset(cal.get(0), cal.get(1), cal.get(2), cal.get(5), cal.get(7), (((((cal.get(11) * 60) + cal.get(12)) * 60) + cal.get(13)) * 1000) + cal.get(14)));
    }

    private static void printProperty(PrintStream out, String key) {
        String value = getProperty(key);
        if (value != null) {
            out.print(key);
            out.print(" = ");
            out.print('\"');
            out.print(value);
            out.println('\"');
        }
    }

    private static void doReportProxy(PrintStream out) {
        printProperty(out, ProxySetup.HTTP_PROXY_HOST);
        printProperty(out, ProxySetup.HTTP_PROXY_PORT);
        printProperty(out, ProxySetup.HTTP_PROXY_USERNAME);
        printProperty(out, ProxySetup.HTTP_PROXY_PASSWORD);
        printProperty(out, ProxySetup.HTTP_NON_PROXY_HOSTS);
        printProperty(out, ProxySetup.HTTPS_PROXY_HOST);
        printProperty(out, ProxySetup.HTTPS_PROXY_PORT);
        printProperty(out, ProxySetup.HTTPS_NON_PROXY_HOSTS);
        printProperty(out, ProxySetup.FTP_PROXY_HOST);
        printProperty(out, ProxySetup.FTP_PROXY_PORT);
        printProperty(out, ProxySetup.FTP_NON_PROXY_HOSTS);
        printProperty(out, ProxySetup.SOCKS_PROXY_HOST);
        printProperty(out, ProxySetup.SOCKS_PROXY_PORT);
        printProperty(out, ProxySetup.SOCKS_PROXY_USERNAME);
        printProperty(out, ProxySetup.SOCKS_PROXY_PASSWORD);
        printProperty(out, ProxySetup.USE_SYSTEM_PROXIES);
        ProxyDiagnostics proxyDiag = new ProxyDiagnostics();
        out.println("Java1.5+ proxy settings:");
        out.println(proxyDiag.toString());
    }
}
