package org.mockito.internal.verification;

import java.util.List;
import org.mockito.internal.exceptions.Reporter;
import org.mockito.internal.invocation.InvocationsFinder;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.api.VerificationDataInOrder;
import org.mockito.internal.verification.api.VerificationInOrderMode;
import org.mockito.invocation.Invocation;
import org.mockito.verification.VerificationMode;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/verification/NoMoreInteractions.class */
public class NoMoreInteractions implements VerificationMode, VerificationInOrderMode {
    @Override // org.mockito.verification.VerificationMode
    public void verify(VerificationData data) {
        Invocation unverified = InvocationsFinder.findFirstUnverified(data.getAllInvocations());
        if (unverified != null) {
            throw Reporter.noMoreInteractionsWanted(unverified, data.getAllInvocations());
        }
    }

    @Override // org.mockito.internal.verification.api.VerificationInOrderMode
    public void verifyInOrder(VerificationDataInOrder data) {
        List<Invocation> invocations = data.getAllInvocations();
        Invocation unverified = InvocationsFinder.findFirstUnverifiedInOrder(data.getOrderingContext(), invocations);
        if (unverified != null) {
            throw Reporter.noMoreInteractionsWantedInOrder(unverified);
        }
    }
}
