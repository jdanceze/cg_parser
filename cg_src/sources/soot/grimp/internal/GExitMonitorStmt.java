package soot.grimp.internal;

import soot.Value;
import soot.grimp.Grimp;
import soot.jimple.internal.JExitMonitorStmt;
/* loaded from: gencallgraphv3.jar:soot/grimp/internal/GExitMonitorStmt.class */
public class GExitMonitorStmt extends JExitMonitorStmt {
    public GExitMonitorStmt(Value op) {
        super(Grimp.v().newExprBox(op));
    }

    @Override // soot.jimple.internal.JExitMonitorStmt, soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new GExitMonitorStmt(Grimp.cloneIfNecessary(getOp()));
    }
}
