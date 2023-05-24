package soot.dava.internal.javaRep;

import java.util.HashSet;
import soot.SootFieldRef;
import soot.UnitPrinter;
import soot.Value;
import soot.grimp.internal.GInstanceFieldRef;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/javaRep/DInstanceFieldRef.class */
public class DInstanceFieldRef extends GInstanceFieldRef {
    private HashSet<Object> thisLocals;

    public DInstanceFieldRef(Value base, SootFieldRef fieldRef, HashSet<Object> thisLocals) {
        super(base, fieldRef);
        this.thisLocals = thisLocals;
    }

    @Override // soot.jimple.internal.AbstractInstanceFieldRef, soot.Value
    public void toString(UnitPrinter up) {
        if (this.thisLocals.contains(getBase())) {
            up.fieldRef(this.fieldRef);
        } else {
            super.toString(up);
        }
    }

    @Override // soot.grimp.internal.GInstanceFieldRef, soot.jimple.internal.AbstractInstanceFieldRef
    public String toString() {
        if (this.thisLocals.contains(getBase())) {
            return this.fieldRef.name();
        }
        return super.toString();
    }

    @Override // soot.grimp.internal.GInstanceFieldRef, soot.jimple.internal.AbstractInstanceFieldRef, soot.Value
    public Object clone() {
        return new DInstanceFieldRef(getBase(), this.fieldRef, this.thisLocals);
    }
}
