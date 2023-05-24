package org.jf.dexlib2.dexbacked.reference;

import javax.annotation.Nonnull;
import org.jf.dexlib2.base.reference.BaseStringReference;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.DexReader;
import org.jf.dexlib2.iface.reference.Reference;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/reference/DexBackedStringReference.class */
public class DexBackedStringReference extends BaseStringReference {
    @Nonnull
    public final DexBackedDexFile dexFile;
    public final int stringIndex;

    public DexBackedStringReference(@Nonnull DexBackedDexFile dexBuf, int stringIndex) {
        this.dexFile = dexBuf;
        this.stringIndex = stringIndex;
    }

    @Override // org.jf.dexlib2.iface.reference.StringReference
    @Nonnull
    public String getString() {
        return (String) this.dexFile.getStringSection().get(this.stringIndex);
    }

    public int getSize() {
        int stringOffset = this.dexFile.getStringSection().getOffset(this.stringIndex);
        int stringDataOffset = this.dexFile.getBuffer().readSmallUint(stringOffset);
        DexReader reader = this.dexFile.getDataBuffer().readerAt(stringDataOffset);
        int size = 4 + reader.peekSmallUleb128Size();
        int utf16Length = reader.readSmallUleb128();
        return size + reader.peekStringLength(utf16Length);
    }

    @Override // org.jf.dexlib2.base.reference.BaseReference, org.jf.dexlib2.iface.reference.Reference
    public void validateReference() throws Reference.InvalidReferenceException {
        if (this.stringIndex < 0 || this.stringIndex >= this.dexFile.getStringSection().size()) {
            throw new Reference.InvalidReferenceException("string@" + this.stringIndex);
        }
    }
}
