package org.powermock.api.support.membermodification.strategy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
/* loaded from: gencallgraphv3.jar:powermock-api-support-2.0.9.jar:org/powermock/api/support/membermodification/strategy/MethodReplaceStrategy.class */
public interface MethodReplaceStrategy {
    void with(Method method);

    void with(InvocationHandler invocationHandler);
}
