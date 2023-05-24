package org.mockito.internal.stubbing.defaultanswers;

import java.io.Serializable;
import org.mockito.Mockito;
import org.mockito.internal.debugging.LocationImpl;
import org.mockito.internal.exceptions.Reporter;
import org.mockito.internal.stubbing.defaultanswers.RetrieveGenericsForDefaultAnswers;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.invocation.Location;
import org.mockito.stubbing.Answer;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/stubbing/defaultanswers/ReturnsSmartNulls.class */
public class ReturnsSmartNulls implements Answer<Object>, Serializable {
    private static final long serialVersionUID = 7618312406617949441L;
    private final Answer<Object> delegate = new ReturnsMoreEmptyValues();

    @Override // org.mockito.stubbing.Answer
    public Object answer(final InvocationOnMock invocation) throws Throwable {
        Object defaultReturnValue = this.delegate.answer(invocation);
        if (defaultReturnValue != null) {
            return defaultReturnValue;
        }
        return RetrieveGenericsForDefaultAnswers.returnTypeForMockWithCorrectGenerics(invocation, new RetrieveGenericsForDefaultAnswers.AnswerCallback() { // from class: org.mockito.internal.stubbing.defaultanswers.ReturnsSmartNulls.1
            @Override // org.mockito.internal.stubbing.defaultanswers.RetrieveGenericsForDefaultAnswers.AnswerCallback
            public Object apply(Class<?> type) {
                if (type == null) {
                    return null;
                }
                return Mockito.mock(type, new ThrowsSmartNullPointer(invocation, new LocationImpl()));
            }
        });
    }

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/stubbing/defaultanswers/ReturnsSmartNulls$ThrowsSmartNullPointer.class */
    private static class ThrowsSmartNullPointer implements Answer {
        private final InvocationOnMock unstubbedInvocation;
        private final Location location;

        ThrowsSmartNullPointer(InvocationOnMock unstubbedInvocation, Location location) {
            this.unstubbedInvocation = unstubbedInvocation;
            this.location = location;
        }

        @Override // org.mockito.stubbing.Answer
        public Object answer(InvocationOnMock currentInvocation) throws Throwable {
            if (ObjectMethodsGuru.isToStringMethod(currentInvocation.getMethod())) {
                return "SmartNull returned by this unstubbed method call on a mock:\n" + this.unstubbedInvocation.toString();
            }
            throw Reporter.smartNullPointerException(this.unstubbedInvocation.toString(), this.location);
        }
    }
}
