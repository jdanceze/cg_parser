package org.mockito.listeners;

import org.mockito.mock.MockCreationSettings;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/listeners/MockCreationListener.class */
public interface MockCreationListener extends MockitoListener {
    void onMockCreated(Object obj, MockCreationSettings mockCreationSettings);

    default void onStaticMockCreated(Class<?> mock, MockCreationSettings settings) {
    }
}
