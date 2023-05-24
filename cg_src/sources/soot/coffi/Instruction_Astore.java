package soot.coffi;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:soot/coffi/Instruction_Astore.class */
public class Instruction_Astore extends Instruction_bytevar implements Interface_Astore {
    public Instruction_Astore() {
        super((byte) 58);
        this.name = "astore";
    }

    @Override // soot.coffi.Interface_Astore
    public int getLocalNumber() {
        return this.arg_b;
    }
}
