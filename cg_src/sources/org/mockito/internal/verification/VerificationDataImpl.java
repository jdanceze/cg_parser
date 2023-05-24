package org.mockito.internal.verification;

import java.util.List;
import org.mockito.internal.exceptions.Reporter;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MatchableInvocation;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/verification/VerificationDataImpl.class */
public class VerificationDataImpl implements VerificationData {
    private final InvocationMatcher wanted;
    private final InvocationContainerImpl invocations;

    public VerificationDataImpl(InvocationContainerImpl invocations, InvocationMatcher wanted) {
        this.invocations = invocations;
        this.wanted = wanted;
        assertWantedIsVerifiable();
    }

    @Override // org.mockito.internal.verification.api.VerificationData
    public List<Invocation> getAllInvocations() {
        return this.invocations.getInvocations();
    }

    @Override // org.mockito.internal.verification.api.VerificationData
    public MatchableInvocation getTarget() {
        return this.wanted;
    }

    @Override // org.mockito.internal.verification.api.VerificationData
    public InvocationMatcher getWanted() {
        return this.wanted;
    }

    private void assertWantedIsVerifiable() {
        if (this.wanted != null && ObjectMethodsGuru.isToStringMethod(this.wanted.getMethod())) {
            throw Reporter.cannotVerifyToString();
        }
    }
}
