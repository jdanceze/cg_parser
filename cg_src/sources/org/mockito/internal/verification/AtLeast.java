package org.mockito.internal.verification;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.api.VerificationDataInOrder;
import org.mockito.internal.verification.api.VerificationInOrderMode;
import org.mockito.internal.verification.checkers.AtLeastXNumberOfInvocationsChecker;
import org.mockito.internal.verification.checkers.MissingInvocationChecker;
import org.mockito.verification.VerificationMode;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/verification/AtLeast.class */
public class AtLeast implements VerificationInOrderMode, VerificationMode {
    final int wantedCount;

    public AtLeast(int wantedNumberOfInvocations) {
        if (wantedNumberOfInvocations < 0) {
            throw new MockitoException("Negative value is not allowed here");
        }
        this.wantedCount = wantedNumberOfInvocations;
    }

    @Override // org.mockito.verification.VerificationMode
    public void verify(VerificationData data) {
        if (this.wantedCount == 1) {
            MissingInvocationChecker.checkMissingInvocation(data.getAllInvocations(), data.getTarget());
        }
        AtLeastXNumberOfInvocationsChecker.checkAtLeastNumberOfInvocations(data.getAllInvocations(), data.getTarget(), this.wantedCount);
    }

    @Override // org.mockito.internal.verification.api.VerificationInOrderMode
    public void verifyInOrder(VerificationDataInOrder data) {
        if (this.wantedCount == 1) {
            MissingInvocationChecker.checkMissingInvocation(data.getAllInvocations(), data.getWanted(), data.getOrderingContext());
        }
        AtLeastXNumberOfInvocationsChecker.checkAtLeastNumberOfInvocations(data.getAllInvocations(), data.getWanted(), this.wantedCount, data.getOrderingContext());
    }

    public String toString() {
        return "Wanted invocations count: at least " + this.wantedCount;
    }
}
