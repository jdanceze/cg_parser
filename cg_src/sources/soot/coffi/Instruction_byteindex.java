package soot.coffi;
/* loaded from: gencallgraphv3.jar:soot/coffi/Instruction_byteindex.class */
class Instruction_byteindex extends Instruction {
    public short arg_b;

    public Instruction_byteindex(byte c) {
        super(c);
    }

    @Override // soot.coffi.Instruction
    public String toString(cp_info[] constant_pool) {
        int i = this.arg_b & 255;
        return String.valueOf(super.toString(constant_pool)) + Instruction.argsep + "[" + constant_pool[i].toString(constant_pool) + "]";
    }

    @Override // soot.coffi.Instruction
    public int nextOffset(int curr) {
        return curr + 2;
    }

    @Override // soot.coffi.Instruction
    public void markCPRefs(boolean[] refs) {
        refs[this.arg_b & 255] = true;
    }

    @Override // soot.coffi.Instruction
    public void redirectCPRefs(short[] redirect) {
        this.arg_b = (byte) redirect[this.arg_b & 255];
    }

    @Override // soot.coffi.Instruction
    public int parse(byte[] bc, int index) {
        this.arg_b = bc[index];
        this.arg_b = this.arg_b >= 0 ? this.arg_b : (short) (256 + this.arg_b);
        return index + 1;
    }

    @Override // soot.coffi.Instruction
    public int compile(byte[] bc, int index) {
        int index2 = index + 1;
        bc[index] = this.code;
        int index3 = index2 + 1;
        bc[index2] = (byte) this.arg_b;
        return index3;
    }
}
