package soot.coffi;

import soot.jimple.Jimple;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:soot/coffi/Instruction_Goto.class */
public class Instruction_Goto extends Instruction_intbranch {
    public Instruction_Goto() {
        super((byte) -89);
        this.name = Jimple.GOTO;
    }

    @Override // soot.coffi.Instruction_branch, soot.coffi.Instruction
    public Instruction[] branchpoints(Instruction next) {
        Instruction[] i = {this.target};
        return i;
    }
}
