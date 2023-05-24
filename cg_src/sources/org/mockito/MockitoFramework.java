package org.mockito;

import org.mockito.exceptions.misusing.RedundantListenerException;
import org.mockito.invocation.InvocationFactory;
import org.mockito.listeners.MockitoListener;
import org.mockito.plugins.MockitoPlugins;
@NotExtensible
@Incubating
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/MockitoFramework.class */
public interface MockitoFramework {
    @Incubating
    MockitoFramework addListener(MockitoListener mockitoListener) throws RedundantListenerException;

    @Incubating
    MockitoFramework removeListener(MockitoListener mockitoListener);

    @Incubating
    MockitoPlugins getPlugins();

    @Incubating
    InvocationFactory getInvocationFactory();

    @Incubating
    void clearInlineMocks();

    @Incubating
    void clearInlineMock(Object obj);
}
