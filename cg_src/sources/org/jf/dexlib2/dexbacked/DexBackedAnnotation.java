package org.jf.dexlib2.dexbacked;

import java.util.Set;
import javax.annotation.Nonnull;
import org.jf.dexlib2.base.BaseAnnotation;
import org.jf.dexlib2.dexbacked.util.VariableSizeSet;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/DexBackedAnnotation.class */
public class DexBackedAnnotation extends BaseAnnotation {
    @Nonnull
    public final DexBackedDexFile dexFile;
    public final int visibility;
    public final int typeIndex;
    private final int elementsOffset;

    public DexBackedAnnotation(@Nonnull DexBackedDexFile dexFile, int annotationOffset) {
        this.dexFile = dexFile;
        DexReader reader = dexFile.getDataBuffer().readerAt(annotationOffset);
        this.visibility = reader.readUbyte();
        this.typeIndex = reader.readSmallUleb128();
        this.elementsOffset = reader.getOffset();
    }

    @Override // org.jf.dexlib2.iface.Annotation
    public int getVisibility() {
        return this.visibility;
    }

    @Override // org.jf.dexlib2.iface.Annotation, org.jf.dexlib2.iface.BasicAnnotation
    @Nonnull
    public String getType() {
        return (String) this.dexFile.getTypeSection().get(this.typeIndex);
    }

    @Override // org.jf.dexlib2.iface.Annotation, org.jf.dexlib2.iface.BasicAnnotation
    @Nonnull
    public Set<? extends DexBackedAnnotationElement> getElements() {
        DexReader reader = this.dexFile.getDataBuffer().readerAt(this.elementsOffset);
        int size = reader.readSmallUleb128();
        return new VariableSizeSet<DexBackedAnnotationElement>(this.dexFile.getDataBuffer(), reader.getOffset(), size) { // from class: org.jf.dexlib2.dexbacked.DexBackedAnnotation.1
            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.jf.dexlib2.dexbacked.util.VariableSizeSet
            @Nonnull
            public DexBackedAnnotationElement readNextItem(@Nonnull DexReader reader2, int index) {
                return new DexBackedAnnotationElement(DexBackedAnnotation.this.dexFile, reader2);
            }
        };
    }
}
