package soot.jimple.toolkits.pointer;

import java.util.Set;
import soot.PointsToSet;
import soot.SootField;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/pointer/FullRWSet.class */
public class FullRWSet extends RWSet {
    @Override // soot.jimple.toolkits.pointer.RWSet
    public int size() {
        throw new RuntimeException("Unsupported");
    }

    @Override // soot.jimple.toolkits.pointer.RWSet
    public boolean getCallsNative() {
        return true;
    }

    @Override // soot.jimple.toolkits.pointer.RWSet
    public boolean setCallsNative() {
        throw new RuntimeException("Unsupported");
    }

    @Override // soot.jimple.toolkits.pointer.RWSet
    public Set getGlobals() {
        throw new RuntimeException("Unsupported");
    }

    @Override // soot.jimple.toolkits.pointer.RWSet
    public Set getFields() {
        throw new RuntimeException("Unsupported");
    }

    @Override // soot.jimple.toolkits.pointer.RWSet
    public PointsToSet getBaseForField(Object f) {
        throw new RuntimeException("Unsupported");
    }

    @Override // soot.jimple.toolkits.pointer.RWSet
    public boolean hasNonEmptyIntersection(RWSet other) {
        return other != null;
    }

    @Override // soot.jimple.toolkits.pointer.RWSet
    public boolean union(RWSet other) {
        throw new RuntimeException("Unsupported");
    }

    @Override // soot.jimple.toolkits.pointer.RWSet
    public boolean addGlobal(SootField global) {
        throw new RuntimeException("Unsupported");
    }

    @Override // soot.jimple.toolkits.pointer.RWSet
    public boolean addFieldRef(PointsToSet otherBase, Object field) {
        throw new RuntimeException("Unsupported");
    }

    @Override // soot.jimple.toolkits.pointer.RWSet
    public boolean isEquivTo(RWSet other) {
        return other instanceof FullRWSet;
    }
}
