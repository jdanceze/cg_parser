package org.mockito.exceptions.base;

import org.mockito.internal.exceptions.stacktrace.ConditionalStackTraceFilter;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/exceptions/base/MockitoException.class */
public class MockitoException extends RuntimeException {
    private static final long serialVersionUID = 1;
    private StackTraceElement[] unfilteredStackTrace;

    public MockitoException(String message, Throwable t) {
        super(message, t);
        filterStackTrace();
    }

    public MockitoException(String message) {
        super(message);
        filterStackTrace();
    }

    private void filterStackTrace() {
        this.unfilteredStackTrace = getStackTrace();
        ConditionalStackTraceFilter filter = new ConditionalStackTraceFilter();
        filter.filter(this);
    }

    public StackTraceElement[] getUnfilteredStackTrace() {
        return this.unfilteredStackTrace;
    }
}
