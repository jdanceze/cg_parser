package org.mockito.internal.stubbing.defaultanswers;

import java.io.Serializable;
import org.mockito.Mockito;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.stubbing.defaultanswers.RetrieveGenericsForDefaultAnswers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/stubbing/defaultanswers/ReturnsMocks.class */
public class ReturnsMocks implements Answer<Object>, Serializable {
    private static final long serialVersionUID = -6755257986994634579L;
    private final Answer<Object> delegate = new ReturnsMoreEmptyValues();

    @Override // org.mockito.stubbing.Answer
    public Object answer(InvocationOnMock invocation) throws Throwable {
        Object defaultReturnValue = this.delegate.answer(invocation);
        if (defaultReturnValue != null) {
            return defaultReturnValue;
        }
        return RetrieveGenericsForDefaultAnswers.returnTypeForMockWithCorrectGenerics(invocation, new RetrieveGenericsForDefaultAnswers.AnswerCallback() { // from class: org.mockito.internal.stubbing.defaultanswers.ReturnsMocks.1
            @Override // org.mockito.internal.stubbing.defaultanswers.RetrieveGenericsForDefaultAnswers.AnswerCallback
            public Object apply(Class<?> type) {
                if (type == null) {
                    return null;
                }
                return Mockito.mock(type, new MockSettingsImpl().defaultAnswer(ReturnsMocks.this));
            }
        });
    }
}
