package soot.coffi;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:soot/coffi/Instruction_Goto_w.class */
public class Instruction_Goto_w extends Instruction_longbranch {
    public Instruction_Goto_w() {
        super((byte) -56);
        this.name = "goto_w";
    }

    @Override // soot.coffi.Instruction_branch, soot.coffi.Instruction
    public Instruction[] branchpoints(Instruction next) {
        Instruction[] i = {this.target};
        return i;
    }
}
