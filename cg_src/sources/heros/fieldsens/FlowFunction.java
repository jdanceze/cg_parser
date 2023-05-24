package heros.fieldsens;

import heros.fieldsens.structs.WrappedFact;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/fieldsens/FlowFunction.class */
public interface FlowFunction<FieldRef, D, Stmt, Method> {

    /* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/fieldsens/FlowFunction$Constraint.class */
    public interface Constraint<FieldRef> {
        AccessPath<FieldRef> applyToAccessPath(AccessPath<FieldRef> accessPath);

        boolean canBeAppliedTo(AccessPath<FieldRef> accessPath);
    }

    Set<ConstrainedFact<FieldRef, D, Stmt, Method>> computeTargets(D d, AccessPathHandler<FieldRef, D, Stmt, Method> accessPathHandler);

    /* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/fieldsens/FlowFunction$ConstrainedFact.class */
    public static class ConstrainedFact<FieldRef, D, Stmt, Method> {
        private WrappedFact<FieldRef, D, Stmt, Method> fact;
        private Constraint<FieldRef> constraint;

        /* JADX INFO: Access modifiers changed from: package-private */
        public ConstrainedFact(WrappedFact<FieldRef, D, Stmt, Method> fact) {
            this.fact = fact;
            this.constraint = null;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public ConstrainedFact(WrappedFact<FieldRef, D, Stmt, Method> fact, Constraint<FieldRef> constraint) {
            this.fact = fact;
            this.constraint = constraint;
        }

        public WrappedFact<FieldRef, D, Stmt, Method> getFact() {
            return this.fact;
        }

        public Constraint<FieldRef> getConstraint() {
            return this.constraint;
        }

        public int hashCode() {
            int result = (31 * 1) + (this.constraint == null ? 0 : this.constraint.hashCode());
            return (31 * result) + (this.fact == null ? 0 : this.fact.hashCode());
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || !(obj instanceof ConstrainedFact)) {
                return false;
            }
            ConstrainedFact other = (ConstrainedFact) obj;
            if (this.constraint == null) {
                if (other.constraint != null) {
                    return false;
                }
            } else if (!this.constraint.equals(other.constraint)) {
                return false;
            }
            if (this.fact == null) {
                if (other.fact != null) {
                    return false;
                }
                return true;
            } else if (!this.fact.equals(other.fact)) {
                return false;
            } else {
                return true;
            }
        }

        public String toString() {
            return this.fact.toString() + "<" + this.constraint + ">";
        }
    }

    /* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/fieldsens/FlowFunction$WriteFieldConstraint.class */
    public static class WriteFieldConstraint<FieldRef> implements Constraint<FieldRef> {
        private FieldRef fieldRef;

        public WriteFieldConstraint(FieldRef fieldRef) {
            this.fieldRef = fieldRef;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // heros.fieldsens.FlowFunction.Constraint
        public AccessPath<FieldRef> applyToAccessPath(AccessPath<FieldRef> accPath) {
            return accPath.appendExcludedFieldReference((FieldRef[]) new Object[]{this.fieldRef});
        }

        public String toString() {
            return "^" + this.fieldRef.toString();
        }

        public int hashCode() {
            int result = (31 * 1) + (this.fieldRef == null ? 0 : this.fieldRef.hashCode());
            return result;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || !(obj instanceof WriteFieldConstraint)) {
                return false;
            }
            WriteFieldConstraint other = (WriteFieldConstraint) obj;
            if (this.fieldRef == null) {
                if (other.fieldRef != null) {
                    return false;
                }
                return true;
            } else if (!this.fieldRef.equals(other.fieldRef)) {
                return false;
            } else {
                return true;
            }
        }

        @Override // heros.fieldsens.FlowFunction.Constraint
        public boolean canBeAppliedTo(AccessPath<FieldRef> accPath) {
            return true;
        }
    }

    /* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/fieldsens/FlowFunction$ReadFieldConstraint.class */
    public static class ReadFieldConstraint<FieldRef> implements Constraint<FieldRef> {
        private FieldRef fieldRef;

        public ReadFieldConstraint(FieldRef fieldRef) {
            this.fieldRef = fieldRef;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // heros.fieldsens.FlowFunction.Constraint
        public AccessPath<FieldRef> applyToAccessPath(AccessPath<FieldRef> accPath) {
            return accPath.append(this.fieldRef);
        }

        public String toString() {
            return this.fieldRef.toString();
        }

        public int hashCode() {
            int result = (31 * 1) + (this.fieldRef == null ? 0 : this.fieldRef.hashCode());
            return result;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || !(obj instanceof ReadFieldConstraint)) {
                return false;
            }
            ReadFieldConstraint other = (ReadFieldConstraint) obj;
            if (this.fieldRef == null) {
                if (other.fieldRef != null) {
                    return false;
                }
                return true;
            } else if (!this.fieldRef.equals(other.fieldRef)) {
                return false;
            } else {
                return true;
            }
        }

        @Override // heros.fieldsens.FlowFunction.Constraint
        public boolean canBeAppliedTo(AccessPath<FieldRef> accPath) {
            return !accPath.isAccessInExclusions(this.fieldRef);
        }
    }
}
