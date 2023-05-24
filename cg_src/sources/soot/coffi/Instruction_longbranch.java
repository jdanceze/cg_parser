package soot.coffi;
/* loaded from: gencallgraphv3.jar:soot/coffi/Instruction_longbranch.class */
class Instruction_longbranch extends Instruction_branch {
    public Instruction_longbranch(byte c) {
        super(c);
    }

    @Override // soot.coffi.Instruction
    public int nextOffset(int curr) {
        return curr + 5;
    }

    @Override // soot.coffi.Instruction
    public int parse(byte[] bc, int index) {
        this.arg_i = getInt(bc, index);
        return index + 4;
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
        return index2 + 4;
    }
}
