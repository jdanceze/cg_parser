package org.mockito.internal.verification;

import java.util.Set;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.listeners.VerificationListener;
import org.mockito.verification.VerificationEvent;
import org.mockito.verification.VerificationMode;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/verification/MockAwareVerificationMode.class */
public class MockAwareVerificationMode implements VerificationMode {
    private final Object mock;
    private final VerificationMode mode;
    private final Set<VerificationListener> listeners;

    public MockAwareVerificationMode(Object mock, VerificationMode mode, Set<VerificationListener> listeners) {
        this.mock = mock;
        this.mode = mode;
        this.listeners = listeners;
    }

    @Override // org.mockito.verification.VerificationMode
    public void verify(VerificationData data) {
        try {
            this.mode.verify(data);
            notifyListeners(new VerificationEventImpl(this.mock, this.mode, data, null));
        } catch (Error e) {
            notifyListeners(new VerificationEventImpl(this.mock, this.mode, data, e));
            throw e;
        } catch (RuntimeException e2) {
            notifyListeners(new VerificationEventImpl(this.mock, this.mode, data, e2));
            throw e2;
        }
    }

    private void notifyListeners(VerificationEvent event) {
        for (VerificationListener listener : this.listeners) {
            listener.onVerification(event);
        }
    }

    public Object getMock() {
        return this.mock;
    }
}
