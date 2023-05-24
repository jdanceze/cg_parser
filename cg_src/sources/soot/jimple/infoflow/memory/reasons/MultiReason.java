package soot.jimple.infoflow.memory.reasons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import soot.jimple.infoflow.memory.ISolverTerminationReason;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/memory/reasons/MultiReason.class */
public class MultiReason implements ISolverTerminationReason, Cloneable {
    private List<ISolverTerminationReason> reasons = new ArrayList();

    public MultiReason(ISolverTerminationReason... reasons) {
        if (reasons != null && reasons.length > 0) {
            for (ISolverTerminationReason reason : reasons) {
                this.reasons.add(reason);
            }
        }
    }

    public MultiReason(Collection<ISolverTerminationReason> reasons) {
        this.reasons.addAll(reasons);
    }

    /* renamed from: clone */
    public MultiReason m2755clone() {
        return new MultiReason(this.reasons);
    }

    public List<ISolverTerminationReason> getReasons() {
        return this.reasons;
    }

    public int hashCode() {
        int result = (31 * 1) + (this.reasons == null ? 0 : this.reasons.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        MultiReason other = (MultiReason) obj;
        if (this.reasons == null) {
            if (other.reasons != null) {
                return false;
            }
            return true;
        } else if (!this.reasons.equals(other.reasons)) {
            return false;
        } else {
            return true;
        }
    }

    @Override // soot.jimple.infoflow.memory.ISolverTerminationReason
    public ISolverTerminationReason combine(ISolverTerminationReason terminationReason) {
        MultiReason multiReason = m2755clone();
        multiReason.reasons.add(terminationReason);
        return multiReason;
    }
}
