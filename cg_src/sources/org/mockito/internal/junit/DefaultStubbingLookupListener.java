package org.mockito.internal.junit;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.mockito.internal.exceptions.Reporter;
import org.mockito.internal.stubbing.StrictnessSelector;
import org.mockito.internal.stubbing.UnusedStubbingReporting;
import org.mockito.invocation.Invocation;
import org.mockito.listeners.StubbingLookupEvent;
import org.mockito.listeners.StubbingLookupListener;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Stubbing;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/junit/DefaultStubbingLookupListener.class */
class DefaultStubbingLookupListener implements StubbingLookupListener, Serializable {
    private static final long serialVersionUID = -6789800638070123629L;
    private Strictness currentStrictness;
    private boolean mismatchesReported;

    /* JADX INFO: Access modifiers changed from: package-private */
    public DefaultStubbingLookupListener(Strictness strictness) {
        this.currentStrictness = strictness;
    }

    @Override // org.mockito.listeners.StubbingLookupListener
    public void onStubbingLookup(StubbingLookupEvent event) {
        Strictness actualStrictness = StrictnessSelector.determineStrictness(event.getStubbingFound(), event.getMockSettings(), this.currentStrictness);
        if (actualStrictness != Strictness.STRICT_STUBS) {
            return;
        }
        if (event.getStubbingFound() == null) {
            List<Invocation> argMismatchStubbings = potentialArgMismatches(event.getInvocation(), event.getAllStubbings());
            if (!argMismatchStubbings.isEmpty()) {
                this.mismatchesReported = true;
                Reporter.potentialStubbingProblem(event.getInvocation(), argMismatchStubbings);
                return;
            }
            return;
        }
        event.getInvocation().markVerified();
    }

    private static List<Invocation> potentialArgMismatches(Invocation invocation, Collection<Stubbing> stubbings) {
        List<Invocation> matchingStubbings = new LinkedList<>();
        for (Stubbing s : stubbings) {
            if (UnusedStubbingReporting.shouldBeReported(s) && s.getInvocation().getMethod().getName().equals(invocation.getMethod().getName()) && !s.getInvocation().getLocation().getSourceFile().equals(invocation.getLocation().getSourceFile())) {
                matchingStubbings.add(s.getInvocation());
            }
        }
        return matchingStubbings;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setCurrentStrictness(Strictness currentStrictness) {
        this.currentStrictness = currentStrictness;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isMismatchesReported() {
        return this.mismatchesReported;
    }
}
