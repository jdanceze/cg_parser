package org.mockito.internal.verification;

import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/verification/VerificationOverTimeImpl.class */
public class VerificationOverTimeImpl implements VerificationMode {
    private final long pollingPeriodMillis;
    private final VerificationMode delegate;
    private final boolean returnOnSuccess;
    private final Timer timer;

    public VerificationOverTimeImpl(long pollingPeriodMillis, long durationMillis, VerificationMode delegate, boolean returnOnSuccess) {
        this(pollingPeriodMillis, delegate, returnOnSuccess, new Timer(durationMillis));
    }

    public VerificationOverTimeImpl(long pollingPeriodMillis, VerificationMode delegate, boolean returnOnSuccess, Timer timer) {
        this.pollingPeriodMillis = pollingPeriodMillis;
        this.delegate = delegate;
        this.returnOnSuccess = returnOnSuccess;
        this.timer = timer;
    }

    @Override // org.mockito.verification.VerificationMode
    public void verify(VerificationData data) {
        AssertionError error = null;
        this.timer.start();
        while (this.timer.isCounting()) {
            try {
                this.delegate.verify(data);
            } catch (MockitoAssertionError e) {
                error = handleVerifyException(e);
            } catch (AssertionError e2) {
                error = handleVerifyException(e2);
            }
            if (this.returnOnSuccess) {
                return;
            }
            error = null;
        }
        if (error != null) {
            throw error;
        }
    }

    private AssertionError handleVerifyException(AssertionError e) {
        if (canRecoverFromFailure(this.delegate)) {
            sleep(this.pollingPeriodMillis);
            return e;
        }
        throw e;
    }

    protected boolean canRecoverFromFailure(VerificationMode verificationMode) {
        return ((verificationMode instanceof AtMost) || (verificationMode instanceof NoMoreInteractions)) ? false : true;
    }

    public VerificationOverTimeImpl copyWithVerificationMode(VerificationMode verificationMode) {
        return new VerificationOverTimeImpl(this.pollingPeriodMillis, this.timer.duration(), verificationMode, this.returnOnSuccess);
    }

    private void sleep(long sleep) {
        try {
            Thread.sleep(sleep);
        } catch (InterruptedException ie) {
            throw new RuntimeException("Thread sleep has been interrupted", ie);
        }
    }

    public boolean isReturnOnSuccess() {
        return this.returnOnSuccess;
    }

    public long getPollingPeriodMillis() {
        return this.pollingPeriodMillis;
    }

    public Timer getTimer() {
        return this.timer;
    }

    public VerificationMode getDelegate() {
        return this.delegate;
    }
}
