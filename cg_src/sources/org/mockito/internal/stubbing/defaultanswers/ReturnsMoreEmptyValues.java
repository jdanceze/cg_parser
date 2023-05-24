package org.mockito.internal.stubbing.defaultanswers;

import java.io.Serializable;
import java.lang.reflect.Array;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/stubbing/defaultanswers/ReturnsMoreEmptyValues.class */
public class ReturnsMoreEmptyValues implements Answer<Object>, Serializable {
    private static final long serialVersionUID = -2816745041482698471L;
    private final Answer<Object> delegate = new ReturnsEmptyValues();

    @Override // org.mockito.stubbing.Answer
    public Object answer(InvocationOnMock invocation) throws Throwable {
        Object ret = this.delegate.answer(invocation);
        if (ret != null) {
            return ret;
        }
        Class<?> returnType = invocation.getMethod().getReturnType();
        return returnValueFor(returnType);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Object returnValueFor(Class<?> type) {
        if (type == String.class) {
            return "";
        }
        if (type.isArray()) {
            Class<?> componentType = type.getComponentType();
            return Array.newInstance(componentType, 0);
        }
        return null;
    }
}
