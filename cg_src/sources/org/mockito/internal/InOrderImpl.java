package org.mockito.internal;

import java.util.LinkedList;
import java.util.List;
import org.mockito.InOrder;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.exceptions.Reporter;
import org.mockito.internal.verification.InOrderContextImpl;
import org.mockito.internal.verification.InOrderWrapper;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.internal.verification.VerificationWrapper;
import org.mockito.internal.verification.VerificationWrapperInOrderWrapper;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.internal.verification.api.VerificationInOrderMode;
import org.mockito.invocation.Invocation;
import org.mockito.verification.VerificationMode;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/InOrderImpl.class */
public class InOrderImpl implements InOrder, InOrderContext {
    private final MockitoCore mockitoCore = new MockitoCore();
    private final List<Object> mocksToBeVerifiedInOrder = new LinkedList();
    private final InOrderContext inOrderContext = new InOrderContextImpl();

    public List<Object> getMocksToBeVerifiedInOrder() {
        return this.mocksToBeVerifiedInOrder;
    }

    public InOrderImpl(List<?> mocksToBeVerifiedInOrder) {
        this.mocksToBeVerifiedInOrder.addAll(mocksToBeVerifiedInOrder);
    }

    @Override // org.mockito.InOrder
    public <T> T verify(T mock) {
        return (T) verify(mock, VerificationModeFactory.times(1));
    }

    @Override // org.mockito.InOrder
    public <T> T verify(T mock, VerificationMode mode) {
        if (!this.mocksToBeVerifiedInOrder.contains(mock)) {
            throw Reporter.inOrderRequiresFamiliarMock();
        }
        if (mode instanceof VerificationWrapper) {
            return (T) this.mockitoCore.verify(mock, new VerificationWrapperInOrderWrapper((VerificationWrapper) mode, this));
        }
        if (!(mode instanceof VerificationInOrderMode)) {
            throw new MockitoException(mode.getClass().getSimpleName() + " is not implemented to work with InOrder");
        }
        return (T) this.mockitoCore.verify(mock, new InOrderWrapper((VerificationInOrderMode) mode, this));
    }

    @Override // org.mockito.internal.verification.api.InOrderContext
    public boolean isVerified(Invocation i) {
        return this.inOrderContext.isVerified(i);
    }

    @Override // org.mockito.internal.verification.api.InOrderContext
    public void markVerified(Invocation i) {
        this.inOrderContext.markVerified(i);
    }

    @Override // org.mockito.InOrder
    public void verifyNoMoreInteractions() {
        this.mockitoCore.verifyNoMoreInteractionsInOrder(this.mocksToBeVerifiedInOrder, this);
    }
}
