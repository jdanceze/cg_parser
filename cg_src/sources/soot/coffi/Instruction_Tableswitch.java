package soot.coffi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.jimple.Jimple;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:soot/coffi/Instruction_Tableswitch.class */
public class Instruction_Tableswitch extends Instruction {
    private static final Logger logger = LoggerFactory.getLogger(Instruction_Tableswitch.class);
    public byte pad;
    public int default_offset;
    public int low;
    public int high;
    public int[] jump_offsets;
    public Instruction default_inst;
    public Instruction[] jump_insts;

    public Instruction_Tableswitch() {
        super((byte) -86);
        this.name = Jimple.TABLESWITCH;
        this.branches = true;
    }

    @Override // soot.coffi.Instruction
    public String toString(cp_info[] constant_pool) {
        String args = String.valueOf(super.toString(constant_pool)) + Instruction.argsep + "(" + Integer.toString(this.pad) + ")";
        String args2 = String.valueOf(String.valueOf(String.valueOf(args) + Instruction.argsep + "label_" + Integer.toString(this.default_inst.label)) + Instruction.argsep + Integer.toString(this.low)) + Instruction.argsep + Integer.toString(this.high) + ": ";
        for (int i = 0; i < (this.high - this.low) + 1; i++) {
            args2 = String.valueOf(args2) + Instruction.argsep + "label_" + Integer.toString(this.jump_insts[i].label);
        }
        return args2;
    }

    @Override // soot.coffi.Instruction
    public int parse(byte[] bc, int index) {
        int i = index % 4;
        if (i != 0) {
            this.pad = (byte) (4 - i);
        } else {
            this.pad = (byte) 0;
        }
        int index2 = index + this.pad;
        this.default_offset = getInt(bc, index2);
        int index3 = index2 + 4;
        this.low = getInt(bc, index3);
        int index4 = index3 + 4;
        this.high = getInt(bc, index4);
        int index5 = index4 + 4;
        int i2 = (this.high - this.low) + 1;
        if (i2 > 0) {
            this.jump_offsets = new int[i2];
            int j = 0;
            do {
                this.jump_offsets[j] = getInt(bc, index5);
                index5 += 4;
                j++;
            } while (j < i2);
            return index5;
        }
        return index5;
    }

    @Override // soot.coffi.Instruction
    public int nextOffset(int curr) {
        int siz = 0;
        int i = (curr + 1) % 4;
        if (i != 0) {
            siz = 4 - i;
        }
        return curr + siz + 13 + (((this.high - this.low) + 1) * 4);
    }

    @Override // soot.coffi.Instruction
    public int compile(byte[] bc, int index) {
        int index2;
        int intToBytes;
        int index3 = index + 1;
        bc[index] = this.code;
        for (int i = 0; i < this.pad; i++) {
            int i2 = index3;
            index3++;
            bc[i2] = 0;
        }
        if (this.default_inst != null) {
            index2 = intToBytes(this.default_inst.label - this.label, bc, index3);
        } else {
            index2 = intToBytes(this.default_offset, bc, index3);
        }
        int index4 = intToBytes(this.high, bc, intToBytes(this.low, bc, index2));
        for (int i3 = 0; i3 <= this.high - this.low; i3++) {
            if (this.jump_insts[i3] != null) {
                intToBytes = intToBytes(this.jump_insts[i3].label - this.label, bc, index4);
            } else {
                intToBytes = intToBytes(this.jump_offsets[i3], bc, index4);
            }
            index4 = intToBytes;
        }
        return index4;
    }

    @Override // soot.coffi.Instruction
    public void offsetToPointer(ByteCode bc) {
        this.default_inst = bc.locateInst(this.default_offset + this.label);
        if (this.default_inst == null) {
            logger.warn("can't locate target of instruction");
            logger.debug(" which should be at byte address " + (this.label + this.default_offset));
        } else {
            this.default_inst.labelled = true;
        }
        if ((this.high - this.low) + 1 > 0) {
            this.jump_insts = new Instruction[(this.high - this.low) + 1];
            for (int i = 0; i < (this.high - this.low) + 1; i++) {
                this.jump_insts[i] = bc.locateInst(this.jump_offsets[i] + this.label);
                if (this.jump_insts[i] == null) {
                    logger.warn("can't locate target of instruction");
                    logger.debug(" which should be at byte address " + (this.label + this.jump_offsets[i]));
                } else {
                    this.jump_insts[i].labelled = true;
                }
            }
        }
    }

    @Override // soot.coffi.Instruction
    public Instruction[] branchpoints(Instruction next) {
        Instruction[] i = new Instruction[(this.high - this.low) + 2];
        i[0] = this.default_inst;
        for (int j = 1; j < (this.high - this.low) + 2; j++) {
            i[j] = this.jump_insts[j - 1];
        }
        return i;
    }
}
