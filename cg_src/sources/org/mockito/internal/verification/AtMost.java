package org.mockito.internal.verification;

import java.util.Iterator;
import java.util.List;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.exceptions.Reporter;
import org.mockito.internal.invocation.InvocationMarker;
import org.mockito.internal.invocation.InvocationsFinder;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MatchableInvocation;
import org.mockito.verification.VerificationMode;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/verification/AtMost.class */
public class AtMost implements VerificationMode {
    private final int maxNumberOfInvocations;

    public AtMost(int maxNumberOfInvocations) {
        if (maxNumberOfInvocations < 0) {
            throw new MockitoException("Negative value is not allowed here");
        }
        this.maxNumberOfInvocations = maxNumberOfInvocations;
    }

    @Override // org.mockito.verification.VerificationMode
    public void verify(VerificationData data) {
        List<Invocation> invocations = data.getAllInvocations();
        MatchableInvocation wanted = data.getTarget();
        List<Invocation> found = InvocationsFinder.findInvocations(invocations, wanted);
        int foundSize = found.size();
        if (foundSize > this.maxNumberOfInvocations) {
            throw Reporter.wantedAtMostX(this.maxNumberOfInvocations, foundSize);
        }
        removeAlreadyVerified(found);
        InvocationMarker.markVerified(found, wanted);
    }

    private void removeAlreadyVerified(List<Invocation> invocations) {
        Iterator<Invocation> iterator = invocations.iterator();
        while (iterator.hasNext()) {
            Invocation i = iterator.next();
            if (i.isVerified()) {
                iterator.remove();
            }
        }
    }
}
