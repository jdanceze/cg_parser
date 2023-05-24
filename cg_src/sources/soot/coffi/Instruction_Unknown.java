package soot.coffi;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:soot/coffi/Instruction_Unknown.class */
public class Instruction_Unknown extends Instruction_noargs {
    public Instruction_Unknown(byte c) {
        super(c);
        this.name = "unknown instruction (" + (c & 255) + ")";
    }
}
