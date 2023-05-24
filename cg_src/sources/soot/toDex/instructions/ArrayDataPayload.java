package soot.toDex.instructions;

import java.util.List;
import net.bytebuddy.asm.Advice;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.builder.instruction.BuilderArrayPayload;
import soot.toDex.LabelAssigner;
/* loaded from: gencallgraphv3.jar:soot/toDex/instructions/ArrayDataPayload.class */
public class ArrayDataPayload extends AbstractPayload {
    private final int elementWidth;
    private final List<Number> arrayElements;

    public ArrayDataPayload(int elementWidth, List<Number> arrayElements) {
        this.elementWidth = elementWidth;
        this.arrayElements = arrayElements;
    }

    @Override // soot.toDex.instructions.AbstractInsn, soot.toDex.instructions.Insn
    public int getSize() {
        return 4 + (((this.arrayElements.size() * this.elementWidth) + 1) / 2);
    }

    @Override // soot.toDex.instructions.InsnWithOffset
    public int getMaxJumpOffset() {
        return Advice.MethodSizeHandler.UNDEFINED_SIZE;
    }

    @Override // soot.toDex.instructions.AbstractInsn
    protected BuilderInstruction getRealInsn0(LabelAssigner assigner) {
        return new BuilderArrayPayload(this.elementWidth, this.arrayElements);
    }
}
