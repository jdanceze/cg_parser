package org.powermock.api.mockito.internal.expectation;

import java.lang.reflect.Method;
import org.powermock.api.mockito.expectation.PrivatelyExpectedArguments;
/* loaded from: gencallgraphv3.jar:powermock-api-mockito2-2.0.9.jar:org/powermock/api/mockito/internal/expectation/DefaultPrivatelyExpectedArguments.class */
public class DefaultPrivatelyExpectedArguments implements PrivatelyExpectedArguments {
    private final Method method;
    private final Object mock;

    public DefaultPrivatelyExpectedArguments(Object mock, Method method) {
        this.mock = mock;
        this.method = method;
        method.setAccessible(true);
    }

    @Override // org.powermock.api.mockito.expectation.PrivatelyExpectedArguments
    public <T> void withArguments(Object firstArgument, Object... additionalArguments) throws Exception {
        if (additionalArguments == null || additionalArguments.length == 0) {
            this.method.invoke(this.mock, firstArgument);
            return;
        }
        Object[] allArgs = new Object[additionalArguments.length + 1];
        allArgs[0] = firstArgument;
        if (additionalArguments.length > 0) {
            System.arraycopy(additionalArguments, 0, allArgs, 1, additionalArguments.length);
        }
        this.method.invoke(this.mock, allArgs);
    }

    @Override // org.powermock.api.mockito.expectation.PrivatelyExpectedArguments
    public <T> void withNoArguments() throws Exception {
        this.method.invoke(this.mock, new Object[0]);
    }
}
