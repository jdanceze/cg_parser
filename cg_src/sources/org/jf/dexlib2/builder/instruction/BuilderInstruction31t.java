package org.jf.dexlib2.builder.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderOffsetInstruction;
import org.jf.dexlib2.builder.Label;
import org.jf.dexlib2.iface.instruction.formats.Instruction31t;
import org.jf.dexlib2.util.Preconditions;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/builder/instruction/BuilderInstruction31t.class */
public class BuilderInstruction31t extends BuilderOffsetInstruction implements Instruction31t {
    public static final Format FORMAT = Format.Format31t;
    protected final int registerA;

    public BuilderInstruction31t(@Nonnull Opcode opcode, int registerA, @Nonnull Label target) {
        super(opcode, target);
        this.registerA = Preconditions.checkByteRegister(registerA);
    }

    @Override // org.jf.dexlib2.iface.instruction.OneRegisterInstruction
    public int getRegisterA() {
        return this.registerA;
    }

    @Override // org.jf.dexlib2.builder.BuilderInstruction
    public Format getFormat() {
        return FORMAT;
    }
}
