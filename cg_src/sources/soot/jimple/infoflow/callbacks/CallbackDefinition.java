package soot.jimple.infoflow.callbacks;

import soot.SootMethod;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/callbacks/CallbackDefinition.class */
public class CallbackDefinition {
    protected final SootMethod targetMethod;
    protected final SootMethod parentMethod;

    public CallbackDefinition(SootMethod targetMethod, SootMethod parentMethod) {
        this.targetMethod = targetMethod;
        this.parentMethod = parentMethod;
    }

    public SootMethod getTargetMethod() {
        return this.targetMethod;
    }

    public SootMethod getParentMethod() {
        return this.parentMethod;
    }

    public int hashCode() {
        int result = (31 * 1) + (this.targetMethod == null ? 0 : this.targetMethod.hashCode());
        return (31 * result) + (this.parentMethod == null ? 0 : this.parentMethod.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        CallbackDefinition other = (CallbackDefinition) obj;
        if (this.targetMethod == null) {
            if (other.targetMethod != null) {
                return false;
            }
        } else if (!this.targetMethod.equals(other.targetMethod)) {
            return false;
        }
        if (this.parentMethod == null) {
            if (other.parentMethod != null) {
                return false;
            }
            return true;
        } else if (!this.parentMethod.equals(other.parentMethod)) {
            return false;
        } else {
            return true;
        }
    }

    public String toString() {
        return this.targetMethod.toString();
    }
}
