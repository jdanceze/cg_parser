package soot.toDex.instructions;

import org.jf.dexlib2.Opcode;
import soot.jimple.Stmt;
/* loaded from: gencallgraphv3.jar:soot/toDex/instructions/InsnWithOffset.class */
public abstract class InsnWithOffset extends AbstractInsn {
    protected Stmt target;

    public abstract int getMaxJumpOffset();

    public InsnWithOffset(Opcode opc) {
        super(opc);
    }

    public void setTarget(Stmt target) {
        if (target == null) {
            throw new RuntimeException("Cannot jump to a NULL target");
        }
        this.target = target;
    }

    public Stmt getTarget() {
        return this.target;
    }
}
