package org.mockito.internal.invocation;

import java.util.List;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MatchableInvocation;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/invocation/InvocationMarker.class */
public class InvocationMarker {
    private InvocationMarker() {
    }

    public static void markVerified(List<Invocation> invocations, MatchableInvocation wanted) {
        for (Invocation invocation : invocations) {
            markVerified(invocation, wanted);
        }
    }

    public static void markVerified(Invocation invocation, MatchableInvocation wanted) {
        invocation.markVerified();
        wanted.captureArgumentsFrom(invocation);
    }

    public static void markVerifiedInOrder(List<Invocation> chunk, MatchableInvocation wanted, InOrderContext context) {
        markVerified(chunk, wanted);
        for (Invocation i : chunk) {
            context.markVerified(i);
        }
    }
}
