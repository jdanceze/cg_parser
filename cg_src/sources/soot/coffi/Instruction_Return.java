package soot.coffi;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:soot/coffi/Instruction_Return.class */
public class Instruction_Return extends Instruction_noargs {
    public Instruction_Return() {
        super((byte) -79);
        this.name = "return";
        this.branches = true;
        this.returns = true;
    }
}
