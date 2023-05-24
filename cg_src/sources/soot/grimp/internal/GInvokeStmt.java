package soot.grimp.internal;

import soot.Value;
import soot.grimp.Grimp;
import soot.jimple.internal.JInvokeStmt;
/* loaded from: gencallgraphv3.jar:soot/grimp/internal/GInvokeStmt.class */
public class GInvokeStmt extends JInvokeStmt {
    public GInvokeStmt(Value c) {
        super(Grimp.v().newInvokeExprBox(c));
    }

    @Override // soot.jimple.internal.JInvokeStmt, soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new GInvokeStmt(Grimp.cloneIfNecessary(getInvokeExpr()));
    }
}
