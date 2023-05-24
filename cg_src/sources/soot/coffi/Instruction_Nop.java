package soot.coffi;

import soot.jimple.Jimple;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:soot/coffi/Instruction_Nop.class */
public class Instruction_Nop extends Instruction_noargs {
    public Instruction_Nop() {
        super((byte) 0);
        this.name = Jimple.NOP;
    }
}
