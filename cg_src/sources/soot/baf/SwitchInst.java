package soot.baf;

import java.util.List;
import soot.Unit;
import soot.UnitBox;
/* loaded from: gencallgraphv3.jar:soot/baf/SwitchInst.class */
public interface SwitchInst extends Inst {
    Unit getDefaultTarget();

    void setDefaultTarget(Unit unit);

    UnitBox getDefaultTargetBox();

    int getTargetCount();

    List<Unit> getTargets();

    Unit getTarget(int i);

    void setTarget(int i, Unit unit);

    void setTargets(List<Unit> list);

    UnitBox getTargetBox(int i);
}
