package org.jf.dexlib2.dexbacked.reference;

import javax.annotation.Nonnull;
import org.jf.dexlib2.base.reference.BaseMethodHandleReference;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.iface.reference.Reference;
import org.jf.util.ExceptionWithContext;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/reference/DexBackedMethodHandleReference.class */
public class DexBackedMethodHandleReference extends BaseMethodHandleReference {
    @Nonnull
    public final DexBackedDexFile dexFile;
    public final int methodHandleIndex;
    public final int methodHandleOffset;

    public DexBackedMethodHandleReference(DexBackedDexFile dexFile, int methodHandleIndex) {
        this.dexFile = dexFile;
        this.methodHandleIndex = methodHandleIndex;
        this.methodHandleOffset = dexFile.getMethodHandleSection().getOffset(methodHandleIndex);
    }

    @Override // org.jf.dexlib2.iface.reference.MethodHandleReference
    public int getMethodHandleType() {
        return this.dexFile.getBuffer().readUshort(this.methodHandleOffset + 0);
    }

    @Override // org.jf.dexlib2.iface.reference.MethodHandleReference
    @Nonnull
    public Reference getMemberReference() {
        int memberIndex = this.dexFile.getBuffer().readUshort(this.methodHandleOffset + 4);
        switch (getMethodHandleType()) {
            case 0:
            case 1:
            case 2:
            case 3:
                return new DexBackedFieldReference(this.dexFile, memberIndex);
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                return new DexBackedMethodReference(this.dexFile, memberIndex);
            default:
                throw new ExceptionWithContext("Invalid method handle type: %d", Integer.valueOf(getMethodHandleType()));
        }
    }

    @Override // org.jf.dexlib2.base.reference.BaseReference, org.jf.dexlib2.iface.reference.Reference
    public void validateReference() throws Reference.InvalidReferenceException {
        if (this.methodHandleIndex < 0 || this.methodHandleIndex >= this.dexFile.getMethodHandleSection().size()) {
            throw new Reference.InvalidReferenceException("methodhandle@" + this.methodHandleIndex);
        }
        try {
            getMemberReference();
        } catch (ExceptionWithContext ex) {
            throw new Reference.InvalidReferenceException("methodhandle@" + this.methodHandleIndex, ex);
        }
    }
}
