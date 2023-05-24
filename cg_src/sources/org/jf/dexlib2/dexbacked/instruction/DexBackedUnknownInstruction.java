package org.jf.dexlib2.dexbacked.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.iface.instruction.formats.UnknownInstruction;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/instruction/DexBackedUnknownInstruction.class */
public class DexBackedUnknownInstruction extends DexBackedInstruction implements UnknownInstruction {
    public DexBackedUnknownInstruction(@Nonnull DexBackedDexFile dexFile, int instructionStart) {
        super(dexFile, Opcode.NOP, instructionStart);
    }

    @Override // org.jf.dexlib2.iface.instruction.formats.UnknownInstruction
    public int getOriginalOpcode() {
        int opcode = this.dexFile.getDataBuffer().readUbyte(this.instructionStart);
        if (opcode == 0) {
            opcode = this.dexFile.getDataBuffer().readUshort(this.instructionStart);
        }
        return opcode;
    }
}
