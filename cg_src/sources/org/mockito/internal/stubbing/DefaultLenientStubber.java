package org.mockito.internal.stubbing;

import org.mockito.internal.MockitoCore;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.LenientStubber;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stubber;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/stubbing/DefaultLenientStubber.class */
public class DefaultLenientStubber implements LenientStubber {
    private static final MockitoCore MOCKITO_CORE = new MockitoCore();

    @Override // org.mockito.stubbing.BaseStubber
    public Stubber doThrow(Throwable... toBeThrown) {
        return stubber().doThrow(toBeThrown);
    }

    @Override // org.mockito.stubbing.BaseStubber
    public Stubber doThrow(Class<? extends Throwable> toBeThrown) {
        return stubber().doThrow(toBeThrown);
    }

    @Override // org.mockito.stubbing.BaseStubber
    public Stubber doThrow(Class<? extends Throwable> toBeThrown, Class<? extends Throwable>... nextToBeThrown) {
        return stubber().doThrow(toBeThrown, nextToBeThrown);
    }

    @Override // org.mockito.stubbing.BaseStubber
    public Stubber doAnswer(Answer answer) {
        return stubber().doAnswer(answer);
    }

    @Override // org.mockito.stubbing.BaseStubber
    public Stubber doNothing() {
        return stubber().doNothing();
    }

    @Override // org.mockito.stubbing.BaseStubber
    public Stubber doReturn(Object toBeReturned) {
        return stubber().doReturn(toBeReturned);
    }

    @Override // org.mockito.stubbing.BaseStubber
    public Stubber doReturn(Object toBeReturned, Object... nextToBeReturned) {
        return stubber().doReturn(toBeReturned, nextToBeReturned);
    }

    @Override // org.mockito.stubbing.BaseStubber
    public Stubber doCallRealMethod() {
        return stubber().doCallRealMethod();
    }

    @Override // org.mockito.stubbing.LenientStubber
    public <T> OngoingStubbing<T> when(T methodCall) {
        OngoingStubbingImpl<T> ongoingStubbing = (OngoingStubbingImpl) MOCKITO_CORE.when(methodCall);
        ongoingStubbing.setStrictness(Strictness.LENIENT);
        return ongoingStubbing;
    }

    private static Stubber stubber() {
        return MOCKITO_CORE.stubber(Strictness.LENIENT);
    }
}
