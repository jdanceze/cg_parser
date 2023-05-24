package org.mockito.internal.stubbing.answers;

import java.io.Serializable;
import java.lang.reflect.Modifier;
import org.mockito.Answers;
import org.mockito.internal.exceptions.Reporter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.ValidableAnswer;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/stubbing/answers/CallsRealMethods.class */
public class CallsRealMethods implements Answer<Object>, ValidableAnswer, Serializable {
    private static final long serialVersionUID = 9057165148930624087L;

    @Override // org.mockito.stubbing.Answer
    public Object answer(InvocationOnMock invocation) throws Throwable {
        if (Modifier.isAbstract(invocation.getMethod().getModifiers())) {
            return Answers.RETURNS_DEFAULTS.answer(invocation);
        }
        return invocation.callRealMethod();
    }

    @Override // org.mockito.stubbing.ValidableAnswer
    public void validateFor(InvocationOnMock invocation) {
        if (new InvocationInfo(invocation).isAbstract()) {
            throw Reporter.cannotCallAbstractRealMethod();
        }
    }
}
