package org.mockito.verification;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/verification/VerificationAfterDelay.class */
public interface VerificationAfterDelay extends VerificationMode {
    VerificationMode times(int i);

    VerificationMode never();

    VerificationMode atLeastOnce();

    VerificationMode atLeast(int i);

    VerificationMode atMostOnce();

    VerificationMode atMost(int i);

    VerificationMode only();
}
