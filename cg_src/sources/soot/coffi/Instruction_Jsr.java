package soot.coffi;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:soot/coffi/Instruction_Jsr.class */
public class Instruction_Jsr extends Instruction_intbranch {
    public Instruction_Jsr() {
        super((byte) -88);
        this.name = "jsr";
    }

    @Override // soot.coffi.Instruction_branch, soot.coffi.Instruction
    public Instruction[] branchpoints(Instruction next) {
        Instruction[] i = {this.target};
        return i;
    }
}
