package soot.JastAddJ;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/CONSTANT_MethodType_Info.class */
public class CONSTANT_MethodType_Info extends CONSTANT_Info {
    public int descriptor_index;

    public CONSTANT_MethodType_Info(BytecodeParser parser) {
        super(parser);
        this.descriptor_index = this.p.u2();
    }

    public String toString() {
        return "MethodTypeInfo: " + this.descriptor_index;
    }
}
