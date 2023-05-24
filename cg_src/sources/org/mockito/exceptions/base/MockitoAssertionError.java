package org.mockito.exceptions.base;

import org.mockito.internal.exceptions.stacktrace.ConditionalStackTraceFilter;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/exceptions/base/MockitoAssertionError.class */
public class MockitoAssertionError extends AssertionError {
    private static final long serialVersionUID = 1;
    private final StackTraceElement[] unfilteredStackTrace;

    public MockitoAssertionError(String message) {
        super(message);
        this.unfilteredStackTrace = getStackTrace();
        ConditionalStackTraceFilter filter = new ConditionalStackTraceFilter();
        filter.filter(this);
    }

    public MockitoAssertionError(MockitoAssertionError error, String message) {
        super(message + "\n" + error.getMessage());
        super.setStackTrace(error.getStackTrace());
        this.unfilteredStackTrace = error.getUnfilteredStackTrace();
    }

    public MockitoAssertionError(AssertionError error, String message) {
        super(message + "\n" + error.getMessage());
        this.unfilteredStackTrace = error.getStackTrace();
        super.setStackTrace(this.unfilteredStackTrace);
    }

    public StackTraceElement[] getUnfilteredStackTrace() {
        return this.unfilteredStackTrace;
    }
}
