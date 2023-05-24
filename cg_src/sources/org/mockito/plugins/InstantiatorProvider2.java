package org.mockito.plugins;

import org.mockito.creation.instance.Instantiator;
import org.mockito.mock.MockCreationSettings;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/plugins/InstantiatorProvider2.class */
public interface InstantiatorProvider2 {
    Instantiator getInstantiator(MockCreationSettings<?> mockCreationSettings);
}
