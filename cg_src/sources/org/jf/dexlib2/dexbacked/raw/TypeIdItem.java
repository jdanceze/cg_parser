package org.jf.dexlib2.dexbacked.raw;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.raw.util.DexAnnotator;
import org.jf.dexlib2.util.AnnotatedBytes;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/raw/TypeIdItem.class */
public class TypeIdItem {
    public static final int ITEM_SIZE = 4;

    @Nonnull
    public static SectionAnnotator makeAnnotator(@Nonnull DexAnnotator annotator, @Nonnull MapItem mapItem) {
        return new SectionAnnotator(annotator, mapItem) { // from class: org.jf.dexlib2.dexbacked.raw.TypeIdItem.1
            @Override // org.jf.dexlib2.dexbacked.raw.SectionAnnotator
            @Nonnull
            public String getItemName() {
                return "type_id_item";
            }

            @Override // org.jf.dexlib2.dexbacked.raw.SectionAnnotator
            protected void annotateItem(@Nonnull AnnotatedBytes out, int itemIndex, @Nullable String itemIdentity) {
                int stringIndex = this.dexFile.getBuffer().readSmallUint(out.getCursor());
                out.annotate(4, StringIdItem.getReferenceAnnotation(this.dexFile, stringIndex), new Object[0]);
            }
        };
    }

    @Nonnull
    public static String getReferenceAnnotation(@Nonnull DexBackedDexFile dexFile, int typeIndex) {
        try {
            String typeString = (String) dexFile.getTypeSection().get(typeIndex);
            return String.format("type_id_item[%d]: %s", Integer.valueOf(typeIndex), typeString);
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
            return String.format("type_id_item[%d]", Integer.valueOf(typeIndex));
        }
    }

    @Nonnull
    public static String getOptionalReferenceAnnotation(@Nonnull DexBackedDexFile dexFile, int typeIndex) {
        if (typeIndex == -1) {
            return "type_id_item[NO_INDEX]";
        }
        return getReferenceAnnotation(dexFile, typeIndex);
    }
}
