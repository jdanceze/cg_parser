package soot.toDex.instructions;

import java.util.BitSet;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.builder.instruction.BuilderInstruction11n;
import soot.toDex.LabelAssigner;
import soot.toDex.Register;
/* loaded from: gencallgraphv3.jar:soot/toDex/instructions/Insn11n.class */
public class Insn11n extends AbstractInsn implements OneRegInsn {
    private byte litB;

    public Insn11n(Opcode opc, Register regA, byte litB) {
        super(opc);
        this.regs.add(regA);
        this.litB = litB;
    }

    @Override // soot.toDex.instructions.OneRegInsn
    public Register getRegA() {
        return this.regs.get(0);
    }

    public byte getLitB() {
        return this.litB;
    }

    @Override // soot.toDex.instructions.AbstractInsn
    protected BuilderInstruction getRealInsn0(LabelAssigner assigner) {
        return new BuilderInstruction11n(this.opc, (byte) getRegA().getNumber(), getLitB());
    }

    @Override // soot.toDex.instructions.AbstractInsn, soot.toDex.instructions.Insn
    public BitSet getIncompatibleRegs() {
        BitSet incompatRegs = new BitSet(1);
        if (!getRegA().fitsByte()) {
            incompatRegs.set(0);
        }
        return incompatRegs;
    }

    @Override // soot.toDex.instructions.AbstractInsn
    public String toString() {
        return String.valueOf(super.toString()) + " lit: " + ((int) getLitB());
    }
}
