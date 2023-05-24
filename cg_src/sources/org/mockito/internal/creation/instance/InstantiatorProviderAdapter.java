package org.mockito.internal.creation.instance;

import org.mockito.mock.MockCreationSettings;
import org.mockito.plugins.InstantiatorProvider;
import org.mockito.plugins.InstantiatorProvider2;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/instance/InstantiatorProviderAdapter.class */
public class InstantiatorProviderAdapter implements InstantiatorProvider2 {
    private final InstantiatorProvider provider;

    public InstantiatorProviderAdapter(InstantiatorProvider provider) {
        this.provider = provider;
    }

    @Override // org.mockito.plugins.InstantiatorProvider2
    public org.mockito.creation.instance.Instantiator getInstantiator(final MockCreationSettings<?> settings) {
        return new org.mockito.creation.instance.Instantiator() { // from class: org.mockito.internal.creation.instance.InstantiatorProviderAdapter.1
            @Override // org.mockito.creation.instance.Instantiator
            public <T> T newInstance(Class<T> cls) throws org.mockito.creation.instance.InstantiationException {
                try {
                    return (T) InstantiatorProviderAdapter.this.provider.getInstantiator(settings).newInstance(cls);
                } catch (InstantiationException e) {
                    throw new org.mockito.creation.instance.InstantiationException(e.getMessage(), e.getCause());
                }
            }
        };
    }
}
