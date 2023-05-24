package soot.grimp.internal;

import soot.Value;
import soot.grimp.Grimp;
import soot.jimple.internal.JThrowStmt;
/* loaded from: gencallgraphv3.jar:soot/grimp/internal/GThrowStmt.class */
public class GThrowStmt extends JThrowStmt {
    public GThrowStmt(Value op) {
        super(Grimp.v().newExprBox(op));
    }

    @Override // soot.jimple.internal.JThrowStmt, soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new GThrowStmt(Grimp.cloneIfNecessary(getOp()));
    }
}
