package soot.baf;

import soot.Type;
/* loaded from: gencallgraphv3.jar:soot/baf/NewArrayInst.class */
public interface NewArrayInst extends Inst {
    Type getBaseType();

    void setBaseType(Type type);
}
