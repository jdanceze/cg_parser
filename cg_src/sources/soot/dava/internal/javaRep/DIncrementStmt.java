package soot.dava.internal.javaRep;

import soot.UnitPrinter;
import soot.Value;
import soot.grimp.Grimp;
import soot.grimp.internal.GAssignStmt;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/javaRep/DIncrementStmt.class */
public class DIncrementStmt extends GAssignStmt {
    public DIncrementStmt(Value variable, Value rvalue) {
        super(variable, rvalue);
    }

    @Override // soot.grimp.internal.GAssignStmt, soot.jimple.internal.JAssignStmt, soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new DIncrementStmt(Grimp.cloneIfNecessary(getLeftOp()), Grimp.cloneIfNecessary(getRightOp()));
    }

    @Override // soot.jimple.internal.JAssignStmt
    public String toString() {
        return String.valueOf(getLeftOpBox().getValue().toString()) + "++";
    }

    @Override // soot.jimple.internal.JAssignStmt, soot.Unit
    public void toString(UnitPrinter up) {
        getLeftOpBox().toString(up);
        up.literal("++");
    }
}
