package org.jf.dexlib2.immutable.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.formats.Instruction3rms;
import org.jf.dexlib2.util.Preconditions;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/immutable/instruction/ImmutableInstruction3rms.class */
public class ImmutableInstruction3rms extends ImmutableInstruction implements Instruction3rms {
    public static final Format FORMAT = Format.Format3rms;
    protected final int startRegister;
    protected final int registerCount;
    protected final int vtableIndex;

    public ImmutableInstruction3rms(@Nonnull Opcode opcode, int startRegister, int registerCount, int vtableIndex) {
        super(opcode);
        this.startRegister = Preconditions.checkShortRegister(startRegister);
        this.registerCount = Preconditions.checkRegisterRangeCount(registerCount);
        this.vtableIndex = Preconditions.checkVtableIndex(vtableIndex);
    }

    public static ImmutableInstruction3rms of(Instruction3rms instruction) {
        if (instruction instanceof ImmutableInstruction3rms) {
            return (ImmutableInstruction3rms) instruction;
        }
        return new ImmutableInstruction3rms(instruction.getOpcode(), instruction.getStartRegister(), instruction.getRegisterCount(), instruction.getVtableIndex());
    }

    @Override // org.jf.dexlib2.iface.instruction.RegisterRangeInstruction
    public int getStartRegister() {
        return this.startRegister;
    }

    @Override // org.jf.dexlib2.iface.instruction.VariableRegisterInstruction
    public int getRegisterCount() {
        return this.registerCount;
    }

    @Override // org.jf.dexlib2.iface.instruction.VtableIndexInstruction
    public int getVtableIndex() {
        return this.vtableIndex;
    }

    @Override // org.jf.dexlib2.immutable.instruction.ImmutableInstruction
    public Format getFormat() {
        return FORMAT;
    }
}
