package soot.JastAddJ;

import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/CONSTANT_InvokeDynamic_Info.class */
public class CONSTANT_InvokeDynamic_Info extends CONSTANT_Info {
    public int method_attr_index;
    public int name_and_type_index;

    public CONSTANT_InvokeDynamic_Info(BytecodeParser parser) {
        super(parser);
        this.method_attr_index = this.p.u2();
        this.name_and_type_index = this.p.u2();
    }

    public String toString() {
        return "InvokeDynamicInfo: " + this.method_attr_index + Instruction.argsep + this.name_and_type_index;
    }
}
