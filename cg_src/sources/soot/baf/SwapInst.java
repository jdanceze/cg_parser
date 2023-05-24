package soot.baf;

import soot.Type;
/* loaded from: gencallgraphv3.jar:soot/baf/SwapInst.class */
public interface SwapInst extends Inst {
    Type getFromType();

    void setFromType(Type type);

    Type getToType();

    void setToType(Type type);
}
