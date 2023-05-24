package soot.coffi;

import soot.jimple.Jimple;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:soot/coffi/Instruction_Newarray.class */
public class Instruction_Newarray extends Instruction {
    public static final int T_BOOLEAN = 4;
    public static final int T_CHAR = 5;
    public static final int T_FLOAT = 6;
    public static final int T_DOUBLE = 7;
    public static final int T_BYTE = 8;
    public static final int T_SHORT = 9;
    public static final int T_INT = 10;
    public static final int T_LONG = 11;
    public byte atype;

    public Instruction_Newarray() {
        super((byte) -68);
        this.name = Jimple.NEWARRAY;
    }

    @Override // soot.coffi.Instruction
    public String toString(cp_info[] constant_pool) {
        String args;
        switch (this.atype) {
            case 4:
                args = "boolean";
                break;
            case 5:
                args = "char";
                break;
            case 6:
                args = Jimple.FLOAT;
                break;
            case 7:
                args = "double";
                break;
            case 8:
                args = "byte";
                break;
            case 9:
                args = "short";
                break;
            case 10:
                args = "int";
                break;
            case 11:
                args = "long";
                break;
            default:
                args = Integer.toString(this.atype);
                break;
        }
        return String.valueOf(super.toString(constant_pool)) + Instruction.argsep + args;
    }

    @Override // soot.coffi.Instruction
    public int nextOffset(int curr) {
        return curr + 2;
    }

    @Override // soot.coffi.Instruction
    public int parse(byte[] bc, int index) {
        this.atype = bc[index];
        return index + 1;
    }

    @Override // soot.coffi.Instruction
    public int compile(byte[] bc, int index) {
        int index2 = index + 1;
        bc[index] = this.code;
        bc[index2] = this.atype;
        return index2 + 1;
    }
}
