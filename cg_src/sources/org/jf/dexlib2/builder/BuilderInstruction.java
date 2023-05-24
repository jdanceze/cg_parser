package org.jf.dexlib2.builder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.util.Preconditions;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/builder/BuilderInstruction.class */
public abstract class BuilderInstruction implements Instruction {
    @Nonnull
    protected final Opcode opcode;
    @Nullable
    MethodLocation location;

    public abstract Format getFormat();

    /* JADX INFO: Access modifiers changed from: protected */
    public BuilderInstruction(@Nonnull Opcode opcode) {
        Preconditions.checkFormat(opcode, getFormat());
        this.opcode = opcode;
    }

    @Override // org.jf.dexlib2.iface.instruction.Instruction
    @Nonnull
    public Opcode getOpcode() {
        return this.opcode;
    }

    @Override // org.jf.dexlib2.iface.instruction.Instruction
    public int getCodeUnits() {
        return getFormat().size / 2;
    }

    @Nonnull
    public MethodLocation getLocation() {
        if (this.location == null) {
            throw new IllegalStateException("Cannot get the location of an instruction that hasn't been added to a method.");
        }
        return this.location;
    }
}
