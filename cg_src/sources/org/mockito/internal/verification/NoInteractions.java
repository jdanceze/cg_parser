package org.mockito.internal.verification;

import java.util.List;
import org.mockito.internal.exceptions.Reporter;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.invocation.Invocation;
import org.mockito.verification.VerificationMode;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/verification/NoInteractions.class */
public class NoInteractions implements VerificationMode {
    @Override // org.mockito.verification.VerificationMode
    public void verify(VerificationData data) {
        List<Invocation> invocations = data.getAllInvocations();
        if (!invocations.isEmpty()) {
            throw Reporter.noInteractionsWanted(invocations.get(0).getMock(), invocations);
        }
    }
}
