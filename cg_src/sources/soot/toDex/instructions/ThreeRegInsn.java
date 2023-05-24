package soot.toDex.instructions;

import soot.toDex.Register;
/* loaded from: gencallgraphv3.jar:soot/toDex/instructions/ThreeRegInsn.class */
public interface ThreeRegInsn extends TwoRegInsn {
    public static final int REG_C_IDX = 2;

    Register getRegC();
}
