package org.mockito.stubbing;

import org.mockito.Incubating;
import org.mockito.invocation.InvocationOnMock;
@Incubating
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/stubbing/ValidableAnswer.class */
public interface ValidableAnswer {
    void validateFor(InvocationOnMock invocationOnMock);
}
