package org.jf.dexlib2.builder.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.iface.instruction.formats.Instruction22x;
import org.jf.dexlib2.util.Preconditions;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/builder/instruction/BuilderInstruction22x.class */
public class BuilderInstruction22x extends BuilderInstruction implements Instruction22x {
    public static final Format FORMAT = Format.Format22x;
    protected final int registerA;
    protected final int registerB;

    public BuilderInstruction22x(@Nonnull Opcode opcode, int registerA, int registerB) {
        super(opcode);
        this.registerA = Preconditions.checkByteRegister(registerA);
        this.registerB = Preconditions.checkShortRegister(registerB);
    }

    @Override // org.jf.dexlib2.iface.instruction.OneRegisterInstruction
    public int getRegisterA() {
        return this.registerA;
    }

    @Override // org.jf.dexlib2.iface.instruction.TwoRegisterInstruction
    public int getRegisterB() {
        return this.registerB;
    }

    @Override // org.jf.dexlib2.builder.BuilderInstruction
    public Format getFormat() {
        return FORMAT;
    }
}
