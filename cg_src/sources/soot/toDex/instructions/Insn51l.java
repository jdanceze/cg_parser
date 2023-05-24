package soot.toDex.instructions;

import java.util.BitSet;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.builder.instruction.BuilderInstruction51l;
import soot.toDex.LabelAssigner;
import soot.toDex.Register;
/* loaded from: gencallgraphv3.jar:soot/toDex/instructions/Insn51l.class */
public class Insn51l extends AbstractInsn implements OneRegInsn {
    private long litB;

    public Insn51l(Opcode opc, Register regA, long litB) {
        super(opc);
        this.regs.add(regA);
        this.litB = litB;
    }

    @Override // soot.toDex.instructions.OneRegInsn
    public Register getRegA() {
        return this.regs.get(0);
    }

    public long getLitB() {
        return this.litB;
    }

    @Override // soot.toDex.instructions.AbstractInsn
    protected BuilderInstruction getRealInsn0(LabelAssigner assigner) {
        return new BuilderInstruction51l(this.opc, (short) getRegA().getNumber(), getLitB());
    }

    @Override // soot.toDex.instructions.AbstractInsn, soot.toDex.instructions.Insn
    public BitSet getIncompatibleRegs() {
        BitSet incompatRegs = new BitSet(1);
        if (!getRegA().fitsShort()) {
            incompatRegs.set(0);
        }
        return incompatRegs;
    }

    @Override // soot.toDex.instructions.AbstractInsn
    public String toString() {
        return String.valueOf(super.toString()) + " lit: " + getLitB();
    }
}
