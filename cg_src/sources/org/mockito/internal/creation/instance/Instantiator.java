package org.mockito.internal.creation.instance;
@Deprecated
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/instance/Instantiator.class */
public interface Instantiator {
    <T> T newInstance(Class<T> cls) throws InstantiationException;
}
