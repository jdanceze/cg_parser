package org.mockito.internal.creation.instance;

import org.mockito.internal.configuration.GlobalConfiguration;
import org.objenesis.ObjenesisStd;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/instance/ObjenesisInstantiator.class */
class ObjenesisInstantiator implements org.mockito.creation.instance.Instantiator {
    private final ObjenesisStd objenesis = new ObjenesisStd(new GlobalConfiguration().enableClassCache());

    @Override // org.mockito.creation.instance.Instantiator
    public <T> T newInstance(Class<T> cls) {
        return (T) this.objenesis.newInstance(cls);
    }
}
