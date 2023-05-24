package soot.jimple;

import soot.Unit;
import soot.UnitBox;
/* loaded from: gencallgraphv3.jar:soot/jimple/GotoStmt.class */
public interface GotoStmt extends Stmt {
    Unit getTarget();

    void setTarget(Unit unit);

    UnitBox getTargetBox();
}
