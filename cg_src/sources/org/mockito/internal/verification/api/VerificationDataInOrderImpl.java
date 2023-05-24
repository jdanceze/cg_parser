package org.mockito.internal.verification.api;

import java.util.List;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MatchableInvocation;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/verification/api/VerificationDataInOrderImpl.class */
public class VerificationDataInOrderImpl implements VerificationDataInOrder {
    private final InOrderContext inOrder;
    private final List<Invocation> allInvocations;
    private final MatchableInvocation wanted;

    public VerificationDataInOrderImpl(InOrderContext inOrder, List<Invocation> allInvocations, MatchableInvocation wanted) {
        this.inOrder = inOrder;
        this.allInvocations = allInvocations;
        this.wanted = wanted;
    }

    @Override // org.mockito.internal.verification.api.VerificationDataInOrder
    public List<Invocation> getAllInvocations() {
        return this.allInvocations;
    }

    @Override // org.mockito.internal.verification.api.VerificationDataInOrder
    public InOrderContext getOrderingContext() {
        return this.inOrder;
    }

    @Override // org.mockito.internal.verification.api.VerificationDataInOrder
    public MatchableInvocation getWanted() {
        return this.wanted;
    }
}
