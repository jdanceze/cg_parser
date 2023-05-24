package soot.coffi;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:soot/coffi/Instruction_Invokeinterface.class */
public class Instruction_Invokeinterface extends Instruction_intindex {
    public byte nargs;
    public byte reserved;

    public Instruction_Invokeinterface() {
        super((byte) -71);
        this.name = "invokeinterface";
        this.calls = true;
    }

    @Override // soot.coffi.Instruction_intindex, soot.coffi.Instruction
    public String toString(cp_info[] constant_pool) {
        return String.valueOf(super.toString(constant_pool)) + Instruction.argsep + ((int) this.nargs) + Instruction.argsep + "(reserved " + ((int) this.reserved) + ")";
    }

    @Override // soot.coffi.Instruction_intindex, soot.coffi.Instruction
    public int nextOffset(int curr) {
        return super.nextOffset(curr) + 2;
    }

    @Override // soot.coffi.Instruction_intindex, soot.coffi.Instruction
    public int parse(byte[] bc, int index) {
        int index2 = super.parse(bc, index);
        this.nargs = bc[index2];
        int index3 = index2 + 1;
        this.reserved = bc[index3];
        return index3 + 1;
    }

    @Override // soot.coffi.Instruction_intindex, soot.coffi.Instruction
    public int compile(byte[] bc, int index) {
        int index2 = super.compile(bc, index);
        int index3 = index2 + 1;
        bc[index2] = this.nargs;
        int index4 = index3 + 1;
        bc[index3] = this.reserved;
        return index4;
    }
}
