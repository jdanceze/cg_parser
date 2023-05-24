package soot.coffi;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:soot/coffi/Instruction_New.class */
public class Instruction_New extends Instruction_intindex {
    public Instruction_New() {
        super((byte) -69);
        this.name = "new";
        this.calls = true;
    }

    @Override // soot.coffi.Instruction
    public Instruction[] branchpoints(Instruction next) {
        Instruction[] i = {null};
        return i;
    }
}
