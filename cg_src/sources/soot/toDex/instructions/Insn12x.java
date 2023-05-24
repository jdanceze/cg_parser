package soot.toDex.instructions;

import java.util.BitSet;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.builder.instruction.BuilderInstruction12x;
import soot.toDex.LabelAssigner;
import soot.toDex.Register;
/* loaded from: gencallgraphv3.jar:soot/toDex/instructions/Insn12x.class */
public class Insn12x extends AbstractInsn implements TwoRegInsn {
    public Insn12x(Opcode opc, Register regA, Register regB) {
        super(opc);
        this.regs.add(regA);
        this.regs.add(regB);
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
        return new BuilderInstruction12x(this.opc, (byte) getRegA().getNumber(), (byte) getRegB().getNumber());
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
}
