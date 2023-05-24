package org.mockito.internal.stubbing;

import java.util.List;
import org.mockito.internal.exceptions.Reporter;
import org.mockito.invocation.Invocation;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/stubbing/OngoingStubbingImpl.class */
public class OngoingStubbingImpl<T> extends BaseStubbing<T> {
    private final InvocationContainerImpl invocationContainer;
    private Strictness strictness;

    public OngoingStubbingImpl(InvocationContainerImpl invocationContainer) {
        super(invocationContainer.invokedMock());
        this.invocationContainer = invocationContainer;
    }

    @Override // org.mockito.stubbing.OngoingStubbing
    public OngoingStubbing<T> thenAnswer(Answer<?> answer) {
        if (!this.invocationContainer.hasInvocationForPotentialStubbing()) {
            throw Reporter.incorrectUseOfApi();
        }
        this.invocationContainer.addAnswer(answer, this.strictness);
        return new ConsecutiveStubbing(this.invocationContainer);
    }

    public List<Invocation> getRegisteredInvocations() {
        return this.invocationContainer.getInvocations();
    }

    public void setStrictness(Strictness strictness) {
        this.strictness = strictness;
    }
}
