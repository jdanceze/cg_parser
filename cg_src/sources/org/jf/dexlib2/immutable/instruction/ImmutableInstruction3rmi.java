package org.jf.dexlib2.immutable.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.formats.Instruction3rmi;
import org.jf.dexlib2.util.Preconditions;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/immutable/instruction/ImmutableInstruction3rmi.class */
public class ImmutableInstruction3rmi extends ImmutableInstruction implements Instruction3rmi {
    public static final Format FORMAT = Format.Format3rmi;
    protected final int startRegister;
    protected final int registerCount;
    protected final int inlineIndex;

    public ImmutableInstruction3rmi(@Nonnull Opcode opcode, int startRegister, int registerCount, int inlineIndex) {
        super(opcode);
        this.startRegister = Preconditions.checkShortRegister(startRegister);
        this.registerCount = Preconditions.checkRegisterRangeCount(registerCount);
        this.inlineIndex = Preconditions.checkInlineIndex(inlineIndex);
    }

    public static ImmutableInstruction3rmi of(Instruction3rmi instruction) {
        if (instruction instanceof ImmutableInstruction3rmi) {
            return (ImmutableInstruction3rmi) instruction;
        }
        return new ImmutableInstruction3rmi(instruction.getOpcode(), instruction.getStartRegister(), instruction.getRegisterCount(), instruction.getInlineIndex());
    }

    @Override // org.jf.dexlib2.iface.instruction.RegisterRangeInstruction
    public int getStartRegister() {
        return this.startRegister;
    }

    @Override // org.jf.dexlib2.iface.instruction.VariableRegisterInstruction
    public int getRegisterCount() {
        return this.registerCount;
    }

    @Override // org.jf.dexlib2.iface.instruction.InlineIndexInstruction
    public int getInlineIndex() {
        return this.inlineIndex;
    }

    @Override // org.jf.dexlib2.immutable.instruction.ImmutableInstruction
    public Format getFormat() {
        return FORMAT;
    }
}
