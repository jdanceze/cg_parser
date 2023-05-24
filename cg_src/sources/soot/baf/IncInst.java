package soot.baf;

import soot.Local;
import soot.jimple.Constant;
/* loaded from: gencallgraphv3.jar:soot/baf/IncInst.class */
public interface IncInst extends Inst {
    Constant getConstant();

    void setConstant(Constant constant);

    void setLocal(Local local);

    Local getLocal();
}
