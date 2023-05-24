package soot.toDex.instructions;

import java.util.BitSet;
import net.bytebuddy.asm.Advice;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.builder.instruction.BuilderInstruction22t;
import soot.toDex.LabelAssigner;
import soot.toDex.Register;
/* loaded from: gencallgraphv3.jar:soot/toDex/instructions/Insn22t.class */
public class Insn22t extends InsnWithOffset implements TwoRegInsn {
    public Insn22t(Opcode opc, Register regA, Register regB) {
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
        return new BuilderInstruction22t(this.opc, (byte) getRegA().getNumber(), (byte) getRegB().getNumber(), assigner.getOrCreateLabel(this.target));
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

    @Override // soot.toDex.instructions.InsnWithOffset
    public int getMaxJumpOffset() {
        return Advice.MethodSizeHandler.UNDEFINED_SIZE;
    }
}
