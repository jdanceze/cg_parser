package org.powermock.api.support.membermodification.strategy;
/* loaded from: gencallgraphv3.jar:powermock-api-support-2.0.9.jar:org/powermock/api/support/membermodification/strategy/MethodStubStrategy.class */
public interface MethodStubStrategy<T> {
    @Deprecated
    void andReturn(T t);

    void toReturn(T t);

    void toThrow(Throwable th);
}
