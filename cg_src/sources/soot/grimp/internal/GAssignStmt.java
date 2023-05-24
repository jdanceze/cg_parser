package soot.grimp.internal;

import soot.Value;
import soot.grimp.Grimp;
import soot.jimple.internal.JAssignStmt;
/* loaded from: gencallgraphv3.jar:soot/grimp/internal/GAssignStmt.class */
public class GAssignStmt extends JAssignStmt {
    public GAssignStmt(Value variable, Value rvalue) {
        super(Grimp.v().newVariableBox(variable), Grimp.v().newRValueBox(rvalue));
    }

    @Override // soot.jimple.internal.JAssignStmt, soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new GAssignStmt(Grimp.cloneIfNecessary(getLeftOp()), Grimp.cloneIfNecessary(getRightOp()));
    }
}
