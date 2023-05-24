package soot.grimp.internal;

import soot.Value;
import soot.grimp.Grimp;
import soot.jimple.internal.JReturnStmt;
/* loaded from: gencallgraphv3.jar:soot/grimp/internal/GReturnStmt.class */
public class GReturnStmt extends JReturnStmt {
    public GReturnStmt(Value returnValue) {
        super(Grimp.v().newExprBox(returnValue));
    }

    @Override // soot.jimple.internal.JReturnStmt, soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new GReturnStmt(Grimp.cloneIfNecessary(getOp()));
    }
}
