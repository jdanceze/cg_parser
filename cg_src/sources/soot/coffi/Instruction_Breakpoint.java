package soot.coffi;

import soot.jimple.Jimple;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:soot/coffi/Instruction_Breakpoint.class */
public class Instruction_Breakpoint extends Instruction_noargs {
    public Instruction_Breakpoint() {
        super((byte) -54);
        this.name = Jimple.BREAKPOINT;
    }
}
