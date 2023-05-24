package org.mockito.verification;

import org.mockito.internal.exceptions.Reporter;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.verification.VerificationWrapper;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/verification/Timeout.class */
public class Timeout extends VerificationWrapper<VerificationOverTimeImpl> implements VerificationWithTimeout {
    public Timeout(long millis, VerificationMode delegate) {
        this(10L, millis, delegate);
    }

    Timeout(long pollingPeriodMillis, long millis, VerificationMode delegate) {
        this(new VerificationOverTimeImpl(pollingPeriodMillis, millis, delegate, true));
    }

    Timeout(long pollingPeriodMillis, VerificationMode delegate, Timer timer) {
        this(new VerificationOverTimeImpl(pollingPeriodMillis, delegate, true, timer));
    }

    Timeout(VerificationOverTimeImpl verificationOverTime) {
        super(verificationOverTime);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.mockito.internal.verification.VerificationWrapper
    public VerificationMode copySelfWithNewVerificationMode(VerificationMode newVerificationMode) {
        return new Timeout(((VerificationOverTimeImpl) this.wrappedVerification).copyWithVerificationMode(newVerificationMode));
    }

    @Override // org.mockito.internal.verification.VerificationWrapper
    public VerificationMode atMost(int maxNumberOfInvocations) {
        throw Reporter.atMostAndNeverShouldNotBeUsedWithTimeout();
    }

    @Override // org.mockito.internal.verification.VerificationWrapper
    public VerificationMode never() {
        throw Reporter.atMostAndNeverShouldNotBeUsedWithTimeout();
    }
}
