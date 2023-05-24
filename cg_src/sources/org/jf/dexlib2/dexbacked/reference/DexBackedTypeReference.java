package org.jf.dexlib2.dexbacked.reference;

import javax.annotation.Nonnull;
import org.jf.dexlib2.base.reference.BaseTypeReference;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.iface.reference.Reference;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/reference/DexBackedTypeReference.class */
public class DexBackedTypeReference extends BaseTypeReference {
    @Nonnull
    public final DexBackedDexFile dexFile;
    public final int typeIndex;

    public DexBackedTypeReference(@Nonnull DexBackedDexFile dexFile, int typeIndex) {
        this.dexFile = dexFile;
        this.typeIndex = typeIndex;
    }

    @Override // org.jf.dexlib2.iface.reference.TypeReference, org.jf.dexlib2.iface.ClassDef
    @Nonnull
    public String getType() {
        return (String) this.dexFile.getTypeSection().get(this.typeIndex);
    }

    public int getSize() {
        return 4;
    }

    @Override // org.jf.dexlib2.base.reference.BaseReference, org.jf.dexlib2.iface.reference.Reference
    public void validateReference() throws Reference.InvalidReferenceException {
        if (this.typeIndex < 0 || this.typeIndex >= this.dexFile.getTypeSection().size()) {
            throw new Reference.InvalidReferenceException("type@" + this.typeIndex);
        }
    }
}
