package soot.toDex.instructions;

import java.util.BitSet;
import net.bytebuddy.asm.Advice;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.builder.instruction.BuilderInstruction21t;
import soot.toDex.LabelAssigner;
import soot.toDex.Register;
/* loaded from: gencallgraphv3.jar:soot/toDex/instructions/Insn21t.class */
public class Insn21t extends InsnWithOffset implements OneRegInsn {
    public Insn21t(Opcode opc, Register regA) {
        super(opc);
        this.regs.add(regA);
    }

    @Override // soot.toDex.instructions.OneRegInsn
    public Register getRegA() {
        return this.regs.get(0);
    }

    @Override // soot.toDex.instructions.AbstractInsn
    protected BuilderInstruction getRealInsn0(LabelAssigner assigner) {
        return new BuilderInstruction21t(this.opc, (short) getRegA().getNumber(), assigner.getOrCreateLabel(this.target));
    }

    @Override // soot.toDex.instructions.AbstractInsn, soot.toDex.instructions.Insn
    public BitSet getIncompatibleRegs() {
        BitSet incompatRegs = new BitSet(1);
        if (!getRegA().fitsShort()) {
            incompatRegs.set(0);
        }
        return incompatRegs;
    }

    @Override // soot.toDex.instructions.InsnWithOffset
    public int getMaxJumpOffset() {
        return Advice.MethodSizeHandler.UNDEFINED_SIZE;
    }
}
