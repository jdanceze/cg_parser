package org.slf4j.impl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import org.apache.http.client.methods.HttpTrace;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.helpers.MessageFormatter;
import org.slf4j.helpers.Util;
import polyglot.main.Report;
/* loaded from: gencallgraphv3.jar:slf4j-simple-1.7.5.jar:org/slf4j/impl/SimpleLogger.class */
public class SimpleLogger extends MarkerIgnoringBase {
    private static final long serialVersionUID = -632788891211436180L;
    private static final String CONFIGURATION_FILE = "simplelogger.properties";
    private static final int LOG_LEVEL_TRACE = 0;
    private static final int LOG_LEVEL_DEBUG = 10;
    private static final int LOG_LEVEL_INFO = 20;
    private static final int LOG_LEVEL_WARN = 30;
    private static final int LOG_LEVEL_ERROR = 40;
    public static final String SYSTEM_PREFIX = "org.slf4j.simpleLogger.";
    public static final String DEFAULT_LOG_LEVEL_KEY = "org.slf4j.simpleLogger.defaultLogLevel";
    public static final String SHOW_DATE_TIME_KEY = "org.slf4j.simpleLogger.showDateTime";
    public static final String DATE_TIME_FORMAT_KEY = "org.slf4j.simpleLogger.dateTimeFormat";
    public static final String SHOW_THREAD_NAME_KEY = "org.slf4j.simpleLogger.showThreadName";
    public static final String SHOW_LOG_NAME_KEY = "org.slf4j.simpleLogger.showLogName";
    public static final String SHOW_SHORT_LOG_NAME_KEY = "org.slf4j.simpleLogger.showShortLogName";
    public static final String LOG_FILE_KEY = "org.slf4j.simpleLogger.logFile";
    public static final String LEVEL_IN_BRACKETS_KEY = "org.slf4j.simpleLogger.levelInBrackets";
    public static final String WARN_LEVEL_STRING_KEY = "org.slf4j.simpleLogger.warnLevelString";
    public static final String LOG_KEY_PREFIX = "org.slf4j.simpleLogger.log.";
    protected int currentLogLevel;
    private transient String shortLogName = null;
    private static long START_TIME = System.currentTimeMillis();
    private static final Properties SIMPLE_LOGGER_PROPS = new Properties();
    private static boolean INITIALIZED = false;
    private static int DEFAULT_LOG_LEVEL = 20;
    private static boolean SHOW_DATE_TIME = false;
    private static String DATE_TIME_FORMAT_STR = null;
    private static DateFormat DATE_FORMATTER = null;
    private static boolean SHOW_THREAD_NAME = true;
    private static boolean SHOW_LOG_NAME = true;
    private static boolean SHOW_SHORT_LOG_NAME = false;
    private static String LOG_FILE = "System.err";
    private static PrintStream TARGET_STREAM = null;
    private static boolean LEVEL_IN_BRACKETS = false;
    private static String WARN_LEVEL_STRING = "WARN";

    private static String getStringProperty(String name) {
        String prop = null;
        try {
            prop = System.getProperty(name);
        } catch (SecurityException e) {
        }
        return prop == null ? SIMPLE_LOGGER_PROPS.getProperty(name) : prop;
    }

    private static String getStringProperty(String name, String defaultValue) {
        String prop = getStringProperty(name);
        return prop == null ? defaultValue : prop;
    }

    private static boolean getBooleanProperty(String name, boolean defaultValue) {
        String prop = getStringProperty(name);
        return prop == null ? defaultValue : "true".equalsIgnoreCase(prop);
    }

    static void init() {
        INITIALIZED = true;
        loadProperties();
        String defaultLogLevelString = getStringProperty("org.slf4j.simpleLogger.defaultLogLevel", null);
        if (defaultLogLevelString != null) {
            DEFAULT_LOG_LEVEL = stringToLevel(defaultLogLevelString);
        }
        SHOW_LOG_NAME = getBooleanProperty("org.slf4j.simpleLogger.showLogName", SHOW_LOG_NAME);
        SHOW_SHORT_LOG_NAME = getBooleanProperty("org.slf4j.simpleLogger.showShortLogName", SHOW_SHORT_LOG_NAME);
        SHOW_DATE_TIME = getBooleanProperty("org.slf4j.simpleLogger.showDateTime", SHOW_DATE_TIME);
        SHOW_THREAD_NAME = getBooleanProperty("org.slf4j.simpleLogger.showThreadName", SHOW_THREAD_NAME);
        DATE_TIME_FORMAT_STR = getStringProperty("org.slf4j.simpleLogger.dateTimeFormat", DATE_TIME_FORMAT_STR);
        LEVEL_IN_BRACKETS = getBooleanProperty("org.slf4j.simpleLogger.levelInBrackets", LEVEL_IN_BRACKETS);
        WARN_LEVEL_STRING = getStringProperty("org.slf4j.simpleLogger.warnLevelString", WARN_LEVEL_STRING);
        LOG_FILE = getStringProperty("org.slf4j.simpleLogger.logFile", LOG_FILE);
        TARGET_STREAM = computeTargetStream(LOG_FILE);
        if (DATE_TIME_FORMAT_STR != null) {
            try {
                DATE_FORMATTER = new SimpleDateFormat(DATE_TIME_FORMAT_STR);
            } catch (IllegalArgumentException e) {
                Util.report("Bad date format in simplelogger.properties; will output relative time", e);
            }
        }
    }

    private static PrintStream computeTargetStream(String logFile) {
        if ("System.err".equalsIgnoreCase(logFile)) {
            return System.err;
        }
        if ("System.out".equalsIgnoreCase(logFile)) {
            return System.out;
        }
        try {
            FileOutputStream fos = new FileOutputStream(logFile);
            PrintStream printStream = new PrintStream(fos);
            return printStream;
        } catch (FileNotFoundException e) {
            Util.report("Could not open [" + logFile + "]. Defaulting to System.err", e);
            return System.err;
        }
    }

    private static void loadProperties() {
        InputStream in = (InputStream) AccessController.doPrivileged(new PrivilegedAction() { // from class: org.slf4j.impl.SimpleLogger.1
            @Override // java.security.PrivilegedAction
            public Object run() {
                ClassLoader threadCL = Thread.currentThread().getContextClassLoader();
                if (threadCL != null) {
                    return threadCL.getResourceAsStream(SimpleLogger.CONFIGURATION_FILE);
                }
                return ClassLoader.getSystemResourceAsStream(SimpleLogger.CONFIGURATION_FILE);
            }
        });
        if (null != in) {
            try {
                SIMPLE_LOGGER_PROPS.load(in);
                in.close();
            } catch (IOException e) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SimpleLogger(String name) {
        this.currentLogLevel = 20;
        if (!INITIALIZED) {
            init();
        }
        this.name = name;
        String levelString = recursivelyComputeLevelString();
        if (levelString != null) {
            this.currentLogLevel = stringToLevel(levelString);
        } else {
            this.currentLogLevel = DEFAULT_LOG_LEVEL;
        }
    }

    String recursivelyComputeLevelString() {
        String tempName = this.name;
        String levelString = null;
        int length = tempName.length();
        while (true) {
            int indexOfLastDot = length;
            if (levelString != null || indexOfLastDot <= -1) {
                break;
            }
            tempName = tempName.substring(0, indexOfLastDot);
            levelString = getStringProperty("org.slf4j.simpleLogger.log." + tempName, null);
            length = String.valueOf(tempName).lastIndexOf(".");
        }
        return levelString;
    }

    private static int stringToLevel(String levelStr) {
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
        return 20;
    }

    private void log(int level, String message, Throwable t) {
        if (!isLevelEnabled(level)) {
            return;
        }
        StringBuffer buf = new StringBuffer(32);
        if (SHOW_DATE_TIME) {
            if (DATE_FORMATTER != null) {
                buf.append(getFormattedDate());
                buf.append(' ');
            } else {
                buf.append(System.currentTimeMillis() - START_TIME);
                buf.append(' ');
            }
        }
        if (SHOW_THREAD_NAME) {
            buf.append('[');
            buf.append(Thread.currentThread().getName());
            buf.append("] ");
        }
        if (LEVEL_IN_BRACKETS) {
            buf.append('[');
        }
        switch (level) {
            case 0:
                buf.append(HttpTrace.METHOD_NAME);
                break;
            case 10:
                buf.append("DEBUG");
                break;
            case 20:
                buf.append("INFO");
                break;
            case 30:
                buf.append(WARN_LEVEL_STRING);
                break;
            case 40:
                buf.append("ERROR");
                break;
        }
        if (LEVEL_IN_BRACKETS) {
            buf.append(']');
        }
        buf.append(' ');
        if (SHOW_SHORT_LOG_NAME) {
            if (this.shortLogName == null) {
                this.shortLogName = computeShortName();
            }
            buf.append(String.valueOf(this.shortLogName)).append(" - ");
        } else if (SHOW_LOG_NAME) {
            buf.append(String.valueOf(this.name)).append(" - ");
        }
        buf.append(message);
        write(buf, t);
    }

    void write(StringBuffer buf, Throwable t) {
        TARGET_STREAM.println(buf.toString());
        if (t != null) {
            t.printStackTrace(TARGET_STREAM);
        }
        TARGET_STREAM.flush();
    }

    private String getFormattedDate() {
        String dateText;
        Date now = new Date();
        synchronized (DATE_FORMATTER) {
            dateText = DATE_FORMATTER.format(now);
        }
        return dateText;
    }

    private String computeShortName() {
        return this.name.substring(this.name.lastIndexOf(".") + 1);
    }

    private void formatAndLog(int level, String format, Object arg1, Object arg2) {
        if (!isLevelEnabled(level)) {
            return;
        }
        FormattingTuple tp = MessageFormatter.format(format, arg1, arg2);
        log(level, tp.getMessage(), tp.getThrowable());
    }

    private void formatAndLog(int level, String format, Object... arguments) {
        if (!isLevelEnabled(level)) {
            return;
        }
        FormattingTuple tp = MessageFormatter.arrayFormat(format, arguments);
        log(level, tp.getMessage(), tp.getThrowable());
    }

    protected boolean isLevelEnabled(int logLevel) {
        return logLevel >= this.currentLogLevel;
    }

    @Override // org.slf4j.Logger
    public boolean isTraceEnabled() {
        return isLevelEnabled(0);
    }

    @Override // org.slf4j.Logger
    public void trace(String msg) {
        log(0, msg, null);
    }

    @Override // org.slf4j.Logger
    public void trace(String format, Object param1) {
        formatAndLog(0, format, param1, null);
    }

    @Override // org.slf4j.Logger
    public void trace(String format, Object param1, Object param2) {
        formatAndLog(0, format, param1, param2);
    }

    @Override // org.slf4j.Logger
    public void trace(String format, Object... argArray) {
        formatAndLog(0, format, argArray);
    }

    @Override // org.slf4j.Logger
    public void trace(String msg, Throwable t) {
        log(0, msg, t);
    }

    @Override // org.slf4j.Logger
    public boolean isDebugEnabled() {
        return isLevelEnabled(10);
    }

    @Override // org.slf4j.Logger
    public void debug(String msg) {
        log(10, msg, null);
    }

    @Override // org.slf4j.Logger
    public void debug(String format, Object param1) {
        formatAndLog(10, format, param1, null);
    }

    @Override // org.slf4j.Logger
    public void debug(String format, Object param1, Object param2) {
        formatAndLog(10, format, param1, param2);
    }

    @Override // org.slf4j.Logger
    public void debug(String format, Object... argArray) {
        formatAndLog(10, format, argArray);
    }

    @Override // org.slf4j.Logger
    public void debug(String msg, Throwable t) {
        log(10, msg, t);
    }

    @Override // org.slf4j.Logger
    public boolean isInfoEnabled() {
        return isLevelEnabled(20);
    }

    @Override // org.slf4j.Logger
    public void info(String msg) {
        log(20, msg, null);
    }

    @Override // org.slf4j.Logger
    public void info(String format, Object arg) {
        formatAndLog(20, format, arg, null);
    }

    @Override // org.slf4j.Logger
    public void info(String format, Object arg1, Object arg2) {
        formatAndLog(20, format, arg1, arg2);
    }

    @Override // org.slf4j.Logger
    public void info(String format, Object... argArray) {
        formatAndLog(20, format, argArray);
    }

    @Override // org.slf4j.Logger
    public void info(String msg, Throwable t) {
        log(20, msg, t);
    }

    @Override // org.slf4j.Logger
    public boolean isWarnEnabled() {
        return isLevelEnabled(30);
    }

    @Override // org.slf4j.Logger
    public void warn(String msg) {
        log(30, msg, null);
    }

    @Override // org.slf4j.Logger
    public void warn(String format, Object arg) {
        formatAndLog(30, format, arg, null);
    }

    @Override // org.slf4j.Logger
    public void warn(String format, Object arg1, Object arg2) {
        formatAndLog(30, format, arg1, arg2);
    }

    @Override // org.slf4j.Logger
    public void warn(String format, Object... argArray) {
        formatAndLog(30, format, argArray);
    }

    @Override // org.slf4j.Logger
    public void warn(String msg, Throwable t) {
        log(30, msg, t);
    }

    @Override // org.slf4j.Logger
    public boolean isErrorEnabled() {
        return isLevelEnabled(40);
    }

    @Override // org.slf4j.Logger
    public void error(String msg) {
        log(40, msg, null);
    }

    @Override // org.slf4j.Logger
    public void error(String format, Object arg) {
        formatAndLog(40, format, arg, null);
    }

    @Override // org.slf4j.Logger
    public void error(String format, Object arg1, Object arg2) {
        formatAndLog(40, format, arg1, arg2);
    }

    @Override // org.slf4j.Logger
    public void error(String format, Object... argArray) {
        formatAndLog(40, format, argArray);
    }

    @Override // org.slf4j.Logger
    public void error(String msg, Throwable t) {
        log(40, msg, t);
    }
}
