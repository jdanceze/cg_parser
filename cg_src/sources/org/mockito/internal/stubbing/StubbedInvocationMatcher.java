package org.mockito.internal.stubbing;

import java.io.Serializable;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.invocation.MatchableInvocation;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stubbing;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/stubbing/StubbedInvocationMatcher.class */
public class StubbedInvocationMatcher extends InvocationMatcher implements Serializable, Stubbing {
    private static final long serialVersionUID = 4919105134123672727L;
    private final Queue<Answer> answers;
    private final Strictness strictness;
    private DescribedInvocation usedAt;

    public StubbedInvocationMatcher(Answer answer, MatchableInvocation invocation, Strictness strictness) {
        super(invocation.getInvocation(), invocation.getMatchers());
        this.answers = new ConcurrentLinkedQueue();
        this.strictness = strictness;
        this.answers.add(answer);
    }

    @Override // org.mockito.stubbing.Answer
    public Object answer(InvocationOnMock invocation) throws Throwable {
        Answer a;
        synchronized (this.answers) {
            a = this.answers.size() == 1 ? this.answers.peek() : this.answers.poll();
        }
        return a.answer(invocation);
    }

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
    }

    public void markStubUsed(DescribedInvocation usedAt) {
        this.usedAt = usedAt;
    }

    @Override // org.mockito.stubbing.Stubbing
    public boolean wasUsed() {
        return this.usedAt != null;
    }

    @Override // org.mockito.internal.invocation.InvocationMatcher, org.mockito.invocation.DescribedInvocation
    public String toString() {
        return super.toString() + " stubbed with: " + this.answers;
    }

    @Override // org.mockito.stubbing.Stubbing
    public Strictness getStrictness() {
        return this.strictness;
    }
}
