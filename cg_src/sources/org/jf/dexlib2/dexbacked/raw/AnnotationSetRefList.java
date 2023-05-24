package org.jf.dexlib2.dexbacked.raw;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.raw.util.DexAnnotator;
import org.jf.dexlib2.util.AnnotatedBytes;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/raw/AnnotationSetRefList.class */
public class AnnotationSetRefList {
    public static final int SIZE_OFFSET = 0;
    public static final int LIST_OFFSET = 4;

    @Nonnull
    public static SectionAnnotator makeAnnotator(@Nonnull DexAnnotator annotator, @Nonnull MapItem mapItem) {
        return new SectionAnnotator(annotator, mapItem) { // from class: org.jf.dexlib2.dexbacked.raw.AnnotationSetRefList.1
            @Override // org.jf.dexlib2.dexbacked.raw.SectionAnnotator
            @Nonnull
            public String getItemName() {
                return "annotation_set_ref_list";
            }

            @Override // org.jf.dexlib2.dexbacked.raw.SectionAnnotator
            protected void annotateItem(@Nonnull AnnotatedBytes out, int itemIndex, @Nullable String itemIdentity) {
                int size = this.dexFile.getBuffer().readSmallUint(out.getCursor());
                out.annotate(4, "size = %d", Integer.valueOf(size));
                for (int i = 0; i < size; i++) {
                    int annotationSetOffset = this.dexFile.getBuffer().readSmallUint(out.getCursor());
                    out.annotate(4, "annotation_set_item[0x%x]", Integer.valueOf(annotationSetOffset));
                }
            }

            @Override // org.jf.dexlib2.dexbacked.raw.SectionAnnotator
            public int getItemAlignment() {
                return 4;
            }
        };
    }

    @Nonnull
    public static String getReferenceAnnotation(@Nonnull DexBackedDexFile dexFile, int annotationSetRefListOffset) {
        if (annotationSetRefListOffset == 0) {
            return "annotation_set_ref_list[NO_OFFSET]";
        }
        return String.format("annotation_set_ref_list[0x%x]", Integer.valueOf(annotationSetRefListOffset));
    }
}
