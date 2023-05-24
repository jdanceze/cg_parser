package soot.baf;

import java.util.List;
import soot.jimple.IntConstant;
/* loaded from: gencallgraphv3.jar:soot/baf/LookupSwitchInst.class */
public interface LookupSwitchInst extends SwitchInst {
    void setLookupValue(int i, int i2);

    int getLookupValue(int i);

    List<IntConstant> getLookupValues();

    void setLookupValues(List<IntConstant> list);
}
