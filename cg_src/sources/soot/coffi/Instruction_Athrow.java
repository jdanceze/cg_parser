package soot.coffi;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:soot/coffi/Instruction_Athrow.class */
public class Instruction_Athrow extends Instruction_noargs {
    public Instruction_Athrow() {
        super((byte) -65);
        this.name = "athrow";
        this.branches = true;
    }

    @Override // soot.coffi.Instruction
    public Instruction[] branchpoints(Instruction next) {
        Instruction[] i = {null};
        return i;
    }
}
