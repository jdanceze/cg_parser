package org.mockito.internal.stubbing.answers;

import java.io.Serializable;
import org.mockito.internal.exceptions.Reporter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.ValidableAnswer;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/stubbing/answers/Returns.class */
public class Returns implements Answer<Object>, ValidableAnswer, Serializable {
    private static final long serialVersionUID = -6245608253574215396L;
    private final Object value;

    public Returns(Object value) {
        this.value = value;
    }

    @Override // org.mockito.stubbing.Answer
    public Object answer(InvocationOnMock invocation) throws Throwable {
        return this.value;
    }

    @Override // org.mockito.stubbing.ValidableAnswer
    public void validateFor(InvocationOnMock invocation) {
        InvocationInfo invocationInfo = new InvocationInfo(invocation);
        if (invocationInfo.isVoid()) {
            throw Reporter.cannotStubVoidMethodWithAReturnValue(invocationInfo.getMethodName());
        }
        if (returnsNull() && invocationInfo.returnsPrimitive()) {
            throw Reporter.wrongTypeOfReturnValue(invocationInfo.printMethodReturnType(), Jimple.NULL, invocationInfo.getMethodName());
        }
        if (!returnsNull() && !invocationInfo.isValidReturnType(returnType())) {
            throw Reporter.wrongTypeOfReturnValue(invocationInfo.printMethodReturnType(), printReturnType(), invocationInfo.getMethodName());
        }
    }

    private String printReturnType() {
        return this.value.getClass().getSimpleName();
    }

    private Class<?> returnType() {
        return this.value.getClass();
    }

    private boolean returnsNull() {
        return this.value == null;
    }

    public String toString() {
        return "Returns: " + this.value;
    }
}
