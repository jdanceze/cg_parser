package soot.jimple;

import java.util.List;
import soot.Unit;
/* loaded from: gencallgraphv3.jar:soot/jimple/LookupSwitchStmt.class */
public interface LookupSwitchStmt extends SwitchStmt {
    void setLookupValues(List<IntConstant> list);

    void setLookupValue(int i, int i2);

    int getLookupValue(int i);

    List<IntConstant> getLookupValues();

    int getTargetCount();

    void setTargets(Unit[] unitArr);
}
