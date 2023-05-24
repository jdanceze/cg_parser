package org.mockito.internal.debugging;

import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.Invocation;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/debugging/FindingsListener.class */
public interface FindingsListener {
    void foundStubCalledWithDifferentArgs(Invocation invocation, InvocationMatcher invocationMatcher);

    void foundUnusedStub(Invocation invocation);

    void foundUnstubbed(InvocationMatcher invocationMatcher);
}
