package soot.coffi;

import soot.jimple.Jimple;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:soot/coffi/Instruction_Ret.class */
public class Instruction_Ret extends Instruction_bytevar {
    public Instruction_Ret() {
        super((byte) -87);
        this.name = Jimple.RET;
        this.branches = true;
    }
}
