package org.mockito.internal.creation.instance;

import org.mockito.mock.MockCreationSettings;
import org.mockito.plugins.InstantiatorProvider2;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/instance/DefaultInstantiatorProvider.class */
public class DefaultInstantiatorProvider implements InstantiatorProvider2 {
    private static final org.mockito.creation.instance.Instantiator INSTANCE = new ObjenesisInstantiator();

    @Override // org.mockito.plugins.InstantiatorProvider2
    public org.mockito.creation.instance.Instantiator getInstantiator(MockCreationSettings<?> settings) {
        if (settings != null && settings.getConstructorArgs() != null) {
            return new ConstructorInstantiator(settings.getOuterClassInstance() != null, settings.getConstructorArgs());
        }
        return INSTANCE;
    }
}
