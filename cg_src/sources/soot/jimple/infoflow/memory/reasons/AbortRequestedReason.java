package soot.jimple.infoflow.memory.reasons;

import soot.jimple.infoflow.memory.ISolverTerminationReason;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/memory/reasons/AbortRequestedReason.class */
public class AbortRequestedReason implements ISolverTerminationReason {
    @Override // soot.jimple.infoflow.memory.ISolverTerminationReason
    public ISolverTerminationReason combine(ISolverTerminationReason terminationReason) {
        return new MultiReason(this, terminationReason);
    }
}
