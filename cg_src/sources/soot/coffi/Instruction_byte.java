package soot.coffi;

import soot.dava.internal.AST.ASTNode;
/* loaded from: gencallgraphv3.jar:soot/coffi/Instruction_byte.class */
class Instruction_byte extends Instruction {
    public byte arg_b;

    public Instruction_byte(byte c) {
        super(c);
    }

    @Override // soot.coffi.Instruction
    public String toString(cp_info[] constant_pool) {
        return String.valueOf(super.toString(constant_pool)) + Instruction.argsep + ((int) this.arg_b);
    }

    @Override // soot.coffi.Instruction
    public int nextOffset(int curr) {
        return curr + 2;
    }

    @Override // soot.coffi.Instruction
    public int parse(byte[] bc, int index) {
        this.arg_b = bc[index];
        return index + 1;
    }

    @Override // soot.coffi.Instruction
    public int compile(byte[] bc, int index) {
        int index2 = index + 1;
        bc[index] = this.code;
        int index3 = index2 + 1;
        bc[index2] = this.arg_b;
        return index3;
    }

    @Override // soot.coffi.Instruction
    public String toString() {
        return String.valueOf(super.toString()) + ASTNode.TAB + ((int) this.arg_b);
    }
}
