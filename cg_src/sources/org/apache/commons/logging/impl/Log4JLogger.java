package org.apache.commons.logging.impl;

import java.io.Serializable;
import org.apache.commons.logging.Log;
import org.apache.http.client.methods.HttpTrace;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
/* loaded from: gencallgraphv3.jar:commons-logging-1.1.1.jar:org/apache/commons/logging/impl/Log4JLogger.class */
public class Log4JLogger implements Log, Serializable {
    private static final String FQCN;
    private transient Logger logger;
    private String name;
    private static Priority traceLevel;
    static Class class$org$apache$commons$logging$impl$Log4JLogger;
    static Class class$org$apache$log4j$Level;
    static Class class$org$apache$log4j$Priority;

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    static {
        Class cls;
        Class cls2;
        Class<?> cls3;
        Class cls4;
        if (class$org$apache$commons$logging$impl$Log4JLogger == null) {
            cls = class$("org.apache.commons.logging.impl.Log4JLogger");
            class$org$apache$commons$logging$impl$Log4JLogger = cls;
        } else {
            cls = class$org$apache$commons$logging$impl$Log4JLogger;
        }
        FQCN = cls.getName();
        if (class$org$apache$log4j$Priority == null) {
            cls2 = class$("org.apache.log4j.Priority");
            class$org$apache$log4j$Priority = cls2;
        } else {
            cls2 = class$org$apache$log4j$Priority;
        }
        if (class$org$apache$log4j$Level == null) {
            cls3 = class$("org.apache.log4j.Level");
            class$org$apache$log4j$Level = cls3;
        } else {
            cls3 = class$org$apache$log4j$Level;
        }
        if (!cls2.isAssignableFrom(cls3)) {
            throw new InstantiationError("Log4J 1.2 not available");
        }
        try {
            if (class$org$apache$log4j$Level == null) {
                cls4 = class$("org.apache.log4j.Level");
                class$org$apache$log4j$Level = cls4;
            } else {
                cls4 = class$org$apache$log4j$Level;
            }
            traceLevel = (Priority) cls4.getDeclaredField(HttpTrace.METHOD_NAME).get(null);
        } catch (Exception e) {
            traceLevel = Priority.DEBUG;
        }
    }

    public Log4JLogger() {
        this.logger = null;
        this.name = null;
    }

    public Log4JLogger(String name) {
        this.logger = null;
        this.name = null;
        this.name = name;
        this.logger = getLogger();
    }

    public Log4JLogger(Logger logger) {
        this.logger = null;
        this.name = null;
        if (logger == null) {
            throw new IllegalArgumentException("Warning - null logger in constructor; possible log4j misconfiguration.");
        }
        this.name = logger.getName();
        this.logger = logger;
    }

    @Override // org.apache.commons.logging.Log
    public void trace(Object message) {
        getLogger().log(FQCN, traceLevel, message, (Throwable) null);
    }

    @Override // org.apache.commons.logging.Log
    public void trace(Object message, Throwable t) {
        getLogger().log(FQCN, traceLevel, message, t);
    }

    @Override // org.apache.commons.logging.Log
    public void debug(Object message) {
        getLogger().log(FQCN, Priority.DEBUG, message, (Throwable) null);
    }

    @Override // org.apache.commons.logging.Log
    public void debug(Object message, Throwable t) {
        getLogger().log(FQCN, Priority.DEBUG, message, t);
    }

    @Override // org.apache.commons.logging.Log
    public void info(Object message) {
        getLogger().log(FQCN, Priority.INFO, message, (Throwable) null);
    }

    @Override // org.apache.commons.logging.Log
    public void info(Object message, Throwable t) {
        getLogger().log(FQCN, Priority.INFO, message, t);
    }

    @Override // org.apache.commons.logging.Log
    public void warn(Object message) {
        getLogger().log(FQCN, Priority.WARN, message, (Throwable) null);
    }

    @Override // org.apache.commons.logging.Log
    public void warn(Object message, Throwable t) {
        getLogger().log(FQCN, Priority.WARN, message, t);
    }

    @Override // org.apache.commons.logging.Log
    public void error(Object message) {
        getLogger().log(FQCN, Priority.ERROR, message, (Throwable) null);
    }

    @Override // org.apache.commons.logging.Log
    public void error(Object message, Throwable t) {
        getLogger().log(FQCN, Priority.ERROR, message, t);
    }

    @Override // org.apache.commons.logging.Log
    public void fatal(Object message) {
        getLogger().log(FQCN, Priority.FATAL, message, (Throwable) null);
    }

    @Override // org.apache.commons.logging.Log
    public void fatal(Object message, Throwable t) {
        getLogger().log(FQCN, Priority.FATAL, message, t);
    }

    public Logger getLogger() {
        if (this.logger == null) {
            this.logger = Logger.getLogger(this.name);
        }
        return this.logger;
    }

    @Override // org.apache.commons.logging.Log
    public boolean isDebugEnabled() {
        return getLogger().isDebugEnabled();
    }

    @Override // org.apache.commons.logging.Log
    public boolean isErrorEnabled() {
        return getLogger().isEnabledFor(Priority.ERROR);
    }

    @Override // org.apache.commons.logging.Log
    public boolean isFatalEnabled() {
        return getLogger().isEnabledFor(Priority.FATAL);
    }

    @Override // org.apache.commons.logging.Log
    public boolean isInfoEnabled() {
        return getLogger().isInfoEnabled();
    }

    @Override // org.apache.commons.logging.Log
    public boolean isTraceEnabled() {
        return getLogger().isEnabledFor(traceLevel);
    }

    @Override // org.apache.commons.logging.Log
    public boolean isWarnEnabled() {
        return getLogger().isEnabledFor(Priority.WARN);
    }
}
