package org.jf.dexlib2.builder;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.OffsetInstruction;
import org.jf.util.ExceptionWithContext;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/builder/BuilderOffsetInstruction.class */
public abstract class BuilderOffsetInstruction extends BuilderInstruction implements OffsetInstruction {
    @Nonnull
    protected final Label target;

    public BuilderOffsetInstruction(@Nonnull Opcode opcode, @Nonnull Label target) {
        super(opcode);
        this.target = target;
    }

    @Override // org.jf.dexlib2.iface.instruction.OffsetInstruction
    public int getCodeOffset() {
        int codeOffset = internalGetCodeOffset();
        if (getCodeUnits() == 1) {
            if (codeOffset < -128 || codeOffset > 127) {
                throw new ExceptionWithContext("Invalid instruction offset: %d. Offset must be in [-128, 127]", Integer.valueOf(codeOffset));
            }
        } else if (getCodeUnits() == 2 && (codeOffset < -32768 || codeOffset > 32767)) {
            throw new ExceptionWithContext("Invalid instruction offset: %d. Offset must be in [-32768, 32767]", Integer.valueOf(codeOffset));
        }
        return codeOffset;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int internalGetCodeOffset() {
        return this.target.getCodeAddress() - getLocation().getCodeAddress();
    }

    @Nonnull
    public Label getTarget() {
        return this.target;
    }
}
