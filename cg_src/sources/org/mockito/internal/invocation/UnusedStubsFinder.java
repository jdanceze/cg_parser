package org.mockito.internal.invocation;

import java.util.LinkedList;
import java.util.List;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.Invocation;
import org.mockito.stubbing.Stubbing;
@Deprecated
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/invocation/UnusedStubsFinder.class */
public class UnusedStubsFinder {
    public List<Invocation> find(List<?> mocks) {
        List<Invocation> unused = new LinkedList<>();
        for (Object mock : mocks) {
            List<Stubbing> fromSingleMock = MockUtil.getInvocationContainer(mock).getStubbingsDescending();
            for (Stubbing s : fromSingleMock) {
                if (!s.wasUsed()) {
                    unused.add(s.getInvocation());
                }
            }
        }
        return unused;
    }
}
