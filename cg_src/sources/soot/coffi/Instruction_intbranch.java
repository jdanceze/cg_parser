package soot.coffi;
/* loaded from: gencallgraphv3.jar:soot/coffi/Instruction_intbranch.class */
class Instruction_intbranch extends Instruction_branch {
    public Instruction_intbranch(byte c) {
        super(c);
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
        if (this.target != null) {
            shortToBytes((short) (this.target.label - this.label), bc, index2);
        } else {
            shortToBytes((short) this.arg_i, bc, index2);
        }
        return index2 + 2;
    }
}
