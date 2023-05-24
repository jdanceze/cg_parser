package soot.JastAddJ;

import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/CONSTANT_MethodHandle_Info.class */
public class CONSTANT_MethodHandle_Info extends CONSTANT_Info {
    public int reference_kind;
    public int reference_index;

    public CONSTANT_MethodHandle_Info(BytecodeParser parser) {
        super(parser);
        this.reference_kind = this.p.u1();
        this.reference_index = this.p.u2();
    }

    public String toString() {
        return "MethodHandleInfo: " + this.reference_kind + Instruction.argsep + this.reference_index;
    }
}
