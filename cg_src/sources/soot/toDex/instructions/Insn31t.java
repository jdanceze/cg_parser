package soot.toDex.instructions;

import java.util.BitSet;
import net.bytebuddy.asm.Advice;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.builder.instruction.BuilderInstruction31t;
import soot.toDex.LabelAssigner;
import soot.toDex.Register;
/* loaded from: gencallgraphv3.jar:soot/toDex/instructions/Insn31t.class */
public class Insn31t extends InsnWithOffset implements OneRegInsn {
    public AbstractPayload payload;

    public Insn31t(Opcode opc, Register regA) {
        super(opc);
        this.payload = null;
        this.regs.add(regA);
    }

    @Override // soot.toDex.instructions.OneRegInsn
    public Register getRegA() {
        return this.regs.get(0);
    }

    public void setPayload(AbstractPayload payload) {
        this.payload = payload;
    }

    @Override // soot.toDex.instructions.AbstractInsn
    protected BuilderInstruction getRealInsn0(LabelAssigner assigner) {
        return new BuilderInstruction31t(this.opc, (short) getRegA().getNumber(), assigner.getOrCreateLabel(this.payload));
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
