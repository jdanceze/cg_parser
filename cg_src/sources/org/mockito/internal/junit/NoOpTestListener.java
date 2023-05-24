package org.mockito.internal.junit;

import org.mockito.mock.MockCreationSettings;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/junit/NoOpTestListener.class */
public class NoOpTestListener implements MockitoTestListener {
    @Override // org.mockito.internal.junit.MockitoTestListener
    public void testFinished(TestFinishedEvent event) {
    }

    @Override // org.mockito.listeners.MockCreationListener
    public void onMockCreated(Object mock, MockCreationSettings settings) {
    }
}
