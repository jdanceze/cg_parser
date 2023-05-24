package heros.fieldsens.structs;

import heros.fieldsens.AccessPath;
import heros.fieldsens.FlowFunction;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/fieldsens/structs/DeltaConstraint.class */
public class DeltaConstraint<FieldRef> implements FlowFunction.Constraint<FieldRef> {
    private AccessPath.Delta<FieldRef> delta;

    public DeltaConstraint(AccessPath<FieldRef> accPathAtCaller, AccessPath<FieldRef> accPathAtCallee) {
        this.delta = accPathAtCaller.getDeltaTo(accPathAtCallee);
    }

    public DeltaConstraint(AccessPath.Delta<FieldRef> delta) {
        this.delta = delta;
    }

    @Override // heros.fieldsens.FlowFunction.Constraint
    public AccessPath<FieldRef> applyToAccessPath(AccessPath<FieldRef> accPath) {
        return this.delta.applyTo(accPath);
    }

    @Override // heros.fieldsens.FlowFunction.Constraint
    public boolean canBeAppliedTo(AccessPath<FieldRef> accPath) {
        return this.delta.canBeAppliedTo(accPath);
    }

    public String toString() {
        return this.delta.toString();
    }

    public int hashCode() {
        int result = (31 * 1) + (this.delta == null ? 0 : this.delta.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        DeltaConstraint other = (DeltaConstraint) obj;
        if (this.delta == null) {
            if (other.delta != null) {
                return false;
            }
            return true;
        } else if (!this.delta.equals(other.delta)) {
            return false;
        } else {
            return true;
        }
    }
}
