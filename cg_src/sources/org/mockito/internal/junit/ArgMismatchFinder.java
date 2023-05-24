package org.mockito.internal.junit;

import org.mockito.internal.invocation.finder.AllInvocationsFinder;
import org.mockito.invocation.Invocation;
import org.mockito.stubbing.Stubbing;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/junit/ArgMismatchFinder.class */
class ArgMismatchFinder {
    /* JADX INFO: Access modifiers changed from: package-private */
    public StubbingArgMismatches getStubbingArgMismatches(Iterable<?> mocks) {
        StubbingArgMismatches mismatches = new StubbingArgMismatches();
        for (Invocation i : AllInvocationsFinder.find(mocks)) {
            if (i.stubInfo() == null) {
                for (Stubbing stubbing : AllInvocationsFinder.findStubbings(mocks)) {
                    if (!stubbing.wasUsed() && stubbing.getInvocation().getMock() == i.getMock() && stubbing.getInvocation().getMethod().getName().equals(i.getMethod().getName())) {
                        mismatches.add(i, stubbing.getInvocation());
                    }
                }
            }
        }
        return mismatches;
    }
}
