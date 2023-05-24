package org.mockito.internal.debugging;

import java.io.Serializable;
import org.mockito.internal.exceptions.stacktrace.StackTraceFilter;
import org.mockito.invocation.Location;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/debugging/LocationImpl.class */
public class LocationImpl implements Location, Serializable {
    private static final long serialVersionUID = -9054861157390980624L;
    private static final StackTraceFilter stackTraceFilter = new StackTraceFilter();
    private String stackTraceLine;
    private String sourceFile;

    public LocationImpl() {
        this(new Throwable(), false);
    }

    public LocationImpl(Throwable stackTraceHolder, boolean isInline) {
        this(stackTraceFilter, stackTraceHolder, isInline);
    }

    public LocationImpl(StackTraceFilter stackTraceFilter2) {
        this(stackTraceFilter2, new Throwable(), false);
    }

    private LocationImpl(StackTraceFilter stackTraceFilter2, Throwable stackTraceHolder, boolean isInline) {
        computeStackTraceInformation(stackTraceFilter2, stackTraceHolder, isInline);
    }

    @Override // org.mockito.invocation.Location
    public String toString() {
        return this.stackTraceLine;
    }

    private void computeStackTraceInformation(StackTraceFilter stackTraceFilter2, Throwable stackTraceHolder, boolean isInline) {
        StackTraceElement filtered = stackTraceFilter2.filterFirst(stackTraceHolder, isInline);
        if (filtered == null) {
            this.stackTraceLine = "-> at <<unknown line>>";
            this.sourceFile = "<unknown source file>";
            return;
        }
        this.stackTraceLine = "-> at " + filtered.toString();
        this.sourceFile = filtered.getFileName();
    }

    @Override // org.mockito.invocation.Location
    public String getSourceFile() {
        return this.sourceFile;
    }
}
