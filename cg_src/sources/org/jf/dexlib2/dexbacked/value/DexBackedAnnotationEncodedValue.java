package org.jf.dexlib2.dexbacked.value;

import java.util.Set;
import javax.annotation.Nonnull;
import org.jf.dexlib2.base.value.BaseAnnotationEncodedValue;
import org.jf.dexlib2.dexbacked.DexBackedAnnotationElement;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.DexReader;
import org.jf.dexlib2.dexbacked.util.VariableSizeSet;
import org.jf.dexlib2.iface.value.AnnotationEncodedValue;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/value/DexBackedAnnotationEncodedValue.class */
public class DexBackedAnnotationEncodedValue extends BaseAnnotationEncodedValue implements AnnotationEncodedValue {
    @Nonnull
    public final DexBackedDexFile dexFile;
    @Nonnull
    public final String type;
    private final int elementCount;
    private final int elementsOffset;

    public DexBackedAnnotationEncodedValue(@Nonnull DexBackedDexFile dexFile, @Nonnull DexReader reader) {
        this.dexFile = dexFile;
        this.type = (String) dexFile.getTypeSection().get(reader.readSmallUleb128());
        this.elementCount = reader.readSmallUleb128();
        this.elementsOffset = reader.getOffset();
        skipElements(reader, this.elementCount);
    }

    public static void skipFrom(@Nonnull DexReader reader) {
        reader.skipUleb128();
        int elementCount = reader.readSmallUleb128();
        skipElements(reader, elementCount);
    }

    private static void skipElements(@Nonnull DexReader reader, int elementCount) {
        for (int i = 0; i < elementCount; i++) {
            reader.skipUleb128();
            DexBackedEncodedValue.skipFrom(reader);
        }
    }

    @Override // org.jf.dexlib2.iface.value.AnnotationEncodedValue, org.jf.dexlib2.iface.BasicAnnotation
    @Nonnull
    public String getType() {
        return this.type;
    }

    @Override // org.jf.dexlib2.iface.value.AnnotationEncodedValue, org.jf.dexlib2.iface.BasicAnnotation
    @Nonnull
    public Set<? extends DexBackedAnnotationElement> getElements() {
        return new VariableSizeSet<DexBackedAnnotationElement>(this.dexFile.getDataBuffer(), this.elementsOffset, this.elementCount) { // from class: org.jf.dexlib2.dexbacked.value.DexBackedAnnotationEncodedValue.1
            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.jf.dexlib2.dexbacked.util.VariableSizeSet
            @Nonnull
            public DexBackedAnnotationElement readNextItem(@Nonnull DexReader dexReader, int index) {
                return new DexBackedAnnotationElement(DexBackedAnnotationEncodedValue.this.dexFile, dexReader);
            }
        };
    }
}
