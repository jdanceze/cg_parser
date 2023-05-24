package org.mockito.verification;

import org.mockito.Incubating;
import org.mockito.internal.verification.api.VerificationData;
@Incubating
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/verification/VerificationEvent.class */
public interface VerificationEvent {
    Object getMock();

    VerificationMode getMode();

    VerificationData getData();

    Throwable getVerificationError();
}
