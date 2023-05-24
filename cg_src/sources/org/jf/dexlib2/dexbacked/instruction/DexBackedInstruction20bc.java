package org.jf.dexlib2.dexbacked.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.ReferenceType;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.reference.DexBackedReference;
import org.jf.dexlib2.iface.instruction.formats.Instruction20bc;
import org.jf.dexlib2.iface.reference.Reference;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/instruction/DexBackedInstruction20bc.class */
public class DexBackedInstruction20bc extends DexBackedInstruction implements Instruction20bc {
    public DexBackedInstruction20bc(@Nonnull DexBackedDexFile dexFile, @Nonnull Opcode opcode, int instructionStart) {
        super(dexFile, opcode, instructionStart);
    }

    @Override // org.jf.dexlib2.iface.instruction.VerificationErrorInstruction
    public int getVerificationError() {
        return this.dexFile.getDataBuffer().readUbyte(this.instructionStart + 1) & 63;
    }

    @Override // org.jf.dexlib2.iface.instruction.ReferenceInstruction
    @Nonnull
    public Reference getReference() {
        final int referenceIndex = this.dexFile.getDataBuffer().readUshort(this.instructionStart + 2);
        try {
            int referenceType = getReferenceType();
            return DexBackedReference.makeReference(this.dexFile, referenceType, referenceIndex);
        } catch (ReferenceType.InvalidReferenceTypeException ex) {
            return new Reference() { // from class: org.jf.dexlib2.dexbacked.instruction.DexBackedInstruction20bc.1
                @Override // org.jf.dexlib2.iface.reference.Reference
                public void validateReference() throws Reference.InvalidReferenceException {
                    throw new Reference.InvalidReferenceException(String.format("%d@%d", Integer.valueOf(ex.getReferenceType()), Integer.valueOf(referenceIndex)), ex);
                }
            };
        }
    }

    @Override // org.jf.dexlib2.iface.instruction.ReferenceInstruction
    public int getReferenceType() {
        int referenceType = (this.dexFile.getDataBuffer().readUbyte(this.instructionStart + 1) >>> 6) + 1;
        ReferenceType.validateReferenceType(referenceType);
        return referenceType;
    }
}
