package org.mockito.internal.handler;

import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/handler/MockHandlerFactory.class */
public class MockHandlerFactory {
    public static <T> MockHandler<T> createMockHandler(MockCreationSettings<T> settings) {
        MockHandler<T> handler = new MockHandlerImpl<>(settings);
        MockHandler<T> nullResultGuardian = new NullResultGuardian<>(handler);
        return new InvocationNotifierHandler(nullResultGuardian, settings);
    }
}
