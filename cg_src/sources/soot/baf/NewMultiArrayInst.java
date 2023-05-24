package soot.baf;

import soot.ArrayType;
/* loaded from: gencallgraphv3.jar:soot/baf/NewMultiArrayInst.class */
public interface NewMultiArrayInst extends Inst {
    ArrayType getBaseType();

    void setBaseType(ArrayType arrayType);

    int getDimensionCount();

    void setDimensionCount(int i);
}
