package soot.coffi;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:soot/coffi/Instruction_Astore_1.class */
public class Instruction_Astore_1 extends Instruction_noargs implements Interface_Astore {
    public Instruction_Astore_1() {
        super((byte) 76);
        this.name = "astore_1";
    }

    @Override // soot.coffi.Interface_Astore
    public int getLocalNumber() {
        return 1;
    }
}
