package org.mockito.internal.stubbing;

import org.mockito.internal.exceptions.Reporter;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.answers.CallsRealMethods;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.internal.stubbing.answers.ThrowsException;
import org.mockito.internal.stubbing.answers.ThrowsExceptionForClassType;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/stubbing/BaseStubbing.class */
public abstract class BaseStubbing<T> implements OngoingStubbing<T> {
    private final Object strongMockRef;

    /* JADX INFO: Access modifiers changed from: package-private */
    public BaseStubbing(Object mock) {
        this.strongMockRef = mock;
    }

    @Override // org.mockito.stubbing.OngoingStubbing
    public OngoingStubbing<T> then(Answer<?> answer) {
        return thenAnswer(answer);
    }

    @Override // org.mockito.stubbing.OngoingStubbing
    public OngoingStubbing<T> thenReturn(T value) {
        return thenAnswer(new Returns(value));
    }

    @Override // org.mockito.stubbing.OngoingStubbing
    public OngoingStubbing<T> thenReturn(T value, T... values) {
        OngoingStubbing<T> stubbing = thenReturn(value);
        if (values == null) {
            return stubbing.thenReturn(null);
        }
        for (T v : values) {
            stubbing = stubbing.thenReturn(v);
        }
        return stubbing;
    }

    private OngoingStubbing<T> thenThrow(Throwable throwable) {
        return thenAnswer(new ThrowsException(throwable));
    }

    @Override // org.mockito.stubbing.OngoingStubbing
    public OngoingStubbing<T> thenThrow(Throwable... throwables) {
        OngoingStubbing<T> thenThrow;
        if (throwables == null) {
            return thenThrow((Throwable) null);
        }
        OngoingStubbing<T> stubbing = null;
        for (Throwable t : throwables) {
            if (stubbing == null) {
                thenThrow = thenThrow(t);
            } else {
                thenThrow = stubbing.thenThrow(t);
            }
            stubbing = thenThrow;
        }
        return stubbing;
    }

    @Override // org.mockito.stubbing.OngoingStubbing
    public OngoingStubbing<T> thenThrow(Class<? extends Throwable> throwableType) {
        if (throwableType == null) {
            ThreadSafeMockingProgress.mockingProgress().reset();
            throw Reporter.notAnException();
        }
        return thenAnswer(new ThrowsExceptionForClassType(throwableType));
    }

    @Override // org.mockito.stubbing.OngoingStubbing
    public OngoingStubbing<T> thenThrow(Class<? extends Throwable> toBeThrown, Class<? extends Throwable>... nextToBeThrown) {
        if (nextToBeThrown == null) {
            return thenThrow((Class<? extends Throwable>) null);
        }
        OngoingStubbing<T> stubbing = thenThrow(toBeThrown);
        for (Class<? extends Throwable> t : nextToBeThrown) {
            stubbing = stubbing.thenThrow(t);
        }
        return stubbing;
    }

    @Override // org.mockito.stubbing.OngoingStubbing
    public OngoingStubbing<T> thenCallRealMethod() {
        return thenAnswer(new CallsRealMethods());
    }

    @Override // org.mockito.stubbing.OngoingStubbing
    public <M> M getMock() {
        return (M) this.strongMockRef;
    }
}
