package org.powermock.api.mockito.internal.expectation;

import java.lang.reflect.Constructor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;
import org.powermock.core.spi.support.InvocationSubstitute;
/* loaded from: gencallgraphv3.jar:powermock-api-mockito2-2.0.9.jar:org/powermock/api/mockito/internal/expectation/DelegatingToConstructorsOngoingStubbing.class */
public class DelegatingToConstructorsOngoingStubbing<T> implements OngoingStubbing<T> {
    private final OngoingStubbing<T> stubbing;
    private final Constructor<?>[] ctors;

    public DelegatingToConstructorsOngoingStubbing(Constructor<?>[] ctors, OngoingStubbing<T> stubbing) {
        if (stubbing == null) {
            throw new IllegalArgumentException("Internal error: Ongoing stubbing must be provided");
        }
        this.ctors = ctors;
        this.stubbing = stubbing;
    }

    @Override // org.mockito.stubbing.OngoingStubbing
    public OngoingStubbing<T> thenReturn(final T value) {
        this.stubbing.thenReturn(value);
        return new DelegatingToConstructorsOngoingStubbing<T>.InvokeStubMethod() { // from class: org.powermock.api.mockito.internal.expectation.DelegatingToConstructorsOngoingStubbing.1
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // org.powermock.api.mockito.internal.expectation.DelegatingToConstructorsOngoingStubbing.InvokeStubMethod
            public void performStubbing(OngoingStubbing<T> when) {
                when.thenReturn(value);
            }
        }.invoke();
    }

    @Override // org.mockito.stubbing.OngoingStubbing
    public OngoingStubbing<T> thenReturn(final T value, final T... values) {
        this.stubbing.thenReturn(value, values);
        return new DelegatingToConstructorsOngoingStubbing<T>.InvokeStubMethod() { // from class: org.powermock.api.mockito.internal.expectation.DelegatingToConstructorsOngoingStubbing.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // org.powermock.api.mockito.internal.expectation.DelegatingToConstructorsOngoingStubbing.InvokeStubMethod
            public void performStubbing(OngoingStubbing<T> when) {
                when.thenReturn(value, values);
            }
        }.invoke();
    }

    @Override // org.mockito.stubbing.OngoingStubbing
    public OngoingStubbing<T> thenThrow(final Throwable... throwables) {
        this.stubbing.thenThrow(throwables);
        return new DelegatingToConstructorsOngoingStubbing<T>.InvokeStubMethod() { // from class: org.powermock.api.mockito.internal.expectation.DelegatingToConstructorsOngoingStubbing.3
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // org.powermock.api.mockito.internal.expectation.DelegatingToConstructorsOngoingStubbing.InvokeStubMethod
            public void performStubbing(OngoingStubbing<T> when) {
                when.thenThrow(throwables);
            }
        }.invoke();
    }

    @Override // org.mockito.stubbing.OngoingStubbing
    public OngoingStubbing<T> thenThrow(final Class<? extends Throwable> throwableType) {
        this.stubbing.thenThrow(throwableType);
        return new DelegatingToConstructorsOngoingStubbing<T>.InvokeStubMethod() { // from class: org.powermock.api.mockito.internal.expectation.DelegatingToConstructorsOngoingStubbing.4
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // org.powermock.api.mockito.internal.expectation.DelegatingToConstructorsOngoingStubbing.InvokeStubMethod
            public void performStubbing(OngoingStubbing<T> when) {
                when.thenThrow(throwableType);
            }
        }.invoke();
    }

    @Override // org.mockito.stubbing.OngoingStubbing
    public OngoingStubbing<T> thenThrow(final Class<? extends Throwable> toBeThrown, final Class<? extends Throwable>[] nextToBeThrown) {
        this.stubbing.thenThrow(toBeThrown, nextToBeThrown);
        return new DelegatingToConstructorsOngoingStubbing<T>.InvokeStubMethod() { // from class: org.powermock.api.mockito.internal.expectation.DelegatingToConstructorsOngoingStubbing.5
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // org.powermock.api.mockito.internal.expectation.DelegatingToConstructorsOngoingStubbing.InvokeStubMethod
            public void performStubbing(OngoingStubbing<T> when) {
                when.thenThrow(toBeThrown, nextToBeThrown);
            }
        }.invoke();
    }

    @Override // org.mockito.stubbing.OngoingStubbing
    public OngoingStubbing<T> thenCallRealMethod() {
        this.stubbing.thenCallRealMethod();
        return new DelegatingToConstructorsOngoingStubbing<T>.InvokeStubMethod() { // from class: org.powermock.api.mockito.internal.expectation.DelegatingToConstructorsOngoingStubbing.6
            @Override // org.powermock.api.mockito.internal.expectation.DelegatingToConstructorsOngoingStubbing.InvokeStubMethod
            public void performStubbing(OngoingStubbing<T> when) {
                when.thenCallRealMethod();
            }
        }.invoke();
    }

    @Override // org.mockito.stubbing.OngoingStubbing
    public OngoingStubbing<T> thenAnswer(final Answer<?> answer) {
        this.stubbing.thenAnswer(answer);
        return new DelegatingToConstructorsOngoingStubbing<T>.InvokeStubMethod() { // from class: org.powermock.api.mockito.internal.expectation.DelegatingToConstructorsOngoingStubbing.7
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // org.powermock.api.mockito.internal.expectation.DelegatingToConstructorsOngoingStubbing.InvokeStubMethod
            public void performStubbing(OngoingStubbing<T> when) {
                when.thenAnswer(answer);
            }
        }.invoke();
    }

    @Override // org.mockito.stubbing.OngoingStubbing
    public OngoingStubbing<T> then(final Answer<?> answer) {
        this.stubbing.then(answer);
        return new DelegatingToConstructorsOngoingStubbing<T>.InvokeStubMethod() { // from class: org.powermock.api.mockito.internal.expectation.DelegatingToConstructorsOngoingStubbing.8
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // org.powermock.api.mockito.internal.expectation.DelegatingToConstructorsOngoingStubbing.InvokeStubMethod
            public void performStubbing(OngoingStubbing<T> when) {
                when.then(answer);
            }
        }.invoke();
    }

    @Override // org.mockito.stubbing.OngoingStubbing
    public <M> M getMock() {
        return (M) this.stubbing.getMock();
    }

    /* loaded from: gencallgraphv3.jar:powermock-api-mockito2-2.0.9.jar:org/powermock/api/mockito/internal/expectation/DelegatingToConstructorsOngoingStubbing$InvokeStubMethod.class */
    private abstract class InvokeStubMethod {
        public abstract void performStubbing(OngoingStubbing<T> ongoingStubbing);

        private InvokeStubMethod() {
        }

        public OngoingStubbing<T> invoke() {
            Constructor<?>[] constructorArr;
            InvocationSubstitute<T> mock = (InvocationSubstitute) DelegatingToConstructorsOngoingStubbing.this.stubbing.getMock();
            for (Constructor<?> constructor : DelegatingToConstructorsOngoingStubbing.this.ctors) {
                Class<?>[] parameterTypesForCtor = constructor.getParameterTypes();
                Object[] paramArgs = new Object[parameterTypesForCtor.length];
                for (int i = 0; i < parameterTypesForCtor.length; i++) {
                    Class<?> paramType = parameterTypesForCtor[i];
                    paramArgs[i] = ArgumentMatchers.nullable(paramType);
                }
                try {
                    OngoingStubbing<T> when = Mockito.when(mock.performSubstitutionLogic(paramArgs));
                    performStubbing(when);
                } catch (Exception e) {
                    throw new RuntimeException("PowerMock internal error", e);
                }
            }
            return DelegatingToConstructorsOngoingStubbing.this.stubbing;
        }
    }
}
