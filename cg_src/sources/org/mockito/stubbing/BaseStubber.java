package org.mockito.stubbing;

import org.mockito.NotExtensible;
@NotExtensible
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/stubbing/BaseStubber.class */
public interface BaseStubber {
    Stubber doThrow(Throwable... thArr);

    Stubber doThrow(Class<? extends Throwable> cls);

    Stubber doThrow(Class<? extends Throwable> cls, Class<? extends Throwable>... clsArr);

    Stubber doAnswer(Answer answer);

    Stubber doNothing();

    Stubber doReturn(Object obj);

    Stubber doReturn(Object obj, Object... objArr);

    Stubber doCallRealMethod();
}
