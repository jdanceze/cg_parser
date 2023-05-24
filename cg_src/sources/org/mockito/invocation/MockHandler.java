package org.mockito.invocation;

import java.io.Serializable;
import org.mockito.Incubating;
import org.mockito.mock.MockCreationSettings;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/invocation/MockHandler.class */
public interface MockHandler<T> extends Serializable {
    Object handle(Invocation invocation) throws Throwable;

    @Incubating
    MockCreationSettings<T> getMockSettings();

    @Incubating
    InvocationContainer getInvocationContainer();
}
