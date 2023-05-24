package org.mockito.verification;

import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.internal.verification.api.VerificationData;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/verification/VerificationMode.class */
public interface VerificationMode {
    void verify(VerificationData verificationData);

    default VerificationMode description(String description) {
        return VerificationModeFactory.description(this, description);
    }
}
