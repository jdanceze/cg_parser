package soot.coffi;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:soot/coffi/Instruction_Astore_2.class */
public class Instruction_Astore_2 extends Instruction_noargs implements Interface_Astore {
    public Instruction_Astore_2() {
        super((byte) 77);
        this.name = "astore_2";
    }

    @Override // soot.coffi.Interface_Astore
    public int getLocalNumber() {
        return 2;
    }
}
