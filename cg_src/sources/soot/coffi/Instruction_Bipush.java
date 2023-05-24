package soot.coffi;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:soot/coffi/Instruction_Bipush.class */
public class Instruction_Bipush extends Instruction_byte {
    public Instruction_Bipush() {
        super((byte) 16);
        this.name = "bipush";
    }

    public Instruction_Bipush(byte b) {
        this();
        this.arg_b = b;
    }
}
