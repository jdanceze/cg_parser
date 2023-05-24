package org.jf.dexlib2.immutable.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.formats.Instruction21t;
import org.jf.dexlib2.util.Preconditions;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/immutable/instruction/ImmutableInstruction21t.class */
public class ImmutableInstruction21t extends ImmutableInstruction implements Instruction21t {
    public static final Format FORMAT = Format.Format21t;
    protected final int registerA;
    protected final int codeOffset;

    public ImmutableInstruction21t(@Nonnull Opcode opcode, int registerA, int codeOffset) {
        super(opcode);
        this.registerA = Preconditions.checkByteRegister(registerA);
        this.codeOffset = Preconditions.checkShortCodeOffset(codeOffset);
    }

    public static ImmutableInstruction21t of(Instruction21t instruction) {
        if (instruction instanceof ImmutableInstruction21t) {
            return (ImmutableInstruction21t) instruction;
        }
        return new ImmutableInstruction21t(instruction.getOpcode(), instruction.getRegisterA(), instruction.getCodeOffset());
    }

    @Override // org.jf.dexlib2.iface.instruction.OneRegisterInstruction
    public int getRegisterA() {
        return this.registerA;
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
