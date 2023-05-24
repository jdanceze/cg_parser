package org.mockito.internal.stubbing.answers;

import java.util.Collection;
import java.util.LinkedList;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/stubbing/answers/ReturnsElementsOf.class */
public class ReturnsElementsOf implements Answer<Object> {
    private final LinkedList<Object> elements;

    public ReturnsElementsOf(Collection<?> elements) {
        if (elements == null) {
            throw new MockitoException("ReturnsElementsOf does not accept null as constructor argument.\nPlease pass a collection instance");
        }
        this.elements = new LinkedList<>(elements);
    }

    @Override // org.mockito.stubbing.Answer
    public Object answer(InvocationOnMock invocation) throws Throwable {
        return this.elements.size() == 1 ? this.elements.get(0) : this.elements.poll();
    }
}
