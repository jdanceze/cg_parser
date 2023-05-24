package org.powermock.api.support.membermodification.strategy.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import org.powermock.api.support.MethodProxy;
import org.powermock.api.support.Stubber;
import org.powermock.api.support.membermodification.strategy.MethodStubStrategy;
/* loaded from: gencallgraphv3.jar:powermock-api-support-2.0.9.jar:org/powermock/api/support/membermodification/strategy/impl/MethodStubStrategyImpl.class */
public class MethodStubStrategyImpl<T> implements MethodStubStrategy<T> {
    private final Method method;

    public MethodStubStrategyImpl(Method method) {
        if (method == null) {
            throw new IllegalArgumentException("Method to stub cannot be null.");
        }
        this.method = method;
    }

    @Override // org.powermock.api.support.membermodification.strategy.MethodStubStrategy
    @Deprecated
    public void andReturn(T returnValue) {
        toReturn(returnValue);
    }

    @Override // org.powermock.api.support.membermodification.strategy.MethodStubStrategy
    public void toThrow(final Throwable throwable) {
        InvocationHandler throwingInvocationHandler = new InvocationHandler() { // from class: org.powermock.api.support.membermodification.strategy.impl.MethodStubStrategyImpl.1
            @Override // java.lang.reflect.InvocationHandler
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                throw throwable;
            }
        };
        MethodProxy.proxy(this.method, throwingInvocationHandler);
    }

    @Override // org.powermock.api.support.membermodification.strategy.MethodStubStrategy
    public void toReturn(T returnValue) {
        Stubber.stubMethod(this.method, returnValue);
    }
}
