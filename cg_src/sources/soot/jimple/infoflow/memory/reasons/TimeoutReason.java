package soot.jimple.infoflow.memory.reasons;

import soot.jimple.infoflow.memory.ISolverTerminationReason;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/memory/reasons/TimeoutReason.class */
public class TimeoutReason implements ISolverTerminationReason {
    private final long timeElapsed;
    private final long timeout;

    public TimeoutReason(long timeElapsed, long timeout) {
        this.timeElapsed = timeElapsed;
        this.timeout = timeout;
    }

    public long getTimeElapsed() {
        return this.timeElapsed;
    }

    public long getTimeout() {
        return this.timeout;
    }

    public int hashCode() {
        int result = (31 * 1) + ((int) (this.timeElapsed ^ (this.timeElapsed >>> 32)));
        return (31 * result) + ((int) (this.timeout ^ (this.timeout >>> 32)));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TimeoutReason other = (TimeoutReason) obj;
        if (this.timeElapsed != other.timeElapsed || this.timeout != other.timeout) {
            return false;
        }
        return true;
    }

    @Override // soot.jimple.infoflow.memory.ISolverTerminationReason
    public ISolverTerminationReason combine(ISolverTerminationReason terminationReason) {
        return new MultiReason(this, terminationReason);
    }
}
