package org.jf.dexlib2.immutable.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.formats.Instruction10t;
import org.jf.dexlib2.util.Preconditions;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/immutable/instruction/ImmutableInstruction10t.class */
public class ImmutableInstruction10t extends ImmutableInstruction implements Instruction10t {
    public static final Format FORMAT = Format.Format10t;
    protected final int codeOffset;

    public ImmutableInstruction10t(@Nonnull Opcode opcode, int codeOffset) {
        super(opcode);
        this.codeOffset = Preconditions.checkByteCodeOffset(codeOffset);
    }

    public static ImmutableInstruction10t of(Instruction10t instruction) {
        if (instruction instanceof ImmutableInstruction10t) {
            return (ImmutableInstruction10t) instruction;
        }
        return new ImmutableInstruction10t(instruction.getOpcode(), instruction.getCodeOffset());
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
