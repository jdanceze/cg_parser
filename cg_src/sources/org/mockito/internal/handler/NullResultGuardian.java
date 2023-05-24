package org.mockito.internal.handler;

import org.mockito.internal.util.Primitives;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationContainer;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/handler/NullResultGuardian.class */
public class NullResultGuardian<T> implements MockHandler<T> {
    private final MockHandler<T> delegate;

    public NullResultGuardian(MockHandler<T> delegate) {
        this.delegate = delegate;
    }

    @Override // org.mockito.invocation.MockHandler
    public Object handle(Invocation invocation) throws Throwable {
        Object result = this.delegate.handle(invocation);
        Class<?> returnType = invocation.getMethod().getReturnType();
        if (result == null && returnType.isPrimitive()) {
            return Primitives.defaultValue(returnType);
        }
        return result;
    }

    @Override // org.mockito.invocation.MockHandler
    public MockCreationSettings<T> getMockSettings() {
        return this.delegate.getMockSettings();
    }

    @Override // org.mockito.invocation.MockHandler
    public InvocationContainer getInvocationContainer() {
        return this.delegate.getInvocationContainer();
    }
}
