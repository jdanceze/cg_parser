package soot.coffi;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:soot/coffi/Instruction_bytevar.class */
public class Instruction_bytevar extends Instruction implements Interface_OneIntArg {
    public int arg_b;
    public boolean isWide;

    public Instruction_bytevar(byte c) {
        super(c);
    }

    @Override // soot.coffi.Instruction
    public String toString(cp_info[] constant_pool) {
        return String.valueOf(super.toString(constant_pool)) + Instruction.argsep + Instruction.LOCALPREFIX + this.arg_b;
    }

    @Override // soot.coffi.Instruction
    public int nextOffset(int curr) {
        return curr + 1 + (this.isWide ? 3 : 1);
    }

    @Override // soot.coffi.Instruction
    public int parse(byte[] bc, int index) {
        int indexbyte1 = bc[index] & 255;
        if (this.isWide) {
            int indexbyte2 = bc[index + 1] & 255;
            this.arg_b = (indexbyte1 << 8) | indexbyte2;
            return index + 2;
        }
        this.arg_b = indexbyte1;
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

    @Override // soot.coffi.Interface_OneIntArg
    public int getIntArg() {
        return this.arg_b;
    }
}
