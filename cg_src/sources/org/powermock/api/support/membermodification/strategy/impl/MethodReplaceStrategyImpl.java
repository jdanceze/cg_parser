package org.powermock.api.support.membermodification.strategy.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.powermock.api.support.MethodProxy;
import org.powermock.api.support.membermodification.strategy.MethodReplaceStrategy;
import org.powermock.reflect.internal.WhiteboxImpl;
/* loaded from: gencallgraphv3.jar:powermock-api-support-2.0.9.jar:org/powermock/api/support/membermodification/strategy/impl/MethodReplaceStrategyImpl.class */
public class MethodReplaceStrategyImpl implements MethodReplaceStrategy {
    private final Method method;

    public MethodReplaceStrategyImpl(Method method) {
        if (method == null) {
            throw new IllegalArgumentException("Cannot replace a null method.");
        }
        this.method = method;
    }

    @Override // org.powermock.api.support.membermodification.strategy.MethodReplaceStrategy
    public void with(Method method) {
        if (method == null) {
            throw new IllegalArgumentException("A metod cannot be replaced with null.");
        }
        if (!Modifier.isStatic(this.method.getModifiers())) {
            throw new IllegalArgumentException(String.format("Replace requires static methods, '%s' is not static", this.method));
        }
        if (!Modifier.isStatic(method.getModifiers())) {
            throw new IllegalArgumentException(String.format("Replace requires static methods, '%s' is not static", method));
        }
        if (!this.method.getReturnType().isAssignableFrom(method.getReturnType())) {
            throw new IllegalArgumentException(String.format("The replacing method (%s) needs to return %s and not %s.", method.toString(), this.method.getReturnType().getName(), method.getReturnType().getName()));
        }
        if (!WhiteboxImpl.checkIfParameterTypesAreSame(this.method.isVarArgs(), this.method.getParameterTypes(), method.getParameterTypes())) {
            throw new IllegalArgumentException(String.format("The replacing method, \"%s\", needs to have the same number of parameters of the same type as as method \"%s\".", method.toString(), this.method.toString()));
        }
        MethodProxy.proxy(this.method, new MethodInvocationHandler(method));
    }

    @Override // org.powermock.api.support.membermodification.strategy.MethodReplaceStrategy
    public void with(InvocationHandler invocationHandler) {
        if (invocationHandler == null) {
            throw new IllegalArgumentException("Invocation handler cannot be null");
        }
        MethodProxy.proxy(this.method, invocationHandler);
    }

    /* loaded from: gencallgraphv3.jar:powermock-api-support-2.0.9.jar:org/powermock/api/support/membermodification/strategy/impl/MethodReplaceStrategyImpl$MethodInvocationHandler.class */
    private final class MethodInvocationHandler implements InvocationHandler {
        private final Method methodDelegator;

        public MethodInvocationHandler(Method methodDelegator) {
            this.methodDelegator = methodDelegator;
        }

        @Override // java.lang.reflect.InvocationHandler
        public Object invoke(Object object, Method invokingMethod, Object[] arguments) throws Throwable {
            return this.methodDelegator.invoke(object, arguments);
        }
    }
}
