package soot.coffi;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:soot/coffi/Instruction_Iinc.class */
public class Instruction_Iinc extends Instruction_bytevar {
    public int arg_c;

    public Instruction_Iinc() {
        super((byte) -124);
        this.name = "iinc";
    }

    @Override // soot.coffi.Instruction_bytevar, soot.coffi.Instruction
    public String toString(cp_info[] constant_pool) {
        return String.valueOf(super.toString(constant_pool)) + Instruction.argsep + this.arg_c;
    }

    @Override // soot.coffi.Instruction_bytevar, soot.coffi.Instruction
    public int nextOffset(int curr) {
        return super.nextOffset(curr) + (this.isWide ? 2 : 1);
    }

    @Override // soot.coffi.Instruction_bytevar, soot.coffi.Instruction
    public int parse(byte[] bc, int index) {
        int index2 = super.parse(bc, index);
        if (!this.isWide) {
            this.arg_c = bc[index2];
            return index2 + 1;
        }
        int constbyte1 = bc[index2] & 255;
        int constbyte2 = bc[index2 + 1] & 255;
        this.arg_c = (short) ((constbyte1 << 8) | constbyte2);
        return index2 + 2;
    }

    @Override // soot.coffi.Instruction_bytevar, soot.coffi.Instruction
    public int compile(byte[] bc, int index) {
        int index2 = super.compile(bc, index);
        bc[index2] = (byte) (this.arg_c & 255);
        return index2 + 1;
    }
}
