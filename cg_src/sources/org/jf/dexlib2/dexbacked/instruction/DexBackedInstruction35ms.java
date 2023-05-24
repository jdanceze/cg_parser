package org.jf.dexlib2.dexbacked.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.iface.instruction.formats.Instruction35ms;
import org.jf.util.NibbleUtils;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/instruction/DexBackedInstruction35ms.class */
public class DexBackedInstruction35ms extends DexBackedInstruction implements Instruction35ms {
    public DexBackedInstruction35ms(@Nonnull DexBackedDexFile dexFile, @Nonnull Opcode opcode, int instructionStart) {
        super(dexFile, opcode, instructionStart);
    }

    @Override // org.jf.dexlib2.iface.instruction.VariableRegisterInstruction
    public int getRegisterCount() {
        return NibbleUtils.extractHighUnsignedNibble(this.dexFile.getDataBuffer().readUbyte(this.instructionStart + 1));
    }

    @Override // org.jf.dexlib2.iface.instruction.FiveRegisterInstruction
    public int getRegisterC() {
        return NibbleUtils.extractLowUnsignedNibble(this.dexFile.getDataBuffer().readUbyte(this.instructionStart + 4));
    }

    @Override // org.jf.dexlib2.iface.instruction.FiveRegisterInstruction
    public int getRegisterD() {
        return NibbleUtils.extractHighUnsignedNibble(this.dexFile.getDataBuffer().readUbyte(this.instructionStart + 4));
    }

    @Override // org.jf.dexlib2.iface.instruction.FiveRegisterInstruction
    public int getRegisterE() {
        return NibbleUtils.extractLowUnsignedNibble(this.dexFile.getDataBuffer().readUbyte(this.instructionStart + 5));
    }

    @Override // org.jf.dexlib2.iface.instruction.FiveRegisterInstruction
    public int getRegisterF() {
        return NibbleUtils.extractHighUnsignedNibble(this.dexFile.getDataBuffer().readUbyte(this.instructionStart + 5));
    }

    @Override // org.jf.dexlib2.iface.instruction.FiveRegisterInstruction
    public int getRegisterG() {
        return NibbleUtils.extractLowUnsignedNibble(this.dexFile.getDataBuffer().readUbyte(this.instructionStart + 1));
    }

    @Override // org.jf.dexlib2.iface.instruction.VtableIndexInstruction
    public int getVtableIndex() {
        return this.dexFile.getDataBuffer().readUshort(this.instructionStart + 2);
    }
}
