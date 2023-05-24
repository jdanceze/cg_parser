package org.jf.dexlib2.immutable.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.formats.Instruction11n;
import org.jf.dexlib2.util.Preconditions;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/immutable/instruction/ImmutableInstruction11n.class */
public class ImmutableInstruction11n extends ImmutableInstruction implements Instruction11n {
    public static final Format FORMAT = Format.Format11n;
    protected final int registerA;
    protected final int literal;

    public ImmutableInstruction11n(@Nonnull Opcode opcode, int registerA, int literal) {
        super(opcode);
        this.registerA = Preconditions.checkNibbleRegister(registerA);
        this.literal = Preconditions.checkNibbleLiteral(literal);
    }

    public static ImmutableInstruction11n of(Instruction11n instruction) {
        if (instruction instanceof ImmutableInstruction11n) {
            return (ImmutableInstruction11n) instruction;
        }
        return new ImmutableInstruction11n(instruction.getOpcode(), instruction.getRegisterA(), instruction.getNarrowLiteral());
    }

    @Override // org.jf.dexlib2.iface.instruction.OneRegisterInstruction
    public int getRegisterA() {
        return this.registerA;
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
