package soot.jimple;

import java.util.List;
import soot.Unit;
import soot.UnitBox;
import soot.Value;
import soot.ValueBox;
/* loaded from: gencallgraphv3.jar:soot/jimple/SwitchStmt.class */
public interface SwitchStmt extends Stmt {
    Unit getDefaultTarget();

    void setDefaultTarget(Unit unit);

    UnitBox getDefaultTargetBox();

    Value getKey();

    void setKey(Value value);

    ValueBox getKeyBox();

    List<Unit> getTargets();

    Unit getTarget(int i);

    void setTarget(int i, Unit unit);

    UnitBox getTargetBox(int i);
}
