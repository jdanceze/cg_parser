package soot.jimple.toolkits.pointer;

import java.util.Collections;
import java.util.Set;
import soot.PointsToSet;
import soot.SootField;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/pointer/StmtRWSet.class */
public class StmtRWSet extends RWSet {
    protected Object field;
    protected PointsToSet base;
    protected boolean callsNative = false;

    public String toString() {
        return "[Field: " + this.field + this.base + "]\n";
    }

    @Override // soot.jimple.toolkits.pointer.RWSet
    public int size() {
        Set globals = getGlobals();
        Set fields = getFields();
        if (globals == null) {
            if (fields == null) {
                return 0;
            }
            return fields.size();
        } else if (fields == null) {
            return globals.size();
        } else {
            return globals.size() + fields.size();
        }
    }

    @Override // soot.jimple.toolkits.pointer.RWSet
    public boolean getCallsNative() {
        return this.callsNative;
    }

    @Override // soot.jimple.toolkits.pointer.RWSet
    public boolean setCallsNative() {
        boolean ret = !this.callsNative;
        this.callsNative = true;
        return ret;
    }

    @Override // soot.jimple.toolkits.pointer.RWSet
    public Set<Object> getGlobals() {
        return this.base == null ? Collections.singleton(this.field) : Collections.emptySet();
    }

    @Override // soot.jimple.toolkits.pointer.RWSet
    public Set<Object> getFields() {
        return this.base != null ? Collections.singleton(this.field) : Collections.emptySet();
    }

    @Override // soot.jimple.toolkits.pointer.RWSet
    public PointsToSet getBaseForField(Object f) {
        if (this.field.equals(f)) {
            return this.base;
        }
        return null;
    }

    @Override // soot.jimple.toolkits.pointer.RWSet
    public boolean hasNonEmptyIntersection(RWSet other) {
        if (this.field == null) {
            return false;
        }
        if (other instanceof StmtRWSet) {
            StmtRWSet o = (StmtRWSet) other;
            if (!this.field.equals(o.field)) {
                return false;
            }
            if (this.base == null) {
                return o.base == null;
            }
            return Union.hasNonEmptyIntersection(this.base, o.base);
        } else if (other instanceof MethodRWSet) {
            if (this.base == null) {
                return other.getGlobals().contains(this.field);
            }
            return Union.hasNonEmptyIntersection(this.base, other.getBaseForField(this.field));
        } else {
            return other.hasNonEmptyIntersection(this);
        }
    }

    @Override // soot.jimple.toolkits.pointer.RWSet
    public boolean union(RWSet other) {
        throw new RuntimeException("Can't do that");
    }

    @Override // soot.jimple.toolkits.pointer.RWSet
    public boolean addGlobal(SootField global) {
        if (this.field != null || this.base != null) {
            throw new RuntimeException("Can't do that");
        }
        this.field = global;
        return true;
    }

    @Override // soot.jimple.toolkits.pointer.RWSet
    public boolean addFieldRef(PointsToSet otherBase, Object field) {
        if (this.field != null || this.base != null) {
            throw new RuntimeException("Can't do that");
        }
        this.field = field;
        this.base = otherBase;
        return true;
    }

    @Override // soot.jimple.toolkits.pointer.RWSet
    public boolean isEquivTo(RWSet other) {
        if (!(other instanceof StmtRWSet)) {
            return false;
        }
        StmtRWSet o = (StmtRWSet) other;
        if (this.callsNative == o.callsNative && this.field.equals(o.field)) {
            return ((this.base instanceof FullObjectSet) && (o.base instanceof FullObjectSet)) || this.base == o.base;
        }
        return false;
    }
}
