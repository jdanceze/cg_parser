package soot.baf;

import soot.Unit;
import soot.UnitBox;
/* loaded from: gencallgraphv3.jar:soot/baf/TargetArgInst.class */
public interface TargetArgInst extends Inst {
    Unit getTarget();

    UnitBox getTargetBox();

    void setTarget(Unit unit);
}
