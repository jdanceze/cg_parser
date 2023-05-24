package org.mockito.internal.verification;

import java.util.List;
import org.mockito.internal.InOrderImpl;
import org.mockito.internal.invocation.finder.VerifiableInvocationsFinder;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.api.VerificationDataInOrderImpl;
import org.mockito.internal.verification.api.VerificationInOrderMode;
import org.mockito.invocation.Invocation;
import org.mockito.verification.VerificationMode;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/verification/InOrderWrapper.class */
public class InOrderWrapper implements VerificationMode {
    private final VerificationInOrderMode mode;
    private final InOrderImpl inOrder;

    public InOrderWrapper(VerificationInOrderMode mode, InOrderImpl inOrder) {
        this.mode = mode;
        this.inOrder = inOrder;
    }

    @Override // org.mockito.verification.VerificationMode
    public void verify(VerificationData data) {
        List<Invocation> invocations = VerifiableInvocationsFinder.find(this.inOrder.getMocksToBeVerifiedInOrder());
        VerificationDataInOrderImpl dataInOrder = new VerificationDataInOrderImpl(this.inOrder, invocations, data.getTarget());
        this.mode.verifyInOrder(dataInOrder);
    }
}
