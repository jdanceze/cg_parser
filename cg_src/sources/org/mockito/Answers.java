package org.mockito;

import org.mockito.internal.stubbing.answers.CallsRealMethods;
import org.mockito.internal.stubbing.defaultanswers.GloballyConfiguredAnswer;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.internal.stubbing.defaultanswers.ReturnsMocks;
import org.mockito.internal.stubbing.defaultanswers.ReturnsSmartNulls;
import org.mockito.internal.stubbing.defaultanswers.TriesToReturnSelf;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/Answers.class */
public enum Answers implements Answer<Object> {
    RETURNS_DEFAULTS(new GloballyConfiguredAnswer()),
    RETURNS_SMART_NULLS(new ReturnsSmartNulls()),
    RETURNS_MOCKS(new ReturnsMocks()),
    RETURNS_DEEP_STUBS(new ReturnsDeepStubs()),
    CALLS_REAL_METHODS(new CallsRealMethods()),
    RETURNS_SELF(new TriesToReturnSelf());
    
    private final Answer<Object> implementation;

    Answers(Answer answer) {
        this.implementation = answer;
    }

    @Deprecated
    public Answer<Object> get() {
        return this;
    }

    @Override // org.mockito.stubbing.Answer
    public Object answer(InvocationOnMock invocation) throws Throwable {
        return this.implementation.answer(invocation);
    }
}
