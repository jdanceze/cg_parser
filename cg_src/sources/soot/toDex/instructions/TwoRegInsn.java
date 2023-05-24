package soot.toDex.instructions;

import soot.toDex.Register;
/* loaded from: gencallgraphv3.jar:soot/toDex/instructions/TwoRegInsn.class */
public interface TwoRegInsn extends OneRegInsn {
    public static final int REG_B_IDX = 1;

    Register getRegB();
}
