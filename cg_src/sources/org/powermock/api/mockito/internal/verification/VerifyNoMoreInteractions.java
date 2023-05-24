package org.powermock.api.mockito.internal.verification;

import org.mockito.Mockito;
import org.powermock.api.mockito.internal.invocation.MockitoNewInvocationControl;
import org.powermock.api.mockito.invocation.MockitoMethodInvocationControl;
import org.powermock.core.MockRepository;
/* loaded from: gencallgraphv3.jar:powermock-api-mockito2-2.0.9.jar:org/powermock/api/mockito/internal/verification/VerifyNoMoreInteractions.class */
public class VerifyNoMoreInteractions {
    public static void verifyNoMoreInteractions(Object... objects) {
        for (Object mock : objects) {
            if (mock instanceof Class) {
                verifyNoMoreInteractions((Class<?>[]) new Class[]{(Class) mock});
            } else {
                MockitoMethodInvocationControl invocationControl = (MockitoMethodInvocationControl) MockRepository.getInstanceMethodInvocationControl(mock);
                if (invocationControl != null) {
                    invocationControl.verifyNoMoreInteractions();
                } else {
                    Mockito.verifyNoMoreInteractions(mock);
                }
            }
        }
    }

    private static void verifyNoMoreInteractions(Class<?>... types) {
        for (Class<?> type : types) {
            MockitoMethodInvocationControl invocationHandler = (MockitoMethodInvocationControl) MockRepository.getStaticMethodInvocationControl(type);
            if (invocationHandler != null) {
                invocationHandler.verifyNoMoreInteractions();
            }
            MockitoNewInvocationControl<?> newInvocationControl = (MockitoNewInvocationControl) MockRepository.getNewInstanceControl(type);
            if (newInvocationControl != null) {
                newInvocationControl.verifyNoMoreInteractions();
            }
        }
    }
}
