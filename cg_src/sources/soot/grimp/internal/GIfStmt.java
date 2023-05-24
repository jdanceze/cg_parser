package soot.grimp.internal;

import soot.Unit;
import soot.Value;
import soot.grimp.Grimp;
import soot.jimple.internal.JIfStmt;
/* loaded from: gencallgraphv3.jar:soot/grimp/internal/GIfStmt.class */
public class GIfStmt extends JIfStmt {
    public GIfStmt(Value condition, Unit target) {
        super(Grimp.v().newConditionExprBox(condition), Grimp.v().newStmtBox(target));
    }

    @Override // soot.jimple.internal.JIfStmt, soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new GIfStmt(Grimp.cloneIfNecessary(getCondition()), getTarget());
    }
}
