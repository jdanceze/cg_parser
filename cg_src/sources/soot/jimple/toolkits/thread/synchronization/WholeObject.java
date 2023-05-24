package soot.jimple.toolkits.thread.synchronization;

import soot.SootField;
import soot.SootFieldRef;
import soot.Type;
import soot.jimple.FieldRef;
/* compiled from: CriticalSectionAwareSideEffectAnalysis.java */
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/synchronization/WholeObject.class */
class WholeObject {
    Type type;

    public WholeObject(Type type) {
        this.type = type;
    }

    public WholeObject() {
        this.type = null;
    }

    public String toString() {
        return "All Fields" + (this.type == null ? "" : " (" + this.type + ")");
    }

    public int hashCode() {
        if (this.type == null) {
            return 1;
        }
        return this.type.hashCode();
    }

    public boolean equals(Object o) {
        if (this.type == null) {
            return true;
        }
        if (!(o instanceof WholeObject)) {
            return o instanceof FieldRef ? this.type == ((FieldRef) o).getType() : o instanceof SootFieldRef ? this.type == ((SootFieldRef) o).type() : !(o instanceof SootField) || this.type == ((SootField) o).getType();
        }
        WholeObject other = (WholeObject) o;
        return other.type == null || this.type == other.type;
    }
}
