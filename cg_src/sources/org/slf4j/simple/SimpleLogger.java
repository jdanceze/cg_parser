package org.slf4j.simple;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Marker;
import org.slf4j.event.Level;
import org.slf4j.event.LoggingEvent;
import org.slf4j.helpers.LegacyAbstractLogger;
import org.slf4j.helpers.MessageFormatter;
import org.slf4j.helpers.NormalizedParameters;
/* loaded from: gencallgraphv3.jar:slf4j-simple-2.0.3.jar:org/slf4j/simple/SimpleLogger.class */
public class SimpleLogger extends LegacyAbstractLogger {
    private static final long serialVersionUID = -632788891211436180L;
    protected static final int LOG_LEVEL_TRACE = 0;
    protected static final int LOG_LEVEL_DEBUG = 10;
    protected static final int LOG_LEVEL_INFO = 20;
    protected static final int LOG_LEVEL_WARN = 30;
    protected static final int LOG_LEVEL_ERROR = 40;
    static final String TID_PREFIX = "tid=";
    protected static final int LOG_LEVEL_OFF = 50;
    protected int currentLogLevel;
    private transient String shortLogName = null;
    public static final String SYSTEM_PREFIX = "org.slf4j.simpleLogger.";
    public static final String LOG_KEY_PREFIX = "org.slf4j.simpleLogger.log.";
    public static final String CACHE_OUTPUT_STREAM_STRING_KEY = "org.slf4j.simpleLogger.cacheOutputStream";
    public static final String WARN_LEVEL_STRING_KEY = "org.slf4j.simpleLogger.warnLevelString";
    public static final String LEVEL_IN_BRACKETS_KEY = "org.slf4j.simpleLogger.levelInBrackets";
    public static final String LOG_FILE_KEY = "org.slf4j.simpleLogger.logFile";
    public static final String SHOW_SHORT_LOG_NAME_KEY = "org.slf4j.simpleLogger.showShortLogName";
    public static final String SHOW_LOG_NAME_KEY = "org.slf4j.simpleLogger.showLogName";
    public static final String SHOW_THREAD_NAME_KEY = "org.slf4j.simpleLogger.showThreadName";
    public static final String SHOW_THREAD_ID_KEY = "org.slf4j.simpleLogger.showThreadId";
    public static final String DATE_TIME_FORMAT_KEY = "org.slf4j.simpleLogger.dateTimeFormat";
    public static final String SHOW_DATE_TIME_KEY = "org.slf4j.simpleLogger.showDateTime";
    public static final String DEFAULT_LOG_LEVEL_KEY = "org.slf4j.simpleLogger.defaultLogLevel";
    private static final long START_TIME = System.currentTimeMillis();
    static char SP = ' ';
    private static boolean INITIALIZED = false;
    static final SimpleLoggerConfiguration CONFIG_PARAMS = new SimpleLoggerConfiguration();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void lazyInit() {
        if (INITIALIZED) {
            return;
        }
        INITIALIZED = true;
        init();
    }

    static void init() {
        CONFIG_PARAMS.init();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SimpleLogger(String name) {
        this.currentLogLevel = 20;
        this.name = name;
        String levelString = recursivelyComputeLevelString();
        if (levelString != null) {
            this.currentLogLevel = SimpleLoggerConfiguration.stringToLevel(levelString);
        } else {
            this.currentLogLevel = CONFIG_PARAMS.defaultLogLevel;
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
            levelString = CONFIG_PARAMS.getStringProperty("org.slf4j.simpleLogger.log." + tempName, null);
            length = String.valueOf(tempName).lastIndexOf(".");
        }
        return levelString;
    }

    void write(StringBuilder buf, Throwable t) {
        PrintStream targetStream = CONFIG_PARAMS.outputChoice.getTargetPrintStream();
        synchronized (CONFIG_PARAMS) {
            targetStream.println(buf.toString());
            writeThrowable(t, targetStream);
            targetStream.flush();
        }
    }

    protected void writeThrowable(Throwable t, PrintStream targetStream) {
        if (t != null) {
            t.printStackTrace(targetStream);
        }
    }

    private String getFormattedDate() {
        String dateText;
        Date now = new Date();
        synchronized (CONFIG_PARAMS.dateFormatter) {
            dateText = CONFIG_PARAMS.dateFormatter.format(now);
        }
        return dateText;
    }

    private String computeShortName() {
        return this.name.substring(this.name.lastIndexOf(".") + 1);
    }

    protected boolean isLevelEnabled(int logLevel) {
        return logLevel >= this.currentLogLevel;
    }

    @Override // org.slf4j.Logger
    public boolean isTraceEnabled() {
        return isLevelEnabled(0);
    }

    @Override // org.slf4j.Logger
    public boolean isDebugEnabled() {
        return isLevelEnabled(10);
    }

    @Override // org.slf4j.Logger
    public boolean isInfoEnabled() {
        return isLevelEnabled(20);
    }

    @Override // org.slf4j.Logger
    public boolean isWarnEnabled() {
        return isLevelEnabled(30);
    }

    @Override // org.slf4j.Logger
    public boolean isErrorEnabled() {
        return isLevelEnabled(40);
    }

    @Override // org.slf4j.helpers.AbstractLogger
    protected void handleNormalizedLoggingCall(Level level, Marker marker, String messagePattern, Object[] arguments, Throwable throwable) {
        List<Marker> markers = null;
        if (marker != null) {
            markers = new ArrayList<>();
            markers.add(marker);
        }
        innerHandleNormalizedLoggingCall(level, markers, messagePattern, arguments, throwable);
    }

    private void innerHandleNormalizedLoggingCall(Level level, List<Marker> markers, String messagePattern, Object[] arguments, Throwable t) {
        StringBuilder buf = new StringBuilder(32);
        if (CONFIG_PARAMS.showDateTime) {
            if (CONFIG_PARAMS.dateFormatter != null) {
                buf.append(getFormattedDate());
                buf.append(SP);
            } else {
                buf.append(System.currentTimeMillis() - START_TIME);
                buf.append(SP);
            }
        }
        if (CONFIG_PARAMS.showThreadName) {
            buf.append('[');
            buf.append(Thread.currentThread().getName());
            buf.append("] ");
        }
        if (CONFIG_PARAMS.showThreadId) {
            buf.append(TID_PREFIX);
            buf.append(Thread.currentThread().getId());
            buf.append(SP);
        }
        if (CONFIG_PARAMS.levelInBrackets) {
            buf.append('[');
        }
        String levelStr = level.name();
        buf.append(levelStr);
        if (CONFIG_PARAMS.levelInBrackets) {
            buf.append(']');
        }
        buf.append(SP);
        if (CONFIG_PARAMS.showShortLogName) {
            if (this.shortLogName == null) {
                this.shortLogName = computeShortName();
            }
            buf.append(String.valueOf(this.shortLogName)).append(" - ");
        } else if (CONFIG_PARAMS.showLogName) {
            buf.append(String.valueOf(this.name)).append(" - ");
        }
        if (markers != null) {
            buf.append(SP);
            for (Marker marker : markers) {
                buf.append(marker.getName()).append(SP);
            }
        }
        String formattedMessage = MessageFormatter.basicArrayFormat(messagePattern, arguments);
        buf.append(formattedMessage);
        write(buf, t);
    }

    public void log(LoggingEvent event) {
        int levelInt = event.getLevel().toInt();
        if (!isLevelEnabled(levelInt)) {
            return;
        }
        NormalizedParameters np = NormalizedParameters.normalize(event);
        innerHandleNormalizedLoggingCall(event.getLevel(), event.getMarkers(), np.getMessage(), np.getArguments(), event.getThrowable());
    }

    @Override // org.slf4j.helpers.AbstractLogger
    protected String getFullyQualifiedCallerName() {
        return null;
    }
}
