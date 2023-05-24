package org.jf.dexlib2.immutable.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.formats.Instruction11x;
import org.jf.dexlib2.util.Preconditions;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/immutable/instruction/ImmutableInstruction11x.class */
public class ImmutableInstruction11x extends ImmutableInstruction implements Instruction11x {
    public static final Format FORMAT = Format.Format11x;
    protected final int registerA;

    public ImmutableInstruction11x(@Nonnull Opcode opcode, int registerA) {
        super(opcode);
        this.registerA = Preconditions.checkByteRegister(registerA);
    }

    public static ImmutableInstruction11x of(Instruction11x instruction) {
        if (instruction instanceof ImmutableInstruction11x) {
            return (ImmutableInstruction11x) instruction;
        }
        return new ImmutableInstruction11x(instruction.getOpcode(), instruction.getRegisterA());
    }

    @Override // org.jf.dexlib2.iface.instruction.OneRegisterInstruction
    public int getRegisterA() {
        return this.registerA;
    }

    @Override // org.jf.dexlib2.immutable.instruction.ImmutableInstruction
    public Format getFormat() {
        return FORMAT;
    }
}
