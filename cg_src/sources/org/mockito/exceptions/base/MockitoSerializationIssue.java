package org.mockito.exceptions.base;

import java.io.ObjectStreamException;
import org.mockito.internal.exceptions.stacktrace.ConditionalStackTraceFilter;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/exceptions/base/MockitoSerializationIssue.class */
public class MockitoSerializationIssue extends ObjectStreamException {
    private StackTraceElement[] unfilteredStackTrace;

    public MockitoSerializationIssue(String message, Exception cause) {
        super(message);
        initCause(cause);
        filterStackTrace();
    }

    private void filterStackTrace() {
        this.unfilteredStackTrace = super.getStackTrace();
        ConditionalStackTraceFilter filter = new ConditionalStackTraceFilter();
        filter.filter(this);
    }

    public StackTraceElement[] getUnfilteredStackTrace() {
        return this.unfilteredStackTrace;
    }
}
