package org.mockito.internal.verification;

import java.util.List;
import org.mockito.internal.exceptions.Reporter;
import org.mockito.internal.invocation.InvocationMarker;
import org.mockito.internal.invocation.InvocationsFinder;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MatchableInvocation;
import org.mockito.verification.VerificationMode;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/verification/Only.class */
public class Only implements VerificationMode {
    @Override // org.mockito.verification.VerificationMode
    public void verify(VerificationData data) {
        MatchableInvocation target = data.getTarget();
        List<Invocation> invocations = data.getAllInvocations();
        List<Invocation> chunk = InvocationsFinder.findInvocations(invocations, target);
        if (invocations.size() != 1 && !chunk.isEmpty()) {
            Invocation unverified = InvocationsFinder.findFirstUnverified(invocations);
            throw Reporter.noMoreInteractionsWanted(unverified, invocations);
        } else if (invocations.size() != 1 || chunk.isEmpty()) {
            throw Reporter.wantedButNotInvoked(target);
        } else {
            InvocationMarker.markVerified(chunk.get(0), target);
        }
    }
}
