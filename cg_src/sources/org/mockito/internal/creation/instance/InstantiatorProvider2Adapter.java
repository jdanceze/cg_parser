package org.mockito.internal.creation.instance;

import org.mockito.mock.MockCreationSettings;
import org.mockito.plugins.InstantiatorProvider;
import org.mockito.plugins.InstantiatorProvider2;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/instance/InstantiatorProvider2Adapter.class */
public class InstantiatorProvider2Adapter implements InstantiatorProvider {
    private final InstantiatorProvider2 provider;

    public InstantiatorProvider2Adapter(InstantiatorProvider2 provider) {
        this.provider = provider;
    }

    @Override // org.mockito.plugins.InstantiatorProvider
    public Instantiator getInstantiator(final MockCreationSettings<?> settings) {
        return new Instantiator() { // from class: org.mockito.internal.creation.instance.InstantiatorProvider2Adapter.1
            @Override // org.mockito.internal.creation.instance.Instantiator
            public <T> T newInstance(Class<T> cls) throws InstantiationException {
                try {
                    return (T) InstantiatorProvider2Adapter.this.provider.getInstantiator(settings).newInstance(cls);
                } catch (org.mockito.creation.instance.InstantiationException e) {
                    throw new InstantiationException(e.getMessage(), e.getCause());
                }
            }
        };
    }
}
