package soot.coffi;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:soot/coffi/Instruction_Areturn.class */
public class Instruction_Areturn extends Instruction_noargs {
    public Instruction_Areturn() {
        super((byte) -80);
        this.name = "areturn";
        this.branches = true;
        this.returns = true;
    }
}
