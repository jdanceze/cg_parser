package org.mockito.internal.junit;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.mockito.invocation.Invocation;
import org.mockito.plugins.MockitoLogger;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/junit/StubbingArgMismatches.class */
class StubbingArgMismatches {
    final Map<Invocation, Set<Invocation>> mismatches = new LinkedHashMap();

    public void add(Invocation invocation, Invocation stubbing) {
        Set<Invocation> matchingInvocations = this.mismatches.get(stubbing);
        if (matchingInvocations == null) {
            matchingInvocations = new LinkedHashSet<>();
            this.mismatches.put(stubbing, matchingInvocations);
        }
        matchingInvocations.add(invocation);
    }

    public void format(String testName, MockitoLogger logger) {
        if (this.mismatches.isEmpty()) {
            return;
        }
        StubbingHint hint = new StubbingHint(testName);
        int x = 1;
        for (Map.Entry<Invocation, Set<Invocation>> m : this.mismatches.entrySet()) {
            int i = x;
            x++;
            hint.appendLine(Integer.valueOf(i), ". Unused... ", m.getKey().getLocation());
            for (Invocation invocation : m.getValue()) {
                hint.appendLine(" ...args ok? ", invocation.getLocation());
            }
        }
        logger.log(hint.toString());
    }

    public int size() {
        return this.mismatches.size();
    }

    public String toString() {
        return "" + this.mismatches;
    }
}
