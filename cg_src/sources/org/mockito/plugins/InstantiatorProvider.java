package org.mockito.plugins;

import org.mockito.internal.creation.instance.Instantiator;
import org.mockito.mock.MockCreationSettings;
@Deprecated
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/plugins/InstantiatorProvider.class */
public interface InstantiatorProvider {
    @Deprecated
    Instantiator getInstantiator(MockCreationSettings<?> mockCreationSettings);
}
