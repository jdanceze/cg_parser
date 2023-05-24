package org.mockito.internal.stubbing.answers;

import java.io.Serializable;
import org.mockito.internal.exceptions.Reporter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.ValidableAnswer;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/stubbing/answers/DoesNothing.class */
public class DoesNothing implements Answer<Object>, ValidableAnswer, Serializable {
    private static final long serialVersionUID = 4840880517740698416L;
    private static final DoesNothing SINGLETON = new DoesNothing();

    private DoesNothing() {
    }

    public static DoesNothing doesNothing() {
        return SINGLETON;
    }

    @Override // org.mockito.stubbing.Answer
    public Object answer(InvocationOnMock invocation) {
        return null;
    }

    @Override // org.mockito.stubbing.ValidableAnswer
    public void validateFor(InvocationOnMock invocation) {
        if (!new InvocationInfo(invocation).isVoid()) {
            throw Reporter.onlyVoidMethodsCanBeSetToDoNothing();
        }
    }
}
