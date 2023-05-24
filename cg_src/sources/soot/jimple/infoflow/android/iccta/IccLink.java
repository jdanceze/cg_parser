package soot.jimple.infoflow.android.iccta;

import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/iccta/IccLink.class */
public class IccLink {
    protected final SootMethod fromSM;
    protected final Unit fromU;
    protected final SootClass destinationC;
    protected String exit_kind;

    public IccLink(SootMethod fromSm, Unit fromU, SootClass destinationC) {
        this.fromSM = fromSm;
        this.fromU = fromU;
        this.destinationC = destinationC;
    }

    public String toString() {
        return this.fromSM + " [" + this.fromU + "] " + this.destinationC;
    }

    public SootMethod getFromSM() {
        return this.fromSM;
    }

    public Unit getFromU() {
        return this.fromU;
    }

    public String getExit_kind() {
        return this.exit_kind;
    }

    public void setExit_kind(String exit_kind) {
        this.exit_kind = exit_kind;
    }

    public SootClass getDestinationC() {
        return this.destinationC;
    }

    public int hashCode() {
        int result = (31 * 1) + (this.destinationC == null ? 0 : this.destinationC.hashCode());
        return (31 * ((31 * ((31 * result) + (this.exit_kind == null ? 0 : this.exit_kind.hashCode()))) + (this.fromSM == null ? 0 : this.fromSM.hashCode()))) + (this.fromU == null ? 0 : this.fromU.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        IccLink other = (IccLink) obj;
        if (this.destinationC == null) {
            if (other.destinationC != null) {
                return false;
            }
        } else if (!this.destinationC.equals(other.destinationC)) {
            return false;
        }
        if (this.exit_kind == null) {
            if (other.exit_kind != null) {
                return false;
            }
        } else if (!this.exit_kind.equals(other.exit_kind)) {
            return false;
        }
        if (this.fromSM == null) {
            if (other.fromSM != null) {
                return false;
            }
        } else if (!this.fromSM.equals(other.fromSM)) {
            return false;
        }
        if (this.fromU == null) {
            if (other.fromU != null) {
                return false;
            }
            return true;
        } else if (!this.fromU.equals(other.fromU)) {
            return false;
        } else {
            return true;
        }
    }
}
