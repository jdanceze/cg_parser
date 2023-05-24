package soot.coffi;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:soot/coffi/Instruction_Jsr_w.class */
public class Instruction_Jsr_w extends Instruction_longbranch {
    public Instruction_Jsr_w() {
        super((byte) -55);
        this.name = "jsr_w";
    }

    @Override // soot.coffi.Instruction_branch, soot.coffi.Instruction
    public Instruction[] branchpoints(Instruction next) {
        Instruction[] i = {this.target};
        return i;
    }
}
