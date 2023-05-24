package org.jf.dexlib2.dexbacked;

import javax.annotation.Nonnull;
import org.jf.dexlib2.base.BaseAnnotationElement;
import org.jf.dexlib2.dexbacked.value.DexBackedEncodedValue;
import org.jf.dexlib2.iface.value.EncodedValue;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/DexBackedAnnotationElement.class */
public class DexBackedAnnotationElement extends BaseAnnotationElement {
    @Nonnull
    private final DexBackedDexFile dexFile;
    public final int nameIndex;
    @Nonnull
    public final EncodedValue value;

    public DexBackedAnnotationElement(@Nonnull DexBackedDexFile dexFile, @Nonnull DexReader reader) {
        this.dexFile = dexFile;
        this.nameIndex = reader.readSmallUleb128();
        this.value = DexBackedEncodedValue.readFrom(dexFile, reader);
    }

    @Override // org.jf.dexlib2.iface.AnnotationElement
    @Nonnull
    public String getName() {
        return (String) this.dexFile.getStringSection().get(this.nameIndex);
    }

    @Override // org.jf.dexlib2.iface.AnnotationElement
    @Nonnull
    public EncodedValue getValue() {
        return this.value;
    }
}
