package soot.baf;

import soot.IdentityUnit;
import soot.Value;
/* loaded from: gencallgraphv3.jar:soot/baf/IdentityInst.class */
public interface IdentityInst extends Inst, IdentityUnit {
    void setLeftOp(Value value);

    void setRightOp(Value value);
}
