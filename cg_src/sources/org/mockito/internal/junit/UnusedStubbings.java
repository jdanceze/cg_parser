package org.mockito.internal.junit;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.mockito.internal.exceptions.Reporter;
import org.mockito.invocation.Invocation;
import org.mockito.plugins.MockitoLogger;
import org.mockito.stubbing.Stubbing;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/junit/UnusedStubbings.class */
public class UnusedStubbings {
    private final Collection<? extends Stubbing> unused;

    /* JADX INFO: Access modifiers changed from: package-private */
    public UnusedStubbings(Collection<? extends Stubbing> unused) {
        this.unused = unused;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void format(String testName, MockitoLogger logger) {
        if (this.unused.isEmpty()) {
            return;
        }
        StubbingHint hint = new StubbingHint(testName);
        int x = 1;
        for (Stubbing candidate : this.unused) {
            if (!candidate.wasUsed()) {
                int i = x;
                x++;
                hint.appendLine(Integer.valueOf(i), ". Unused ", candidate.getInvocation().getLocation());
            }
        }
        logger.log(hint.toString());
    }

    public int size() {
        return this.unused.size();
    }

    public String toString() {
        return this.unused.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reportUnused() {
        if (this.unused.isEmpty()) {
            return;
        }
        List<Invocation> invocations = new LinkedList<>();
        for (Stubbing stubbing : this.unused) {
            invocations.add(stubbing.getInvocation());
        }
        if (invocations.isEmpty()) {
            return;
        }
        Reporter.unncessaryStubbingException(invocations);
    }
}
