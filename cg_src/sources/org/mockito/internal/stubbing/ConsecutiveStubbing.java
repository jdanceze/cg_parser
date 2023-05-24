package org.mockito.internal.stubbing;

import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/stubbing/ConsecutiveStubbing.class */
public class ConsecutiveStubbing<T> extends BaseStubbing<T> {
    private final InvocationContainerImpl invocationContainer;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ConsecutiveStubbing(InvocationContainerImpl invocationContainer) {
        super(invocationContainer.invokedMock());
        this.invocationContainer = invocationContainer;
    }

    @Override // org.mockito.stubbing.OngoingStubbing
    public OngoingStubbing<T> thenAnswer(Answer<?> answer) {
        this.invocationContainer.addConsecutiveAnswer(answer);
        return this;
    }
}
