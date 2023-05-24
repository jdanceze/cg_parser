package org.mockito.internal.junit;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.mockito.internal.invocation.finder.AllInvocationsFinder;
import org.mockito.internal.stubbing.UnusedStubbingReporting;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.invocation.Invocation;
import org.mockito.stubbing.Stubbing;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/junit/UnusedStubbingsFinder.class */
public class UnusedStubbingsFinder {
    public UnusedStubbings getUnusedStubbings(Iterable<Object> mocks) {
        Set<Stubbing> stubbings = AllInvocationsFinder.findStubbings(mocks);
        List<Stubbing> unused = ListUtil.filter(stubbings, new ListUtil.Filter<Stubbing>() { // from class: org.mockito.internal.junit.UnusedStubbingsFinder.1
            @Override // org.mockito.internal.util.collections.ListUtil.Filter
            public boolean isOut(Stubbing s) {
                return !UnusedStubbingReporting.shouldBeReported(s);
            }
        });
        return new UnusedStubbings(unused);
    }

    public Collection<Invocation> getUnusedStubbingsByLocation(Iterable<Object> mocks) {
        Set<Stubbing> stubbings = AllInvocationsFinder.findStubbings(mocks);
        Set<String> locationsOfUsedStubbings = new HashSet<>();
        for (Stubbing s : stubbings) {
            if (!UnusedStubbingReporting.shouldBeReported(s)) {
                locationsOfUsedStubbings.add(s.getInvocation().getLocation().toString());
            }
        }
        Map<String, Invocation> out = new LinkedHashMap<>();
        for (Stubbing s2 : stubbings) {
            String location = s2.getInvocation().getLocation().toString();
            if (!locationsOfUsedStubbings.contains(location)) {
                out.put(location, s2.getInvocation());
            }
        }
        return out.values();
    }
}
