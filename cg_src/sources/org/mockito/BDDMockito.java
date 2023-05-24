package org.mockito;

import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stubber;
import org.mockito.verification.VerificationMode;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/BDDMockito.class */
public class BDDMockito extends Mockito {

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/BDDMockito$BDDMyOngoingStubbing.class */
    public interface BDDMyOngoingStubbing<T> {
        BDDMyOngoingStubbing<T> willAnswer(Answer<?> answer);

        BDDMyOngoingStubbing<T> will(Answer<?> answer);

        BDDMyOngoingStubbing<T> willReturn(T t);

        BDDMyOngoingStubbing<T> willReturn(T t, T... tArr);

        BDDMyOngoingStubbing<T> willThrow(Throwable... thArr);

        BDDMyOngoingStubbing<T> willThrow(Class<? extends Throwable> cls);

        BDDMyOngoingStubbing<T> willThrow(Class<? extends Throwable> cls, Class<? extends Throwable>... clsArr);

        BDDMyOngoingStubbing<T> willCallRealMethod();

        <M> M getMock();
    }

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/BDDMockito$BDDStubber.class */
    public interface BDDStubber {
        BDDStubber willAnswer(Answer<?> answer);

        BDDStubber will(Answer<?> answer);

        @Deprecated
        BDDStubber willNothing();

        BDDStubber willDoNothing();

        BDDStubber willReturn(Object obj);

        BDDStubber willReturn(Object obj, Object... objArr);

        BDDStubber willThrow(Throwable... thArr);

        BDDStubber willThrow(Class<? extends Throwable> cls);

        BDDStubber willThrow(Class<? extends Throwable> cls, Class<? extends Throwable>... clsArr);

        BDDStubber willCallRealMethod();

        <T> T given(T t);
    }

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/BDDMockito$Then.class */
    public interface Then<T> {
        T should();

        T should(VerificationMode verificationMode);

        T should(InOrder inOrder);

        T should(InOrder inOrder, VerificationMode verificationMode);

        @Deprecated
        void shouldHaveZeroInteractions();

        void shouldHaveNoMoreInteractions();

        void shouldHaveNoInteractions();
    }

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/BDDMockito$BDDOngoingStubbingImpl.class */
    private static class BDDOngoingStubbingImpl<T> implements BDDMyOngoingStubbing<T> {
        private final OngoingStubbing<T> mockitoOngoingStubbing;

        public BDDOngoingStubbingImpl(OngoingStubbing<T> ongoingStubbing) {
            this.mockitoOngoingStubbing = ongoingStubbing;
        }

        @Override // org.mockito.BDDMockito.BDDMyOngoingStubbing
        public BDDMyOngoingStubbing<T> willAnswer(Answer<?> answer) {
            return new BDDOngoingStubbingImpl(this.mockitoOngoingStubbing.thenAnswer(answer));
        }

        @Override // org.mockito.BDDMockito.BDDMyOngoingStubbing
        public BDDMyOngoingStubbing<T> will(Answer<?> answer) {
            return new BDDOngoingStubbingImpl(this.mockitoOngoingStubbing.then(answer));
        }

        @Override // org.mockito.BDDMockito.BDDMyOngoingStubbing
        public BDDMyOngoingStubbing<T> willReturn(T value) {
            return new BDDOngoingStubbingImpl(this.mockitoOngoingStubbing.thenReturn(value));
        }

        @Override // org.mockito.BDDMockito.BDDMyOngoingStubbing
        public BDDMyOngoingStubbing<T> willReturn(T value, T... values) {
            return new BDDOngoingStubbingImpl(this.mockitoOngoingStubbing.thenReturn(value, values));
        }

        @Override // org.mockito.BDDMockito.BDDMyOngoingStubbing
        public BDDMyOngoingStubbing<T> willThrow(Throwable... throwables) {
            return new BDDOngoingStubbingImpl(this.mockitoOngoingStubbing.thenThrow(throwables));
        }

        @Override // org.mockito.BDDMockito.BDDMyOngoingStubbing
        public BDDMyOngoingStubbing<T> willThrow(Class<? extends Throwable> throwableType) {
            return new BDDOngoingStubbingImpl(this.mockitoOngoingStubbing.thenThrow(throwableType));
        }

        @Override // org.mockito.BDDMockito.BDDMyOngoingStubbing
        public BDDMyOngoingStubbing<T> willThrow(Class<? extends Throwable> throwableType, Class<? extends Throwable>... throwableTypes) {
            return new BDDOngoingStubbingImpl(this.mockitoOngoingStubbing.thenThrow(throwableType, throwableTypes));
        }

        @Override // org.mockito.BDDMockito.BDDMyOngoingStubbing
        public BDDMyOngoingStubbing<T> willCallRealMethod() {
            return new BDDOngoingStubbingImpl(this.mockitoOngoingStubbing.thenCallRealMethod());
        }

        @Override // org.mockito.BDDMockito.BDDMyOngoingStubbing
        public <M> M getMock() {
            return (M) this.mockitoOngoingStubbing.getMock();
        }
    }

    public static <T> BDDMyOngoingStubbing<T> given(T methodCall) {
        return new BDDOngoingStubbingImpl(Mockito.when(methodCall));
    }

    public static <T> Then<T> then(T mock) {
        return new ThenImpl(mock);
    }

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/BDDMockito$ThenImpl.class */
    private static class ThenImpl<T> implements Then<T> {
        private final T mock;

        ThenImpl(T mock) {
            this.mock = mock;
        }

        @Override // org.mockito.BDDMockito.Then
        public T should() {
            return (T) Mockito.verify(this.mock);
        }

        @Override // org.mockito.BDDMockito.Then
        public T should(VerificationMode mode) {
            return (T) Mockito.verify(this.mock, mode);
        }

        @Override // org.mockito.BDDMockito.Then
        public T should(InOrder inOrder) {
            return (T) inOrder.verify(this.mock);
        }

        @Override // org.mockito.BDDMockito.Then
        public T should(InOrder inOrder, VerificationMode mode) {
            return (T) inOrder.verify(this.mock, mode);
        }

        @Override // org.mockito.BDDMockito.Then
        public void shouldHaveZeroInteractions() {
            Mockito.verifyZeroInteractions(this.mock);
        }

        @Override // org.mockito.BDDMockito.Then
        public void shouldHaveNoMoreInteractions() {
            Mockito.verifyNoMoreInteractions(this.mock);
        }

        @Override // org.mockito.BDDMockito.Then
        public void shouldHaveNoInteractions() {
            Mockito.verifyNoInteractions(this.mock);
        }
    }

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/BDDMockito$BDDStubberImpl.class */
    private static class BDDStubberImpl implements BDDStubber {
        private final Stubber mockitoStubber;

        public BDDStubberImpl(Stubber mockitoStubber) {
            this.mockitoStubber = mockitoStubber;
        }

        @Override // org.mockito.BDDMockito.BDDStubber
        public <T> T given(T mock) {
            return (T) this.mockitoStubber.when(mock);
        }

        @Override // org.mockito.BDDMockito.BDDStubber
        public BDDStubber willAnswer(Answer<?> answer) {
            return new BDDStubberImpl(this.mockitoStubber.doAnswer(answer));
        }

        @Override // org.mockito.BDDMockito.BDDStubber
        public BDDStubber will(Answer<?> answer) {
            return new BDDStubberImpl(this.mockitoStubber.doAnswer(answer));
        }

        @Override // org.mockito.BDDMockito.BDDStubber
        @Deprecated
        public BDDStubber willNothing() {
            return willDoNothing();
        }

        @Override // org.mockito.BDDMockito.BDDStubber
        public BDDStubber willDoNothing() {
            return new BDDStubberImpl(this.mockitoStubber.doNothing());
        }

        @Override // org.mockito.BDDMockito.BDDStubber
        public BDDStubber willReturn(Object toBeReturned) {
            return new BDDStubberImpl(this.mockitoStubber.doReturn(toBeReturned));
        }

        @Override // org.mockito.BDDMockito.BDDStubber
        public BDDStubber willReturn(Object toBeReturned, Object... nextToBeReturned) {
            return new BDDStubberImpl(this.mockitoStubber.doReturn(toBeReturned).doReturn(nextToBeReturned));
        }

        @Override // org.mockito.BDDMockito.BDDStubber
        public BDDStubber willThrow(Throwable... toBeThrown) {
            return new BDDStubberImpl(this.mockitoStubber.doThrow(toBeThrown));
        }

        @Override // org.mockito.BDDMockito.BDDStubber
        public BDDStubber willThrow(Class<? extends Throwable> toBeThrown) {
            return new BDDStubberImpl(this.mockitoStubber.doThrow(toBeThrown));
        }

        @Override // org.mockito.BDDMockito.BDDStubber
        public BDDStubber willThrow(Class<? extends Throwable> toBeThrown, Class<? extends Throwable>... nextToBeThrown) {
            return new BDDStubberImpl(this.mockitoStubber.doThrow(toBeThrown, nextToBeThrown));
        }

        @Override // org.mockito.BDDMockito.BDDStubber
        public BDDStubber willCallRealMethod() {
            return new BDDStubberImpl(this.mockitoStubber.doCallRealMethod());
        }
    }

    public static BDDStubber willThrow(Throwable... toBeThrown) {
        return new BDDStubberImpl(Mockito.doThrow(toBeThrown));
    }

    public static BDDStubber willThrow(Class<? extends Throwable> toBeThrown) {
        return new BDDStubberImpl(Mockito.doThrow(toBeThrown));
    }

    public static BDDStubber willThrow(Class<? extends Throwable> toBeThrown, Class<? extends Throwable>... throwableTypes) {
        return new BDDStubberImpl(Mockito.doThrow(toBeThrown, throwableTypes));
    }

    public static BDDStubber willAnswer(Answer<?> answer) {
        return new BDDStubberImpl(Mockito.doAnswer(answer));
    }

    public static BDDStubber will(Answer<?> answer) {
        return new BDDStubberImpl(Mockito.doAnswer(answer));
    }

    public static BDDStubber willDoNothing() {
        return new BDDStubberImpl(Mockito.doNothing());
    }

    public static BDDStubber willReturn(Object toBeReturned) {
        return new BDDStubberImpl(Mockito.doReturn(toBeReturned));
    }

    public static BDDStubber willReturn(Object toBeReturned, Object... toBeReturnedNext) {
        return new BDDStubberImpl(Mockito.doReturn(toBeReturned, toBeReturnedNext));
    }

    public static BDDStubber willCallRealMethod() {
        return new BDDStubberImpl(Mockito.doCallRealMethod());
    }
}
