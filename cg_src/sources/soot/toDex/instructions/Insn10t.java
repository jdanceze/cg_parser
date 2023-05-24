package soot.toDex.instructions;

import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.builder.instruction.BuilderInstruction10t;
import soot.toDex.LabelAssigner;
/* loaded from: gencallgraphv3.jar:soot/toDex/instructions/Insn10t.class */
public class Insn10t extends InsnWithOffset {
    public Insn10t(Opcode opc) {
        super(opc);
    }

    @Override // soot.toDex.instructions.AbstractInsn
    protected BuilderInstruction getRealInsn0(LabelAssigner assigner) {
        if (this.target == null) {
            throw new RuntimeException("Cannot jump to a NULL target");
        }
        return new BuilderInstruction10t(this.opc, assigner.getOrCreateLabel(this.target));
    }

    @Override // soot.toDex.instructions.InsnWithOffset
    public int getMaxJumpOffset() {
        return 127;
    }
}
