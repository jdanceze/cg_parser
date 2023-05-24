package soot.baf;

import soot.Type;
/* loaded from: gencallgraphv3.jar:soot/baf/PrimitiveCastInst.class */
public interface PrimitiveCastInst extends Inst {
    Type getFromType();

    void setFromType(Type type);

    Type getToType();

    void setToType(Type type);
}
