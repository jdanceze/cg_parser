package org.jf.dexlib2.immutable.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.formats.Instruction21c;
import org.jf.dexlib2.iface.reference.Reference;
import org.jf.dexlib2.immutable.reference.ImmutableReference;
import org.jf.dexlib2.immutable.reference.ImmutableReferenceFactory;
import org.jf.dexlib2.util.Preconditions;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/immutable/instruction/ImmutableInstruction21c.class */
public class ImmutableInstruction21c extends ImmutableInstruction implements Instruction21c {
    public static final Format FORMAT = Format.Format21c;
    protected final int registerA;
    @Nonnull
    protected final ImmutableReference reference;

    public ImmutableInstruction21c(@Nonnull Opcode opcode, int registerA, @Nonnull Reference reference) {
        super(opcode);
        this.registerA = Preconditions.checkByteRegister(registerA);
        this.reference = ImmutableReferenceFactory.of(opcode.referenceType, reference);
    }

    public static ImmutableInstruction21c of(Instruction21c instruction) {
        if (instruction instanceof ImmutableInstruction21c) {
            return (ImmutableInstruction21c) instruction;
        }
        return new ImmutableInstruction21c(instruction.getOpcode(), instruction.getRegisterA(), instruction.getReference());
    }

    @Override // org.jf.dexlib2.iface.instruction.OneRegisterInstruction
    public int getRegisterA() {
        return this.registerA;
    }

    @Override // org.jf.dexlib2.iface.instruction.ReferenceInstruction
    @Nonnull
    public ImmutableReference getReference() {
        return this.reference;
    }

    @Override // org.jf.dexlib2.iface.instruction.ReferenceInstruction
    public int getReferenceType() {
        return this.opcode.referenceType;
    }

    @Override // org.jf.dexlib2.immutable.instruction.ImmutableInstruction
    public Format getFormat() {
        return FORMAT;
    }
}
