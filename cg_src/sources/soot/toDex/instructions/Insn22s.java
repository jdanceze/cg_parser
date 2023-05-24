package soot.toDex.instructions;

import java.util.BitSet;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.builder.instruction.BuilderInstruction22s;
import soot.toDex.LabelAssigner;
import soot.toDex.Register;
/* loaded from: gencallgraphv3.jar:soot/toDex/instructions/Insn22s.class */
public class Insn22s extends AbstractInsn implements TwoRegInsn {
    private short litC;

    public Insn22s(Opcode opc, Register regA, Register regB, short litC) {
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

    public short getLitC() {
        return this.litC;
    }

    @Override // soot.toDex.instructions.AbstractInsn
    protected BuilderInstruction getRealInsn0(LabelAssigner assigner) {
        return new BuilderInstruction22s(this.opc, (byte) getRegA().getNumber(), (byte) getRegB().getNumber(), getLitC());
    }

    @Override // soot.toDex.instructions.AbstractInsn, soot.toDex.instructions.Insn
    public BitSet getIncompatibleRegs() {
        BitSet incompatRegs = new BitSet(2);
        if (!getRegA().fitsByte()) {
            incompatRegs.set(0);
        }
        if (!getRegB().fitsByte()) {
            incompatRegs.set(1);
        }
        return incompatRegs;
    }

    @Override // soot.toDex.instructions.AbstractInsn
    public String toString() {
        return String.valueOf(super.toString()) + " lit: " + ((int) getLitC());
    }
}
