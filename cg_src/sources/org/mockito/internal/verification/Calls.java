package org.mockito.internal.verification;

import java.util.List;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.api.VerificationDataInOrder;
import org.mockito.internal.verification.api.VerificationInOrderMode;
import org.mockito.internal.verification.checkers.MissingInvocationChecker;
import org.mockito.internal.verification.checkers.NumberOfInvocationsChecker;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MatchableInvocation;
import org.mockito.verification.VerificationMode;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/verification/Calls.class */
public class Calls implements VerificationMode, VerificationInOrderMode {
    final int wantedCount;

    public Calls(int wantedNumberOfInvocations) {
        if (wantedNumberOfInvocations <= 0) {
            throw new MockitoException("Negative and zero values are not allowed here");
        }
        this.wantedCount = wantedNumberOfInvocations;
    }

    @Override // org.mockito.verification.VerificationMode
    public void verify(VerificationData data) {
        throw new MockitoException("calls is only intended to work with InOrder");
    }

    @Override // org.mockito.internal.verification.api.VerificationInOrderMode
    public void verifyInOrder(VerificationDataInOrder data) {
        List<Invocation> allInvocations = data.getAllInvocations();
        MatchableInvocation wanted = data.getWanted();
        MissingInvocationChecker.checkMissingInvocation(allInvocations, wanted, data.getOrderingContext());
        NumberOfInvocationsChecker.checkNumberOfInvocationsNonGreedy(allInvocations, wanted, this.wantedCount, data.getOrderingContext());
    }

    public String toString() {
        return "Wanted invocations count (non-greedy): " + this.wantedCount;
    }
}
