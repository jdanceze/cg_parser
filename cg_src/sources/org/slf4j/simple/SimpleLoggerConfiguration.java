package org.slf4j.simple;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.security.AccessController;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import org.slf4j.helpers.Util;
import org.slf4j.simple.OutputChoice;
import polyglot.main.Report;
/* loaded from: gencallgraphv3.jar:slf4j-simple-2.0.3.jar:org/slf4j/simple/SimpleLoggerConfiguration.class */
public class SimpleLoggerConfiguration {
    private static final String CONFIGURATION_FILE = "simplelogger.properties";
    private static final boolean SHOW_DATE_TIME_DEFAULT = false;
    private static final boolean SHOW_THREAD_NAME_DEFAULT = true;
    private static final boolean SHOW_THREAD_ID_DEFAULT = false;
    static final boolean SHOW_LOG_NAME_DEFAULT = true;
    private static final boolean SHOW_SHORT_LOG_NAME_DEFAULT = false;
    private static final boolean LEVEL_IN_BRACKETS_DEFAULT = false;
    private static final String LOG_FILE_DEFAULT = "System.err";
    private static final boolean CACHE_OUTPUT_STREAM_DEFAULT = false;
    private static final String WARN_LEVELS_STRING_DEFAULT = "WARN";
    static int DEFAULT_LOG_LEVEL_DEFAULT = 20;
    private static final String DATE_TIME_FORMAT_STR_DEFAULT = null;
    private static String dateTimeFormatStr = DATE_TIME_FORMAT_STR_DEFAULT;
    int defaultLogLevel = DEFAULT_LOG_LEVEL_DEFAULT;
    boolean showDateTime = false;
    DateFormat dateFormatter = null;
    boolean showThreadName = true;
    boolean showThreadId = false;
    boolean showLogName = true;
    boolean showShortLogName = false;
    boolean levelInBrackets = false;
    private String logFile = LOG_FILE_DEFAULT;
    OutputChoice outputChoice = null;
    private boolean cacheOutputStream = false;
    String warnLevelString = WARN_LEVELS_STRING_DEFAULT;
    private final Properties properties = new Properties();

    /* JADX INFO: Access modifiers changed from: package-private */
    public void init() {
        loadProperties();
        String defaultLogLevelString = getStringProperty("org.slf4j.simpleLogger.defaultLogLevel", null);
        if (defaultLogLevelString != null) {
            this.defaultLogLevel = stringToLevel(defaultLogLevelString);
        }
        this.showLogName = getBooleanProperty("org.slf4j.simpleLogger.showLogName", true);
        this.showShortLogName = getBooleanProperty("org.slf4j.simpleLogger.showShortLogName", false);
        this.showDateTime = getBooleanProperty("org.slf4j.simpleLogger.showDateTime", false);
        this.showThreadName = getBooleanProperty("org.slf4j.simpleLogger.showThreadName", true);
        this.showThreadId = getBooleanProperty(SimpleLogger.SHOW_THREAD_ID_KEY, false);
        dateTimeFormatStr = getStringProperty("org.slf4j.simpleLogger.dateTimeFormat", DATE_TIME_FORMAT_STR_DEFAULT);
        this.levelInBrackets = getBooleanProperty("org.slf4j.simpleLogger.levelInBrackets", false);
        this.warnLevelString = getStringProperty("org.slf4j.simpleLogger.warnLevelString", WARN_LEVELS_STRING_DEFAULT);
        this.logFile = getStringProperty("org.slf4j.simpleLogger.logFile", this.logFile);
        this.cacheOutputStream = getBooleanProperty(SimpleLogger.CACHE_OUTPUT_STREAM_STRING_KEY, false);
        this.outputChoice = computeOutputChoice(this.logFile, this.cacheOutputStream);
        if (dateTimeFormatStr != null) {
            try {
                this.dateFormatter = new SimpleDateFormat(dateTimeFormatStr);
            } catch (IllegalArgumentException e) {
                Util.report("Bad date format in simplelogger.properties; will output relative time", e);
            }
        }
    }

    private void loadProperties() {
        InputStream in = (InputStream) AccessController.doPrivileged(() -> {
            ClassLoader threadCL = Thread.currentThread().getContextClassLoader();
            if (threadCL != null) {
                return threadCL.getResourceAsStream(CONFIGURATION_FILE);
            }
            return ClassLoader.getSystemResourceAsStream(CONFIGURATION_FILE);
        });
        if (null != in) {
            try {
                this.properties.load(in);
                try {
                    in.close();
                } catch (IOException e) {
                }
            } catch (IOException e2) {
                try {
                    in.close();
                } catch (IOException e3) {
                }
            } catch (Throwable th) {
                try {
                    in.close();
                } catch (IOException e4) {
                }
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getStringProperty(String name, String defaultValue) {
        String prop = getStringProperty(name);
        return prop == null ? defaultValue : prop;
    }

    boolean getBooleanProperty(String name, boolean defaultValue) {
        String prop = getStringProperty(name);
        return prop == null ? defaultValue : "true".equalsIgnoreCase(prop);
    }

    String getStringProperty(String name) {
        String prop = null;
        try {
            prop = System.getProperty(name);
        } catch (SecurityException e) {
        }
        return prop == null ? this.properties.getProperty(name) : prop;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int stringToLevel(String levelStr) {
        if ("trace".equalsIgnoreCase(levelStr)) {
            return 0;
        }
        if (Report.debug.equalsIgnoreCase(levelStr)) {
            return 10;
        }
        if ("info".equalsIgnoreCase(levelStr)) {
            return 20;
        }
        if ("warn".equalsIgnoreCase(levelStr)) {
            return 30;
        }
        if ("error".equalsIgnoreCase(levelStr)) {
            return 40;
        }
        if ("off".equalsIgnoreCase(levelStr)) {
            return 50;
        }
        return 20;
    }

    private static OutputChoice computeOutputChoice(String logFile, boolean cacheOutputStream) {
        if (LOG_FILE_DEFAULT.equalsIgnoreCase(logFile)) {
            if (cacheOutputStream) {
                return new OutputChoice(OutputChoice.OutputChoiceType.CACHED_SYS_ERR);
            }
            return new OutputChoice(OutputChoice.OutputChoiceType.SYS_ERR);
        } else if ("System.out".equalsIgnoreCase(logFile)) {
            if (cacheOutputStream) {
                return new OutputChoice(OutputChoice.OutputChoiceType.CACHED_SYS_OUT);
            }
            return new OutputChoice(OutputChoice.OutputChoiceType.SYS_OUT);
        } else {
            try {
                FileOutputStream fos = new FileOutputStream(logFile);
                PrintStream printStream = new PrintStream(fos);
                return new OutputChoice(printStream);
            } catch (FileNotFoundException e) {
                Util.report("Could not open [" + logFile + "]. Defaulting to System.err", e);
                return new OutputChoice(OutputChoice.OutputChoiceType.SYS_ERR);
            }
        }
    }
}
