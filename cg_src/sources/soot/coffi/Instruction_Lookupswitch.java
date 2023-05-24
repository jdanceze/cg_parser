package soot.coffi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.jimple.Jimple;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:soot/coffi/Instruction_Lookupswitch.class */
public class Instruction_Lookupswitch extends Instruction {
    private static final Logger logger = LoggerFactory.getLogger(Instruction_Lookupswitch.class);
    public byte pad;
    public int default_offset;
    public int npairs;
    public int[] match_offsets;
    public Instruction default_inst;
    public Instruction[] match_insts;

    public Instruction_Lookupswitch() {
        super((byte) -85);
        this.name = Jimple.LOOKUPSWITCH;
        this.branches = true;
    }

    @Override // soot.coffi.Instruction
    public String toString(cp_info[] constant_pool) {
        String args = String.valueOf(super.toString(constant_pool)) + Instruction.argsep + "(" + Integer.toString(this.pad) + ")";
        String args2 = String.valueOf(String.valueOf(args) + Instruction.argsep + Integer.toString(this.default_inst.label)) + Instruction.argsep + Integer.toString(this.npairs) + ": ";
        for (int i = 0; i < this.npairs; i++) {
            args2 = String.valueOf(args2) + "case " + Integer.toString(this.match_offsets[i * 2]) + ": label_" + Integer.toString(this.match_insts[i].label);
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
        this.npairs = getInt(bc, index3);
        int index4 = index3 + 4;
        if (this.npairs > 0) {
            this.match_offsets = new int[this.npairs * 2];
            int j = 0;
            do {
                this.match_offsets[j] = getInt(bc, index4);
                int j2 = j + 1;
                int index5 = index4 + 4;
                this.match_offsets[j2] = getInt(bc, index5);
                index4 = index5 + 4;
                j = j2 + 1;
            } while (j < this.npairs * 2);
            return index4;
        }
        return index4;
    }

    @Override // soot.coffi.Instruction
    public int nextOffset(int curr) {
        int siz = 0;
        int i = (curr + 1) % 4;
        if (i != 0) {
            siz = 4 - i;
        }
        return curr + siz + 9 + (this.npairs * 8);
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
        int index4 = intToBytes(this.npairs, bc, index2);
        for (int i3 = 0; i3 < this.npairs; i3++) {
            int index5 = intToBytes(this.match_offsets[i3 * 2], bc, index4);
            if (this.match_insts[i3] != null) {
                intToBytes = intToBytes(this.match_insts[i3].label - this.label, bc, index5);
            } else {
                intToBytes = intToBytes(this.match_offsets[(i3 * 2) + 1], bc, index5);
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
        if (this.npairs > 0) {
            this.match_insts = new Instruction[this.npairs];
            for (int i = 0; i < this.npairs; i++) {
                this.match_insts[i] = bc.locateInst(this.match_offsets[(i * 2) + 1] + this.label);
                if (this.match_insts[i] == null) {
                    logger.warn("can't locate target of instruction");
                    logger.debug(" which should be at byte address " + (this.label + this.match_offsets[(i * 2) + 1]));
                } else {
                    this.match_insts[i].labelled = true;
                }
            }
        }
    }

    @Override // soot.coffi.Instruction
    public Instruction[] branchpoints(Instruction next) {
        Instruction[] i = new Instruction[this.npairs + 1];
        i[0] = this.default_inst;
        for (int j = 1; j < this.npairs + 1; j++) {
            i[j] = this.match_insts[j - 1];
        }
        return i;
    }
}
