package org.mockito.listeners;

import org.mockito.Incubating;
import org.mockito.verification.VerificationEvent;
@Incubating
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/listeners/VerificationListener.class */
public interface VerificationListener extends MockitoListener {
    void onVerification(VerificationEvent verificationEvent);
}
