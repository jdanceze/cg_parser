package org.mockito.internal.stubbing.answers;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.ValidableAnswer;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/stubbing/answers/AnswersWithDelay.class */
public class AnswersWithDelay implements Answer<Object>, ValidableAnswer, Serializable {
    private static final long serialVersionUID = 2177950597971260246L;
    private final long sleepyTime;
    private final Answer<Object> answer;

    public AnswersWithDelay(long sleepyTime, Answer<Object> answer) {
        this.sleepyTime = sleepyTime;
        this.answer = answer;
    }

    @Override // org.mockito.stubbing.Answer
    public Object answer(InvocationOnMock invocation) throws Throwable {
        TimeUnit.MILLISECONDS.sleep(this.sleepyTime);
        return this.answer.answer(invocation);
    }

    @Override // org.mockito.stubbing.ValidableAnswer
    public void validateFor(InvocationOnMock invocation) {
        if (this.answer instanceof ValidableAnswer) {
            ((ValidableAnswer) this.answer).validateFor(invocation);
        }
    }
}
