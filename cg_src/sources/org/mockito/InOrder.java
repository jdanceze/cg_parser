package org.mockito;

import org.mockito.verification.VerificationMode;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/InOrder.class */
public interface InOrder {
    <T> T verify(T t);

    <T> T verify(T t, VerificationMode verificationMode);

    void verifyNoMoreInteractions();
}
