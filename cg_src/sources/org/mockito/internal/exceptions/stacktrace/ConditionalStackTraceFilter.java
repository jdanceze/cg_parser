package org.mockito.internal.exceptions.stacktrace;

import java.io.Serializable;
import org.mockito.configuration.IMockitoConfiguration;
import org.mockito.internal.configuration.GlobalConfiguration;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/exceptions/stacktrace/ConditionalStackTraceFilter.class */
public class ConditionalStackTraceFilter implements Serializable {
    private static final long serialVersionUID = -8085849703510292641L;
    private final IMockitoConfiguration config = new GlobalConfiguration();
    private final StackTraceFilter filter = new StackTraceFilter();

    public void filter(Throwable throwable) {
        if (!this.config.cleansStackTrace()) {
            return;
        }
        StackTraceElement[] filtered = this.filter.filter(throwable.getStackTrace(), true);
        throwable.setStackTrace(filtered);
    }
}
