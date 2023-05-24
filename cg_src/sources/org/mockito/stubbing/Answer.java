package org.mockito.stubbing;

import org.mockito.invocation.InvocationOnMock;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/stubbing/Answer.class */
public interface Answer<T> {
    T answer(InvocationOnMock invocationOnMock) throws Throwable;
}
