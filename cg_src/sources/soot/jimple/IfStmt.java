package soot.jimple;

import soot.Unit;
import soot.UnitBox;
import soot.Value;
import soot.ValueBox;
/* loaded from: gencallgraphv3.jar:soot/jimple/IfStmt.class */
public interface IfStmt extends Stmt {
    Value getCondition();

    void setCondition(Value value);

    ValueBox getConditionBox();

    Stmt getTarget();

    void setTarget(Unit unit);

    UnitBox getTargetBox();
}
