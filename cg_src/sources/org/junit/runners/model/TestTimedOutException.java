package org.junit.runners.model;

import java.util.concurrent.TimeUnit;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/runners/model/TestTimedOutException.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/runners/model/TestTimedOutException.class */
public class TestTimedOutException extends Exception {
    private static final long serialVersionUID = 31935685163547539L;
    private final TimeUnit timeUnit;
    private final long timeout;

    public TestTimedOutException(long timeout, TimeUnit timeUnit) {
        super(String.format("test timed out after %d %s", Long.valueOf(timeout), timeUnit.name().toLowerCase()));
        this.timeUnit = timeUnit;
        this.timeout = timeout;
    }

    public long getTimeout() {
        return this.timeout;
    }

    public TimeUnit getTimeUnit() {
        return this.timeUnit;
    }
}
