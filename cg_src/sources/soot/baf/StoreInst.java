package soot.baf;

import soot.Local;
import soot.Type;
/* loaded from: gencallgraphv3.jar:soot/baf/StoreInst.class */
public interface StoreInst extends Inst {
    Type getOpType();

    void setOpType(Type type);

    Local getLocal();

    void setLocal(Local local);
}
