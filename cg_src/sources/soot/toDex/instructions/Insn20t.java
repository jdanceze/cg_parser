package soot.toDex.instructions;

import net.bytebuddy.asm.Advice;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.builder.instruction.BuilderInstruction20t;
import soot.toDex.LabelAssigner;
/* loaded from: gencallgraphv3.jar:soot/toDex/instructions/Insn20t.class */
public class Insn20t extends InsnWithOffset {
    public Insn20t(Opcode opc) {
        super(opc);
    }

    @Override // soot.toDex.instructions.AbstractInsn
    protected BuilderInstruction getRealInsn0(LabelAssigner assigner) {
        return new BuilderInstruction20t(this.opc, assigner.getOrCreateLabel(this.target));
    }

    @Override // soot.toDex.instructions.InsnWithOffset
    public int getMaxJumpOffset() {
        return Advice.MethodSizeHandler.UNDEFINED_SIZE;
    }
}
