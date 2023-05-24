package soot.coffi;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:soot/coffi/Instruction_Lreturn.class */
public class Instruction_Lreturn extends Instruction_noargs {
    public Instruction_Lreturn() {
        super((byte) -83);
        this.name = "lreturn";
        this.branches = true;
        this.returns = true;
    }
}
