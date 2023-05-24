package org.mockito.creation.instance;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/creation/instance/Instantiator.class */
public interface Instantiator {
    <T> T newInstance(Class<T> cls) throws InstantiationException;
}
