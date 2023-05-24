package org.mockito.verification;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/verification/VerificationWithTimeout.class */
public interface VerificationWithTimeout extends VerificationMode {
    VerificationMode times(int i);

    VerificationMode atLeastOnce();

    VerificationMode atLeast(int i);

    VerificationMode only();
}
