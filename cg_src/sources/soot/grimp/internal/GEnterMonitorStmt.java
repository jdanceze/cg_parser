package soot.grimp.internal;

import soot.Value;
import soot.grimp.Grimp;
import soot.jimple.internal.JEnterMonitorStmt;
/* loaded from: gencallgraphv3.jar:soot/grimp/internal/GEnterMonitorStmt.class */
public class GEnterMonitorStmt extends JEnterMonitorStmt {
    public GEnterMonitorStmt(Value op) {
        super(Grimp.v().newExprBox(op));
    }

    @Override // soot.jimple.internal.JEnterMonitorStmt, soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new GEnterMonitorStmt(Grimp.cloneIfNecessary(getOp()));
    }
}
