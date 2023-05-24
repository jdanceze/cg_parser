package org.mockito.verification;

import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.verification.VerificationWrapper;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/verification/After.class */
public class After extends VerificationWrapper<VerificationOverTimeImpl> implements VerificationAfterDelay {
    public After(long delayMillis, VerificationMode verificationMode) {
        this(10L, delayMillis, verificationMode);
    }

    After(long pollingPeriod, long delayMillis, VerificationMode verificationMode) {
        this(new VerificationOverTimeImpl(pollingPeriod, delayMillis, verificationMode, false));
    }

    After(VerificationOverTimeImpl verificationOverTime) {
        super(verificationOverTime);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.mockito.internal.verification.VerificationWrapper
    public VerificationMode copySelfWithNewVerificationMode(VerificationMode verificationMode) {
        return new After(((VerificationOverTimeImpl) this.wrappedVerification).copyWithVerificationMode(verificationMode));
    }
}
