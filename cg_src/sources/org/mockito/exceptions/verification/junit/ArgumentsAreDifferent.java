package org.mockito.exceptions.verification.junit;

import junit.framework.ComparisonFailure;
import org.mockito.internal.exceptions.stacktrace.ConditionalStackTraceFilter;
import org.mockito.internal.util.StringUtil;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/exceptions/verification/junit/ArgumentsAreDifferent.class */
public class ArgumentsAreDifferent extends ComparisonFailure {
    private static final long serialVersionUID = 1;
    private final String message;
    private final StackTraceElement[] unfilteredStackTrace;

    public ArgumentsAreDifferent(String message, String wanted, String actual) {
        super(message, wanted, actual);
        this.message = message;
        this.unfilteredStackTrace = getStackTrace();
        ConditionalStackTraceFilter filter = new ConditionalStackTraceFilter();
        filter.filter(this);
    }

    @Override // junit.framework.ComparisonFailure, java.lang.Throwable
    public String getMessage() {
        return this.message;
    }

    public StackTraceElement[] getUnfilteredStackTrace() {
        return this.unfilteredStackTrace;
    }

    @Override // java.lang.Throwable
    public String toString() {
        return StringUtil.removeFirstLine(super.toString());
    }
}
