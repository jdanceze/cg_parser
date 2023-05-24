package org.jf.dexlib2.dexbacked.raw;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.raw.util.DexAnnotator;
import org.jf.dexlib2.util.AnnotatedBytes;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/raw/TypeListItem.class */
public class TypeListItem {
    public static final int SIZE_OFFSET = 0;
    public static final int LIST_OFFSET = 4;

    @Nonnull
    public static SectionAnnotator makeAnnotator(@Nonnull DexAnnotator annotator, @Nonnull MapItem mapItem) {
        return new SectionAnnotator(annotator, mapItem) { // from class: org.jf.dexlib2.dexbacked.raw.TypeListItem.1
            @Override // org.jf.dexlib2.dexbacked.raw.SectionAnnotator
            @Nonnull
            public String getItemName() {
                return "type_list";
            }

            @Override // org.jf.dexlib2.dexbacked.raw.SectionAnnotator
            protected void annotateItem(@Nonnull AnnotatedBytes out, int itemIndex, @Nullable String itemIdentity) {
                int size = this.dexFile.getBuffer().readSmallUint(out.getCursor());
                out.annotate(4, "size: %d", Integer.valueOf(size));
                for (int i = 0; i < size; i++) {
                    int typeIndex = this.dexFile.getBuffer().readUshort(out.getCursor());
                    out.annotate(2, TypeIdItem.getReferenceAnnotation(this.dexFile, typeIndex), new Object[0]);
                }
            }

            @Override // org.jf.dexlib2.dexbacked.raw.SectionAnnotator
            public int getItemAlignment() {
                return 4;
            }
        };
    }

    @Nonnull
    public static String getReferenceAnnotation(@Nonnull DexBackedDexFile dexFile, int typeListOffset) {
        if (typeListOffset == 0) {
            return "type_list_item[NO_OFFSET]";
        }
        try {
            String typeList = asString(dexFile, typeListOffset);
            return String.format("type_list_item[0x%x]: %s", Integer.valueOf(typeListOffset), typeList);
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
            return String.format("type_list_item[0x%x]", Integer.valueOf(typeListOffset));
        }
    }

    @Nonnull
    public static String asString(@Nonnull DexBackedDexFile dexFile, int typeListOffset) {
        if (typeListOffset == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int size = dexFile.getDataBuffer().readSmallUint(typeListOffset);
        for (int i = 0; i < size; i++) {
            int typeIndex = dexFile.getDataBuffer().readUshort(typeListOffset + 4 + (i * 2));
            String type = (String) dexFile.getTypeSection().get(typeIndex);
            sb.append(type);
        }
        return sb.toString();
    }
}
