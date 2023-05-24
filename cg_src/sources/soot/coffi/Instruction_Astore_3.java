package soot.coffi;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:soot/coffi/Instruction_Astore_3.class */
public class Instruction_Astore_3 extends Instruction_noargs implements Interface_Astore {
    public Instruction_Astore_3() {
        super((byte) 78);
        this.name = "astore_3";
    }

    @Override // soot.coffi.Interface_Astore
    public int getLocalNumber() {
        return 3;
    }
}
