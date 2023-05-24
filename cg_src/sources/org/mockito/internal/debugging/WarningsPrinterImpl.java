package org.mockito.internal.debugging;

import java.util.List;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.Invocation;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/debugging/WarningsPrinterImpl.class */
public class WarningsPrinterImpl {
    private final boolean warnAboutUnstubbed;
    private final WarningsFinder finder;

    public WarningsPrinterImpl(List<Invocation> unusedStubs, List<InvocationMatcher> allInvocations, boolean warnAboutUnstubbed) {
        this(warnAboutUnstubbed, new WarningsFinder(unusedStubs, allInvocations));
    }

    WarningsPrinterImpl(boolean warnAboutUnstubbed, WarningsFinder finder) {
        this.warnAboutUnstubbed = warnAboutUnstubbed;
        this.finder = finder;
    }

    public String print() {
        LoggingListener listener = new LoggingListener(this.warnAboutUnstubbed);
        this.finder.find(listener);
        return listener.getStubbingInfo();
    }
}
