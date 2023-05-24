package org.mockito.internal.stubbing;

import java.util.LinkedList;
import java.util.List;
import org.mockito.internal.exceptions.Reporter;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.answers.CallsRealMethods;
import org.mockito.internal.stubbing.answers.DoesNothing;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.internal.stubbing.answers.ThrowsException;
import org.mockito.internal.stubbing.answers.ThrowsExceptionForClassType;
import org.mockito.internal.util.MockUtil;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stubber;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/stubbing/StubberImpl.class */
public class StubberImpl implements Stubber {
    private final Strictness strictness;
    private final List<Answer<?>> answers = new LinkedList();

    public StubberImpl(Strictness strictness) {
        this.strictness = strictness;
    }

    @Override // org.mockito.stubbing.Stubber
    public <T> T when(T mock) {
        if (mock == null) {
            ThreadSafeMockingProgress.mockingProgress().reset();
            throw Reporter.nullPassedToWhenMethod();
        } else if (!MockUtil.isMock(mock)) {
            ThreadSafeMockingProgress.mockingProgress().reset();
            throw Reporter.notAMockPassedToWhenMethod();
        } else {
            MockUtil.getInvocationContainer(mock).setAnswersForStubbing(this.answers, this.strictness);
            return mock;
        }
    }

    @Override // org.mockito.stubbing.BaseStubber
    public Stubber doReturn(Object toBeReturned) {
        return doReturnValues(toBeReturned);
    }

    @Override // org.mockito.stubbing.BaseStubber
    public Stubber doReturn(Object toBeReturned, Object... nextToBeReturned) {
        return doReturnValues(toBeReturned).doReturnValues(nextToBeReturned);
    }

    private StubberImpl doReturnValues(Object... toBeReturned) {
        if (toBeReturned == null) {
            this.answers.add(new Returns(null));
            return this;
        }
        for (Object r : toBeReturned) {
            this.answers.add(new Returns(r));
        }
        return this;
    }

    @Override // org.mockito.stubbing.BaseStubber
    public Stubber doThrow(Throwable... toBeThrown) {
        if (toBeThrown == null) {
            this.answers.add(new ThrowsException(null));
            return this;
        }
        for (Throwable throwable : toBeThrown) {
            this.answers.add(new ThrowsException(throwable));
        }
        return this;
    }

    @Override // org.mockito.stubbing.BaseStubber
    public Stubber doThrow(Class<? extends Throwable> toBeThrown) {
        if (toBeThrown == null) {
            ThreadSafeMockingProgress.mockingProgress().reset();
            throw Reporter.notAnException();
        }
        return doAnswer(new ThrowsExceptionForClassType(toBeThrown));
    }

    @Override // org.mockito.stubbing.BaseStubber
    public Stubber doThrow(Class<? extends Throwable> toBeThrown, Class<? extends Throwable>... nextToBeThrown) {
        Stubber stubber = doThrow(toBeThrown);
        if (nextToBeThrown == null) {
            ThreadSafeMockingProgress.mockingProgress().reset();
            throw Reporter.notAnException();
        }
        for (Class<? extends Throwable> next : nextToBeThrown) {
            stubber = stubber.doThrow(next);
        }
        return stubber;
    }

    @Override // org.mockito.stubbing.BaseStubber
    public Stubber doNothing() {
        this.answers.add(DoesNothing.doesNothing());
        return this;
    }

    @Override // org.mockito.stubbing.BaseStubber
    public Stubber doAnswer(Answer answer) {
        this.answers.add(answer);
        return this;
    }

    @Override // org.mockito.stubbing.BaseStubber
    public Stubber doCallRealMethod() {
        this.answers.add(new CallsRealMethods());
        return this;
    }
}
