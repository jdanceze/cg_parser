package soot.jimple.toolkits.pointer;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import soot.G;
import soot.PointsToSet;
import soot.SootField;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/pointer/SiteRWSet.class */
public class SiteRWSet extends RWSet {
    public HashSet<RWSet> sets = new HashSet<>();
    protected boolean callsNative = false;

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

    public String toString() {
        StringBuilder ret = new StringBuilder("SiteRWSet: ");
        boolean empty = true;
        Iterator<RWSet> it = this.sets.iterator();
        while (it.hasNext()) {
            RWSet key = it.next();
            ret.append(key.toString());
            empty = false;
        }
        if (empty) {
            ret.append("empty");
        }
        return ret.toString();
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
        HashSet<Object> ret = new HashSet<>();
        Iterator<RWSet> it = this.sets.iterator();
        while (it.hasNext()) {
            RWSet s = it.next();
            ret.addAll(s.getGlobals());
        }
        return ret;
    }

    @Override // soot.jimple.toolkits.pointer.RWSet
    public Set<Object> getFields() {
        HashSet<Object> ret = new HashSet<>();
        Iterator<RWSet> it = this.sets.iterator();
        while (it.hasNext()) {
            RWSet s = it.next();
            ret.addAll(s.getFields());
        }
        return ret;
    }

    @Override // soot.jimple.toolkits.pointer.RWSet
    public PointsToSet getBaseForField(Object f) {
        Union ret = null;
        Iterator<RWSet> it = this.sets.iterator();
        while (it.hasNext()) {
            RWSet s = it.next();
            PointsToSet os = s.getBaseForField(f);
            if (os != null && !os.isEmpty()) {
                if (ret == null) {
                    ret = G.v().Union_factory.newUnion();
                }
                ret.addAll(os);
            }
        }
        return ret;
    }

    @Override // soot.jimple.toolkits.pointer.RWSet
    public boolean hasNonEmptyIntersection(RWSet oth) {
        if (this.sets.contains(oth)) {
            return true;
        }
        Iterator<RWSet> it = this.sets.iterator();
        while (it.hasNext()) {
            RWSet s = it.next();
            if (oth.hasNonEmptyIntersection(s)) {
                return true;
            }
        }
        return false;
    }

    @Override // soot.jimple.toolkits.pointer.RWSet
    public boolean union(RWSet other) {
        if (other == null) {
            return false;
        }
        boolean ret = false;
        if (other.getCallsNative()) {
            ret = setCallsNative();
        }
        if (other.getFields().isEmpty() && other.getGlobals().isEmpty()) {
            return ret;
        }
        return this.sets.add(other) | ret;
    }

    @Override // soot.jimple.toolkits.pointer.RWSet
    public boolean addGlobal(SootField global) {
        throw new RuntimeException("Not implemented; try MethodRWSet");
    }

    @Override // soot.jimple.toolkits.pointer.RWSet
    public boolean addFieldRef(PointsToSet otherBase, Object field) {
        throw new RuntimeException("Not implemented; try MethodRWSet");
    }

    @Override // soot.jimple.toolkits.pointer.RWSet
    public boolean isEquivTo(RWSet other) {
        if (!(other instanceof SiteRWSet)) {
            return false;
        }
        SiteRWSet o = (SiteRWSet) other;
        if (o.callsNative != this.callsNative) {
            return false;
        }
        return o.sets.equals(this.sets);
    }
}
