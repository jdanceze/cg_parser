package soot.toDex.instructions;

import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.builder.instruction.BuilderInstruction10x;
import soot.toDex.LabelAssigner;
/* loaded from: gencallgraphv3.jar:soot/toDex/instructions/Insn10x.class */
public class Insn10x extends AbstractInsn {
    public Insn10x(Opcode opc) {
        super(opc);
    }

    @Override // soot.toDex.instructions.AbstractInsn
    protected BuilderInstruction getRealInsn0(LabelAssigner assigner) {
        return new BuilderInstruction10x(this.opc);
    }
}
