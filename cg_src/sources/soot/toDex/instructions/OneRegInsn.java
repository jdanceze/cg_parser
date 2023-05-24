package soot.toDex.instructions;

import soot.toDex.Register;
/* loaded from: gencallgraphv3.jar:soot/toDex/instructions/OneRegInsn.class */
public interface OneRegInsn extends Insn {
    public static final int REG_A_IDX = 0;

    Register getRegA();
}
