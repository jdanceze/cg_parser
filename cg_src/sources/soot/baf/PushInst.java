package soot.baf;

import soot.jimple.Constant;
/* loaded from: gencallgraphv3.jar:soot/baf/PushInst.class */
public interface PushInst extends Inst {
    Constant getConstant();

    void setConstant(Constant constant);
}
