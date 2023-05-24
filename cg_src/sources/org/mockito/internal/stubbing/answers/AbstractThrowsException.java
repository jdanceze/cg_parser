package org.mockito.internal.stubbing.answers;

import org.mockito.internal.exceptions.Reporter;
import org.mockito.internal.exceptions.stacktrace.ConditionalStackTraceFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.ValidableAnswer;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/stubbing/answers/AbstractThrowsException.class */
public abstract class AbstractThrowsException implements Answer<Object>, ValidableAnswer {
    private final ConditionalStackTraceFilter filter = new ConditionalStackTraceFilter();

    protected abstract Throwable getThrowable();

    @Override // org.mockito.stubbing.Answer
    public Object answer(InvocationOnMock invocation) throws Throwable {
        Throwable throwable = getThrowable();
        if (throwable == null) {
            throw new IllegalStateException("throwable is null: you shall not call #answer if #validateFor fails!");
        }
        if (MockUtil.isMock(throwable)) {
            throw throwable;
        }
        Throwable t = throwable.fillInStackTrace();
        if (t == null) {
            throw throwable;
        }
        this.filter.filter(t);
        throw t;
    }

    @Override // org.mockito.stubbing.ValidableAnswer
    public void validateFor(InvocationOnMock invocation) {
        Throwable throwable = getThrowable();
        if (throwable == null) {
            throw Reporter.cannotStubWithNullThrowable();
        }
        if (!(throwable instanceof RuntimeException) && !(throwable instanceof Error) && !new InvocationInfo(invocation).isValidException(throwable)) {
            throw Reporter.checkedExceptionInvalid(throwable);
        }
    }
}
