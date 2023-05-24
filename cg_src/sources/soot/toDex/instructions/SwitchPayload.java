package soot.toDex.instructions;

import java.util.List;
import net.bytebuddy.asm.Advice;
import soot.Unit;
/* loaded from: gencallgraphv3.jar:soot/toDex/instructions/SwitchPayload.class */
public abstract class SwitchPayload extends AbstractPayload {
    protected Insn31t switchInsn;
    protected List<Unit> targets;

    public SwitchPayload(List<Unit> targets) {
        this.targets = targets;
    }

    public void setSwitchInsn(Insn31t switchInsn) {
        this.switchInsn = switchInsn;
    }

    @Override // soot.toDex.instructions.InsnWithOffset
    public int getMaxJumpOffset() {
        return Advice.MethodSizeHandler.UNDEFINED_SIZE;
    }
}
