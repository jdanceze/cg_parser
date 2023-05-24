package org.mockito;

import org.mockito.stubbing.OngoingStubbing;
import org.mockito.verification.VerificationMode;
@Incubating
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/MockedStatic.class */
public interface MockedStatic<T> extends ScopedMock {

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/MockedStatic$Verification.class */
    public interface Verification {
        void apply() throws Throwable;
    }

    <S> OngoingStubbing<S> when(Verification verification);

    void verify(VerificationMode verificationMode, Verification verification);

    void reset();

    void clearInvocations();

    void verifyNoMoreInteractions();

    void verifyNoInteractions();

    default void verify(Verification verification) {
        verify(Mockito.times(1), verification);
    }
}
