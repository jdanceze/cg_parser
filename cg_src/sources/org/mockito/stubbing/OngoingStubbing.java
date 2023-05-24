package org.mockito.stubbing;

import org.mockito.NotExtensible;
@NotExtensible
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/stubbing/OngoingStubbing.class */
public interface OngoingStubbing<T> {
    OngoingStubbing<T> thenReturn(T t);

    OngoingStubbing<T> thenReturn(T t, T... tArr);

    OngoingStubbing<T> thenThrow(Throwable... thArr);

    OngoingStubbing<T> thenThrow(Class<? extends Throwable> cls);

    OngoingStubbing<T> thenThrow(Class<? extends Throwable> cls, Class<? extends Throwable>... clsArr);

    OngoingStubbing<T> thenCallRealMethod();

    OngoingStubbing<T> thenAnswer(Answer<?> answer);

    OngoingStubbing<T> then(Answer<?> answer);

    <M> M getMock();
}
