package soot.JastAddJ;

import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/CONSTANT_InterfaceMethodref_Info.class */
public class CONSTANT_InterfaceMethodref_Info extends CONSTANT_Info {
    public int class_index;
    public int name_and_type_index;

    public CONSTANT_InterfaceMethodref_Info(BytecodeParser parser) {
        super(parser);
        this.class_index = this.p.u2();
        this.name_and_type_index = this.p.u2();
    }

    public String toString() {
        return "InterfaceMethodRefInfo: " + this.p.constantPool[this.class_index] + Instruction.argsep + this.p.constantPool[this.name_and_type_index];
    }
}
