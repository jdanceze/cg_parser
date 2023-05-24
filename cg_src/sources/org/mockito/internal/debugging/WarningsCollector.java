package org.mockito.internal.debugging;

import java.util.LinkedList;
import java.util.List;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.UnusedStubsFinder;
import org.mockito.internal.invocation.finder.AllInvocationsFinder;
import org.mockito.invocation.Invocation;
@Deprecated
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/debugging/WarningsCollector.class */
public class WarningsCollector {
    private final List<Object> createdMocks = new LinkedList();

    public String getWarnings() {
        List<Invocation> unused = new UnusedStubsFinder().find(this.createdMocks);
        List<Invocation> all = AllInvocationsFinder.find(this.createdMocks);
        List<InvocationMatcher> allInvocationMatchers = InvocationMatcher.createFrom(all);
        return new WarningsPrinterImpl(unused, allInvocationMatchers, false).print();
    }
}
