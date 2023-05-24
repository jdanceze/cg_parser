package soot.toDex.instructions;

import java.util.BitSet;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.builder.instruction.BuilderInstruction22c;
import org.jf.dexlib2.iface.reference.Reference;
import soot.toDex.LabelAssigner;
import soot.toDex.Register;
/* loaded from: gencallgraphv3.jar:soot/toDex/instructions/Insn22c.class */
public class Insn22c extends AbstractInsn implements TwoRegInsn {
    private Reference referencedItem;

    public Insn22c(Opcode opc, Register regA, Register regB, Reference referencedItem) {
        super(opc);
        this.regs.add(regA);
        this.regs.add(regB);
        this.referencedItem = referencedItem;
    }

    @Override // soot.toDex.instructions.OneRegInsn
    public Register getRegA() {
        return this.regs.get(0);
    }

    @Override // soot.toDex.instructions.TwoRegInsn
    public Register getRegB() {
        return this.regs.get(1);
    }

    @Override // soot.toDex.instructions.AbstractInsn
    protected BuilderInstruction getRealInsn0(LabelAssigner assigner) {
        return new BuilderInstruction22c(this.opc, getRegA().getNumber(), getRegB().getNumber(), this.referencedItem);
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
        return String.valueOf(super.toString()) + " ref: " + this.referencedItem;
    }
}
