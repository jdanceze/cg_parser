package org.apache.commons.logging;
/* loaded from: gencallgraphv3.jar:commons-logging-1.1.1.jar:org/apache/commons/logging/LogConfigurationException.class */
public class LogConfigurationException extends RuntimeException {
    protected Throwable cause;

    public LogConfigurationException() {
        this.cause = null;
    }

    public LogConfigurationException(String message) {
        super(message);
        this.cause = null;
    }

    public LogConfigurationException(Throwable cause) {
        this(cause == null ? null : cause.toString(), cause);
    }

    public LogConfigurationException(String message, Throwable cause) {
        super(new StringBuffer().append(message).append(" (Caused by ").append(cause).append(")").toString());
        this.cause = null;
        this.cause = cause;
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return this.cause;
    }
}
