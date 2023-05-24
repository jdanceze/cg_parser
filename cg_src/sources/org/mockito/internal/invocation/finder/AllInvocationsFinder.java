package org.mockito.internal.invocation.finder;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.mockito.internal.invocation.InvocationComparator;
import org.mockito.internal.stubbing.StubbingComparator;
import org.mockito.internal.util.DefaultMockingDetails;
import org.mockito.invocation.Invocation;
import org.mockito.stubbing.Stubbing;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/invocation/finder/AllInvocationsFinder.class */
public class AllInvocationsFinder {
    private AllInvocationsFinder() {
    }

    public static List<Invocation> find(Iterable<?> mocks) {
        Set<Invocation> invocationsInOrder = new TreeSet<>(new InvocationComparator());
        for (Object mock : mocks) {
            Collection<Invocation> fromSingleMock = new DefaultMockingDetails(mock).getInvocations();
            invocationsInOrder.addAll(fromSingleMock);
        }
        return new LinkedList(invocationsInOrder);
    }

    public static Set<Stubbing> findStubbings(Iterable<?> mocks) {
        Set<Stubbing> stubbings = new TreeSet<>(new StubbingComparator());
        for (Object mock : mocks) {
            if (!(mock instanceof Class)) {
                Collection<? extends Stubbing> fromSingleMock = new DefaultMockingDetails(mock).getStubbings();
                stubbings.addAll(fromSingleMock);
            }
        }
        return stubbings;
    }
}
