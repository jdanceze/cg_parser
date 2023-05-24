package org.mockito.internal.stubbing.defaultanswers;

import java.io.Serializable;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/stubbing/defaultanswers/TriesToReturnSelf.class */
public class TriesToReturnSelf implements Answer<Object>, Serializable {
    private final ReturnsEmptyValues defaultReturn = new ReturnsEmptyValues();

    @Override // org.mockito.stubbing.Answer
    public Object answer(InvocationOnMock invocation) throws Throwable {
        Class<?> methodReturnType = invocation.getMethod().getReturnType();
        Object mock = invocation.getMock();
        Class<?> mockType = MockUtil.getMockHandler(mock).getMockSettings().getTypeToMock();
        if (methodReturnType.isAssignableFrom(mockType)) {
            return invocation.getMock();
        }
        return this.defaultReturn.returnValueFor(methodReturnType);
    }
}
