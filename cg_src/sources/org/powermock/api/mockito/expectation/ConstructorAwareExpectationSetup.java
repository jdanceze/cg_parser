package org.powermock.api.mockito.expectation;

import java.lang.reflect.Constructor;
import org.mockito.stubbing.OngoingStubbing;
/* loaded from: gencallgraphv3.jar:powermock-api-mockito2-2.0.9.jar:org/powermock/api/mockito/expectation/ConstructorAwareExpectationSetup.class */
public class ConstructorAwareExpectationSetup<T> implements WithOrWithoutExpectedArguments<T> {
    private final Constructor<T> ctor;
    private final DefaultConstructorExpectationSetup<T> expectationSetup;

    public ConstructorAwareExpectationSetup(Constructor<T> ctor) {
        if (ctor == null) {
            throw new IllegalArgumentException("Constructor to expect cannot be null");
        }
        this.ctor = ctor;
        this.expectationSetup = setupExpectation();
    }

    @Override // org.powermock.api.mockito.expectation.WithExpectedArguments
    public OngoingStubbing<T> withArguments(Object firstArgument, Object... additionalArguments) throws Exception {
        return this.expectationSetup.withArguments(firstArgument, additionalArguments);
    }

    @Override // org.powermock.api.mockito.expectation.WithoutExpectedArguments
    public OngoingStubbing<T> withNoArguments() throws Exception {
        return this.expectationSetup.withNoArguments();
    }

    private DefaultConstructorExpectationSetup<T> setupExpectation() {
        DefaultConstructorExpectationSetup<T> setup = new DefaultConstructorExpectationSetup<>(this.ctor.getDeclaringClass());
        setup.setParameterTypes(this.ctor.getParameterTypes());
        return setup;
    }
}
