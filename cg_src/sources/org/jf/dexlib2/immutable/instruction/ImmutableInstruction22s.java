package org.jf.dexlib2.immutable.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.formats.Instruction22s;
import org.jf.dexlib2.util.Preconditions;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/immutable/instruction/ImmutableInstruction22s.class */
public class ImmutableInstruction22s extends ImmutableInstruction implements Instruction22s {
    public static final Format FORMAT = Format.Format22s;
    protected final int registerA;
    protected final int registerB;
    protected final int literal;

    public ImmutableInstruction22s(@Nonnull Opcode opcode, int registerA, int registerB, int literal) {
        super(opcode);
        this.registerA = Preconditions.checkNibbleRegister(registerA);
        this.registerB = Preconditions.checkNibbleRegister(registerB);
        this.literal = Preconditions.checkShortLiteral(literal);
    }

    public static ImmutableInstruction22s of(Instruction22s instruction) {
        if (instruction instanceof ImmutableInstruction22s) {
            return (ImmutableInstruction22s) instruction;
        }
        return new ImmutableInstruction22s(instruction.getOpcode(), instruction.getRegisterA(), instruction.getRegisterB(), instruction.getNarrowLiteral());
    }

    @Override // org.jf.dexlib2.iface.instruction.OneRegisterInstruction
    public int getRegisterA() {
        return this.registerA;
    }

    @Override // org.jf.dexlib2.iface.instruction.TwoRegisterInstruction
    public int getRegisterB() {
        return this.registerB;
    }

    @Override // org.jf.dexlib2.iface.instruction.NarrowLiteralInstruction
    public int getNarrowLiteral() {
        return this.literal;
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
