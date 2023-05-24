package org.mockito.internal.invocation.finder;

import java.util.List;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.invocation.Invocation;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/invocation/finder/VerifiableInvocationsFinder.class */
public class VerifiableInvocationsFinder {
    private VerifiableInvocationsFinder() {
    }

    public static List<Invocation> find(List<?> mocks) {
        List<Invocation> invocations = AllInvocationsFinder.find(mocks);
        return ListUtil.filter(invocations, new RemoveIgnoredForVerification());
    }

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/invocation/finder/VerifiableInvocationsFinder$RemoveIgnoredForVerification.class */
    private static class RemoveIgnoredForVerification implements ListUtil.Filter<Invocation> {
        private RemoveIgnoredForVerification() {
        }

        @Override // org.mockito.internal.util.collections.ListUtil.Filter
        public boolean isOut(Invocation invocation) {
            return invocation.isIgnoredForVerification();
        }
    }
}
