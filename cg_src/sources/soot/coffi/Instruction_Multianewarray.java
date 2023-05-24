package soot.coffi;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:soot/coffi/Instruction_Multianewarray.class */
public class Instruction_Multianewarray extends Instruction_intindex {
    public byte dims;

    public Instruction_Multianewarray() {
        super((byte) -59);
        this.name = "multianewarray";
    }

    @Override // soot.coffi.Instruction_intindex, soot.coffi.Instruction
    public String toString(cp_info[] constant_pool) {
        return String.valueOf(super.toString(constant_pool)) + Instruction.argsep + ((int) this.dims);
    }

    @Override // soot.coffi.Instruction_intindex, soot.coffi.Instruction
    public int nextOffset(int curr) {
        return super.nextOffset(curr) + 1;
    }

    @Override // soot.coffi.Instruction_intindex, soot.coffi.Instruction
    public int parse(byte[] bc, int index) {
        int index2 = super.parse(bc, index);
        this.dims = bc[index2];
        return index2 + 1;
    }

    @Override // soot.coffi.Instruction_intindex, soot.coffi.Instruction
    public int compile(byte[] bc, int index) {
        int index2 = super.compile(bc, index);
        bc[index2] = this.dims;
        return index2 + 1;
    }
}
