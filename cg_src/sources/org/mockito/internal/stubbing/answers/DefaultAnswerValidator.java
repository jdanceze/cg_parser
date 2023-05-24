package org.mockito.internal.stubbing.answers;

import org.mockito.internal.exceptions.Reporter;
import org.mockito.invocation.InvocationOnMock;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/stubbing/answers/DefaultAnswerValidator.class */
public abstract class DefaultAnswerValidator {
    public static void validateReturnValueFor(InvocationOnMock invocation, Object returnedValue) throws Throwable {
        InvocationInfo invocationInfo = new InvocationInfo(invocation);
        if (returnedValue != null && !invocationInfo.isValidReturnType(returnedValue.getClass())) {
            throw Reporter.wrongTypeReturnedByDefaultAnswer(invocation.getMock(), invocationInfo.printMethodReturnType(), returnedValue.getClass().getSimpleName(), invocationInfo.getMethodName());
        }
    }
}
