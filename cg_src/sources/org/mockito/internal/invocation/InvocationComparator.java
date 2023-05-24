package org.mockito.internal.invocation;

import java.util.Comparator;
import org.mockito.invocation.Invocation;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/invocation/InvocationComparator.class */
public class InvocationComparator implements Comparator<Invocation> {
    @Override // java.util.Comparator
    public int compare(Invocation o1, Invocation o2) {
        return Integer.valueOf(o1.getSequenceNumber()).compareTo(Integer.valueOf(o2.getSequenceNumber()));
    }
}
