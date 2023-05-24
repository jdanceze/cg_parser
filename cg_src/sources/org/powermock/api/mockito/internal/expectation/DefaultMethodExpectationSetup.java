package org.powermock.api.mockito.internal.expectation;

import java.lang.reflect.Method;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.powermock.api.mockito.expectation.WithOrWithoutExpectedArguments;
/* loaded from: gencallgraphv3.jar:powermock-api-mockito2-2.0.9.jar:org/powermock/api/mockito/internal/expectation/DefaultMethodExpectationSetup.class */
public class DefaultMethodExpectationSetup<T> implements WithOrWithoutExpectedArguments<T> {
    private final Object object;
    private final Method method;

    public DefaultMethodExpectationSetup(Object object, Method method) {
        if (object == null) {
            throw new IllegalArgumentException("object to expect cannot be null");
        }
        if (method == null) {
            throw new IllegalArgumentException("method to expect cannot be null");
        }
        this.object = object;
        this.method = method;
        this.method.setAccessible(true);
    }

    private static Object[] join(Object o, Object[] array) {
        Object[] res = new Object[array.length + 1];
        res[0] = o;
        System.arraycopy(array, 0, res, 1, array.length);
        return res;
    }

    @Override // org.powermock.api.mockito.expectation.WithExpectedArguments
    public OngoingStubbing<T> withArguments(Object firstArgument, Object... additionalArguments) throws Exception {
        return (additionalArguments == null || additionalArguments.length == 0) ? Mockito.when(this.method.invoke(this.object, firstArgument)) : Mockito.when(this.method.invoke(this.object, join(firstArgument, additionalArguments)));
    }

    @Override // org.powermock.api.mockito.expectation.WithoutExpectedArguments
    public OngoingStubbing<T> withNoArguments() throws Exception {
        return Mockito.when(this.method.invoke(this.object, new Object[0]));
    }
}
