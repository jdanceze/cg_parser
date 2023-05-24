package org.jf.dexlib2.immutable.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.formats.Instruction30t;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/immutable/instruction/ImmutableInstruction30t.class */
public class ImmutableInstruction30t extends ImmutableInstruction implements Instruction30t {
    public static final Format FORMAT = Format.Format30t;
    protected final int codeOffset;

    public ImmutableInstruction30t(@Nonnull Opcode opcode, int codeOffset) {
        super(opcode);
        this.codeOffset = codeOffset;
    }

    public static ImmutableInstruction30t of(Instruction30t instruction) {
        if (instruction instanceof ImmutableInstruction30t) {
            return (ImmutableInstruction30t) instruction;
        }
        return new ImmutableInstruction30t(instruction.getOpcode(), instruction.getCodeOffset());
    }

    @Override // org.jf.dexlib2.iface.instruction.OffsetInstruction
    public int getCodeOffset() {
        return this.codeOffset;
    }

    @Override // org.jf.dexlib2.immutable.instruction.ImmutableInstruction
    public Format getFormat() {
        return FORMAT;
    }
}
