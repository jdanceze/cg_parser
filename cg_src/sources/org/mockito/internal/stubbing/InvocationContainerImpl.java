package org.mockito.internal.stubbing;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.mockito.internal.invocation.StubInfoImpl;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.verification.DefaultRegisteredInvocations;
import org.mockito.internal.verification.RegisteredInvocations;
import org.mockito.internal.verification.SingleRegisteredInvocation;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationContainer;
import org.mockito.invocation.MatchableInvocation;
import org.mockito.mock.MockCreationSettings;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stubbing;
import org.mockito.stubbing.ValidableAnswer;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/stubbing/InvocationContainerImpl.class */
public class InvocationContainerImpl implements InvocationContainer, Serializable {
    private static final long serialVersionUID = -5334301962749537177L;
    private final LinkedList<StubbedInvocationMatcher> stubbed = new LinkedList<>();
    private final DoAnswerStyleStubbing doAnswerStyleStubbing;
    private final RegisteredInvocations registeredInvocations;
    private final Strictness mockStrictness;
    private MatchableInvocation invocationForStubbing;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !InvocationContainerImpl.class.desiredAssertionStatus();
    }

    public InvocationContainerImpl(MockCreationSettings mockSettings) {
        this.registeredInvocations = createRegisteredInvocations(mockSettings);
        this.mockStrictness = mockSettings.isLenient() ? Strictness.LENIENT : null;
        this.doAnswerStyleStubbing = new DoAnswerStyleStubbing();
    }

    public void setInvocationForPotentialStubbing(MatchableInvocation invocation) {
        this.registeredInvocations.add(invocation.getInvocation());
        this.invocationForStubbing = invocation;
    }

    public void resetInvocationForPotentialStubbing(MatchableInvocation invocationMatcher) {
        this.invocationForStubbing = invocationMatcher;
    }

    public void addAnswer(Answer answer, Strictness stubbingStrictness) {
        this.registeredInvocations.removeLast();
        addAnswer(answer, false, stubbingStrictness);
    }

    public void addConsecutiveAnswer(Answer answer) {
        addAnswer(answer, true, null);
    }

    public StubbedInvocationMatcher addAnswer(Answer answer, boolean isConsecutive, Strictness stubbingStrictness) {
        StubbedInvocationMatcher first;
        Invocation invocation = this.invocationForStubbing.getInvocation();
        ThreadSafeMockingProgress.mockingProgress().stubbingCompleted();
        if (answer instanceof ValidableAnswer) {
            ((ValidableAnswer) answer).validateFor(invocation);
        }
        synchronized (this.stubbed) {
            if (isConsecutive) {
                this.stubbed.getFirst().addAnswer(answer);
            } else {
                Strictness effectiveStrictness = stubbingStrictness != null ? stubbingStrictness : this.mockStrictness;
                this.stubbed.addFirst(new StubbedInvocationMatcher(answer, this.invocationForStubbing, effectiveStrictness));
            }
            first = this.stubbed.getFirst();
        }
        return first;
    }

    Object answerTo(Invocation invocation) throws Throwable {
        return findAnswerFor(invocation).answer(invocation);
    }

    public StubbedInvocationMatcher findAnswerFor(Invocation invocation) {
        synchronized (this.stubbed) {
            Iterator<StubbedInvocationMatcher> it = this.stubbed.iterator();
            while (it.hasNext()) {
                StubbedInvocationMatcher s = it.next();
                if (s.matches(invocation)) {
                    s.markStubUsed(invocation);
                    invocation.markStubbed(new StubInfoImpl(s));
                    return s;
                }
            }
            return null;
        }
    }

    public void setAnswersForStubbing(List<Answer<?>> answers, Strictness strictness) {
        this.doAnswerStyleStubbing.setAnswers(answers, strictness);
    }

    public boolean hasAnswersForStubbing() {
        return !this.doAnswerStyleStubbing.isSet();
    }

    public boolean hasInvocationForPotentialStubbing() {
        return !this.registeredInvocations.isEmpty();
    }

    public void setMethodForStubbing(MatchableInvocation invocation) {
        this.invocationForStubbing = invocation;
        if (!$assertionsDisabled && !hasAnswersForStubbing()) {
            throw new AssertionError();
        }
        int i = 0;
        while (i < this.doAnswerStyleStubbing.getAnswers().size()) {
            addAnswer(this.doAnswerStyleStubbing.getAnswers().get(i), i != 0, this.doAnswerStyleStubbing.getStubbingStrictness());
            i++;
        }
        this.doAnswerStyleStubbing.clear();
    }

    public String toString() {
        return "invocationForStubbing: " + this.invocationForStubbing;
    }

    public List<Invocation> getInvocations() {
        return this.registeredInvocations.getAll();
    }

    public void clearInvocations() {
        this.registeredInvocations.clear();
    }

    public List<Stubbing> getStubbingsDescending() {
        return this.stubbed;
    }

    public Collection<Stubbing> getStubbingsAscending() {
        List<Stubbing> result = new LinkedList<>(this.stubbed);
        Collections.reverse(result);
        return result;
    }

    public Object invokedMock() {
        return this.invocationForStubbing.getInvocation().getMock();
    }

    public MatchableInvocation getInvocationForStubbing() {
        return this.invocationForStubbing;
    }

    private RegisteredInvocations createRegisteredInvocations(MockCreationSettings mockSettings) {
        if (mockSettings.isStubOnly()) {
            return new SingleRegisteredInvocation();
        }
        return new DefaultRegisteredInvocations();
    }
}
