package soot.toDex.instructions;

import java.util.BitSet;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.builder.instruction.BuilderInstruction22b;
import soot.toDex.LabelAssigner;
import soot.toDex.Register;
/* loaded from: gencallgraphv3.jar:soot/toDex/instructions/Insn22b.class */
public class Insn22b extends AbstractInsn implements TwoRegInsn {
    private byte litC;

    public Insn22b(Opcode opc, Register regA, Register regB, byte litC) {
        super(opc);
        this.regs.add(regA);
        this.regs.add(regB);
        this.litC = litC;
    }

    @Override // soot.toDex.instructions.OneRegInsn
    public Register getRegA() {
        return this.regs.get(0);
    }

    @Override // soot.toDex.instructions.TwoRegInsn
    public Register getRegB() {
        return this.regs.get(1);
    }

    public byte getLitC() {
        return this.litC;
    }

    @Override // soot.toDex.instructions.AbstractInsn
    protected BuilderInstruction getRealInsn0(LabelAssigner assigner) {
        return new BuilderInstruction22b(this.opc, (short) getRegA().getNumber(), (short) getRegB().getNumber(), getLitC());
    }

    @Override // soot.toDex.instructions.AbstractInsn, soot.toDex.instructions.Insn
    public BitSet getIncompatibleRegs() {
        BitSet incompatRegs = new BitSet(2);
        if (!getRegA().fitsShort()) {
            incompatRegs.set(0);
        }
        if (!getRegB().fitsShort()) {
            incompatRegs.set(1);
        }
        return incompatRegs;
    }

    @Override // soot.toDex.instructions.AbstractInsn
    public String toString() {
        return String.valueOf(super.toString()) + " lit: " + ((int) getLitC());
    }
}
