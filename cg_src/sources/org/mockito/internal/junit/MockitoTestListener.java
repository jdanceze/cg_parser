package org.mockito.internal.junit;

import org.mockito.listeners.MockCreationListener;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/junit/MockitoTestListener.class */
public interface MockitoTestListener extends MockCreationListener {
    void testFinished(TestFinishedEvent testFinishedEvent);
}
