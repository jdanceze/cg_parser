package org.powermock.api.mockito.internal.stubbing;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
/* loaded from: gencallgraphv3.jar:powermock-api-mockito2-2.0.9.jar:org/powermock/api/mockito/internal/stubbing/PowerMockCallRealMethod.class */
public class PowerMockCallRealMethod implements Answer {
    @Override // org.mockito.stubbing.Answer
    public Object answer(InvocationOnMock invocation) throws Throwable {
        MockitoRealMethodInvocation.mockitoInvocationStarted();
        try {
            Object answer = Mockito.CALLS_REAL_METHODS.answer(invocation);
            MockitoRealMethodInvocation.mockitoInvocationFinished();
            return answer;
        } catch (Throwable th) {
            MockitoRealMethodInvocation.mockitoInvocationFinished();
            throw th;
        }
    }
}
