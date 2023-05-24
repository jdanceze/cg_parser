package soot.coffi;
/* loaded from: gencallgraphv3.jar:soot/coffi/Instruction_noargs.class */
class Instruction_noargs extends Instruction {
    public Instruction_noargs(byte c) {
        super(c);
    }

    @Override // soot.coffi.Instruction
    public int parse(byte[] bc, int index) {
        return index;
    }

    @Override // soot.coffi.Instruction
    public int compile(byte[] bc, int index) {
        int index2 = index + 1;
        bc[index] = this.code;
        return index2;
    }
}
