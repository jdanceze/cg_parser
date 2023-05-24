package soot.toDex.instructions;

import java.util.BitSet;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.builder.instruction.BuilderInstruction23x;
import soot.toDex.LabelAssigner;
import soot.toDex.Register;
/* loaded from: gencallgraphv3.jar:soot/toDex/instructions/Insn23x.class */
public class Insn23x extends AbstractInsn implements ThreeRegInsn {
    public Insn23x(Opcode opc, Register regA, Register regB, Register regC) {
        super(opc);
        this.regs.add(regA);
        this.regs.add(regB);
        this.regs.add(regC);
    }

    @Override // soot.toDex.instructions.OneRegInsn
    public Register getRegA() {
        return this.regs.get(0);
    }

    @Override // soot.toDex.instructions.TwoRegInsn
    public Register getRegB() {
        return this.regs.get(1);
    }

    @Override // soot.toDex.instructions.ThreeRegInsn
    public Register getRegC() {
        return this.regs.get(2);
    }

    @Override // soot.toDex.instructions.AbstractInsn
    protected BuilderInstruction getRealInsn0(LabelAssigner assigner) {
        return new BuilderInstruction23x(this.opc, (short) getRegA().getNumber(), (short) getRegB().getNumber(), (short) getRegC().getNumber());
    }

    @Override // soot.toDex.instructions.AbstractInsn, soot.toDex.instructions.Insn
    public BitSet getIncompatibleRegs() {
        BitSet incompatRegs = new BitSet(3);
        if (!getRegA().fitsShort()) {
            incompatRegs.set(0);
        }
        if (!getRegB().fitsShort()) {
            incompatRegs.set(1);
        }
        if (!getRegC().fitsShort()) {
            incompatRegs.set(2);
        }
        return incompatRegs;
    }
}
