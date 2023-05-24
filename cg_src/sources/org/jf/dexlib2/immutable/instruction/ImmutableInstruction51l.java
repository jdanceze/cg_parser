package org.jf.dexlib2.immutable.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.formats.Instruction51l;
import org.jf.dexlib2.util.Preconditions;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/immutable/instruction/ImmutableInstruction51l.class */
public class ImmutableInstruction51l extends ImmutableInstruction implements Instruction51l {
    public static final Format FORMAT = Format.Format51l;
    protected final int registerA;
    protected final long literal;

    public ImmutableInstruction51l(@Nonnull Opcode opcode, int registerA, long literal) {
        super(opcode);
        this.registerA = Preconditions.checkByteRegister(registerA);
        this.literal = literal;
    }

    public static ImmutableInstruction51l of(Instruction51l instruction) {
        if (instruction instanceof ImmutableInstruction51l) {
            return (ImmutableInstruction51l) instruction;
        }
        return new ImmutableInstruction51l(instruction.getOpcode(), instruction.getRegisterA(), instruction.getWideLiteral());
    }

    @Override // org.jf.dexlib2.iface.instruction.OneRegisterInstruction
    public int getRegisterA() {
        return this.registerA;
    }

    @Override // org.jf.dexlib2.iface.instruction.WideLiteralInstruction
    public long getWideLiteral() {
        return this.literal;
    }

    @Override // org.jf.dexlib2.immutable.instruction.ImmutableInstruction
    public Format getFormat() {
        return FORMAT;
    }
}
