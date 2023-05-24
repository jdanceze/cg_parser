package org.mockito.internal.verification;

import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/verification/VerificationWrapper.class */
public abstract class VerificationWrapper<WrapperType extends VerificationMode> implements VerificationMode {
    protected final WrapperType wrappedVerification;

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract VerificationMode copySelfWithNewVerificationMode(VerificationMode verificationMode);

    public VerificationWrapper(WrapperType wrappedVerification) {
        this.wrappedVerification = wrappedVerification;
    }

    @Override // org.mockito.verification.VerificationMode
    public void verify(VerificationData data) {
        this.wrappedVerification.verify(data);
    }

    public VerificationMode times(int wantedNumberOfInvocations) {
        return copySelfWithNewVerificationMode(VerificationModeFactory.times(wantedNumberOfInvocations));
    }

    public VerificationMode never() {
        return copySelfWithNewVerificationMode(VerificationModeFactory.atMost(0));
    }

    public VerificationMode atLeastOnce() {
        return copySelfWithNewVerificationMode(VerificationModeFactory.atLeastOnce());
    }

    public VerificationMode atLeast(int minNumberOfInvocations) {
        return copySelfWithNewVerificationMode(VerificationModeFactory.atLeast(minNumberOfInvocations));
    }

    public VerificationMode atMostOnce() {
        return copySelfWithNewVerificationMode(VerificationModeFactory.atMostOnce());
    }

    public VerificationMode atMost(int maxNumberOfInvocations) {
        return copySelfWithNewVerificationMode(VerificationModeFactory.atMost(maxNumberOfInvocations));
    }

    public VerificationMode only() {
        return copySelfWithNewVerificationMode(VerificationModeFactory.only());
    }
}
