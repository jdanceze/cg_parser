package org.mockito.internal.stubbing;

import java.util.Comparator;
import org.mockito.internal.invocation.InvocationComparator;
import org.mockito.stubbing.Stubbing;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/stubbing/StubbingComparator.class */
public class StubbingComparator implements Comparator<Stubbing> {
    private final InvocationComparator invocationComparator = new InvocationComparator();

    @Override // java.util.Comparator
    public int compare(Stubbing o1, Stubbing o2) {
        return this.invocationComparator.compare(o1.getInvocation(), o2.getInvocation());
    }
}
