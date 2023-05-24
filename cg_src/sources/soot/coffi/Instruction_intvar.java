package soot.coffi;
/* loaded from: gencallgraphv3.jar:soot/coffi/Instruction_intvar.class */
class Instruction_intvar extends Instruction implements Interface_OneIntArg {
    public int arg_i;

    public Instruction_intvar(byte c) {
        super(c);
    }

    @Override // soot.coffi.Instruction
    public String toString(cp_info[] constant_pool) {
        return String.valueOf(super.toString(constant_pool)) + Instruction.argsep + Instruction.LOCALPREFIX + this.arg_i;
    }

    @Override // soot.coffi.Instruction
    public int nextOffset(int curr) {
        return curr + 3;
    }

    @Override // soot.coffi.Instruction
    public int parse(byte[] bc, int index) {
        this.arg_i = getShort(bc, index);
        return index + 2;
    }

    @Override // soot.coffi.Instruction
    public int compile(byte[] bc, int index) {
        int index2 = index + 1;
        bc[index] = this.code;
        shortToBytes((short) this.arg_i, bc, index2);
        return index2 + 2;
    }

    @Override // soot.coffi.Interface_OneIntArg
    public int getIntArg() {
        return this.arg_i;
    }
}
