package soot.jimple.infoflow.memory.reasons;

import soot.jimple.infoflow.memory.ISolverTerminationReason;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/memory/reasons/OutOfMemoryReason.class */
public class OutOfMemoryReason implements ISolverTerminationReason {
    private final long currentMemory;

    public OutOfMemoryReason(long currentMemory) {
        this.currentMemory = currentMemory;
    }

    public long getCurrentMemory() {
        return this.currentMemory;
    }

    public int hashCode() {
        int result = (31 * 1) + ((int) (this.currentMemory ^ (this.currentMemory >>> 32)));
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        OutOfMemoryReason other = (OutOfMemoryReason) obj;
        if (this.currentMemory != other.currentMemory) {
            return false;
        }
        return true;
    }

    @Override // soot.jimple.infoflow.memory.ISolverTerminationReason
    public ISolverTerminationReason combine(ISolverTerminationReason terminationReason) {
        return new MultiReason(this, terminationReason);
    }
}
