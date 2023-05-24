package soot.toDex.instructions;

import soot.toDex.Register;
/* loaded from: gencallgraphv3.jar:soot/toDex/instructions/FiveRegInsn.class */
public interface FiveRegInsn extends Insn {
    public static final int REG_D_IDX = 0;
    public static final int REG_E_IDX = 1;
    public static final int REG_F_IDX = 2;
    public static final int REG_G_IDX = 3;
    public static final int REG_A_IDX = 4;

    Register getRegD();

    Register getRegE();

    Register getRegF();

    Register getRegG();

    Register getRegA();
}
