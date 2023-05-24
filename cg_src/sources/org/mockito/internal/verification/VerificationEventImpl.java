package org.mockito.internal.verification;

import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationEvent;
import org.mockito.verification.VerificationMode;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/verification/VerificationEventImpl.class */
public class VerificationEventImpl implements VerificationEvent {
    private final Object mock;
    private final VerificationMode mode;
    private final VerificationData data;
    private final Throwable cause;

    public VerificationEventImpl(Object mock, VerificationMode mode, VerificationData data, Throwable cause) {
        this.mock = mock;
        this.mode = mode;
        this.data = data;
        this.cause = cause;
    }

    @Override // org.mockito.verification.VerificationEvent
    public Object getMock() {
        return this.mock;
    }

    @Override // org.mockito.verification.VerificationEvent
    public VerificationMode getMode() {
        return this.mode;
    }

    @Override // org.mockito.verification.VerificationEvent
    public VerificationData getData() {
        return this.data;
    }

    @Override // org.mockito.verification.VerificationEvent
    public Throwable getVerificationError() {
        return this.cause;
    }
}
