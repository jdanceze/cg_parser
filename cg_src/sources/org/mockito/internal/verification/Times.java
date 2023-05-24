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
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/verification/Times.class */
public class Times implements VerificationInOrderMode, VerificationMode {
    final int wantedCount;

    public Times(int wantedNumberOfInvocations) {
        if (wantedNumberOfInvocations < 0) {
            throw new MockitoException("Negative value is not allowed here");
        }
        this.wantedCount = wantedNumberOfInvocations;
    }

    @Override // org.mockito.verification.VerificationMode
    public void verify(VerificationData data) {
        List<Invocation> invocations = data.getAllInvocations();
        MatchableInvocation wanted = data.getTarget();
        if (this.wantedCount > 0) {
            MissingInvocationChecker.checkMissingInvocation(data.getAllInvocations(), data.getTarget());
        }
        NumberOfInvocationsChecker.checkNumberOfInvocations(invocations, wanted, this.wantedCount);
    }

    @Override // org.mockito.internal.verification.api.VerificationInOrderMode
    public void verifyInOrder(VerificationDataInOrder data) {
        List<Invocation> allInvocations = data.getAllInvocations();
        MatchableInvocation wanted = data.getWanted();
        if (this.wantedCount > 0) {
            MissingInvocationChecker.checkMissingInvocation(allInvocations, wanted, data.getOrderingContext());
        }
        NumberOfInvocationsChecker.checkNumberOfInvocations(allInvocations, wanted, this.wantedCount, data.getOrderingContext());
    }

    public String toString() {
        return "Wanted invocations count: " + this.wantedCount;
    }

    @Override // org.mockito.verification.VerificationMode
    public VerificationMode description(String description) {
        return VerificationModeFactory.description(this, description);
    }
}
