package org.mockito.internal.verification;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InOrderImpl;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.api.VerificationInOrderMode;
import org.mockito.verification.VerificationMode;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/verification/VerificationWrapperInOrderWrapper.class */
public class VerificationWrapperInOrderWrapper implements VerificationMode {
    private final VerificationMode delegate;

    public VerificationWrapperInOrderWrapper(VerificationWrapper<?> verificationWrapper, InOrderImpl inOrder) {
        VerificationMode verificationMode = verificationWrapper.wrappedVerification;
        VerificationMode inOrderWrappedVerificationMode = wrapInOrder(verificationWrapper, verificationMode, inOrder);
        this.delegate = verificationWrapper.copySelfWithNewVerificationMode(inOrderWrappedVerificationMode);
    }

    @Override // org.mockito.verification.VerificationMode
    public void verify(VerificationData data) {
        this.delegate.verify(data);
    }

    private VerificationMode wrapInOrder(VerificationWrapper<?> verificationWrapper, VerificationMode verificationMode, InOrderImpl inOrder) {
        if (verificationMode instanceof VerificationInOrderMode) {
            VerificationInOrderMode verificationInOrderMode = (VerificationInOrderMode) verificationMode;
            return new InOrderWrapper(verificationInOrderMode, inOrder);
        }
        if (verificationMode instanceof VerificationOverTimeImpl) {
            VerificationOverTimeImpl verificationOverTime = (VerificationOverTimeImpl) verificationMode;
            if (verificationOverTime.isReturnOnSuccess()) {
                return new VerificationOverTimeImpl(verificationOverTime.getPollingPeriodMillis(), verificationOverTime.getTimer().duration(), wrapInOrder(verificationWrapper, verificationOverTime.getDelegate(), inOrder), verificationOverTime.isReturnOnSuccess());
            }
        }
        throw new MockitoException(verificationMode.getClass().getSimpleName() + " is not implemented to work with InOrder wrapped inside a " + verificationWrapper.getClass().getSimpleName());
    }
}
