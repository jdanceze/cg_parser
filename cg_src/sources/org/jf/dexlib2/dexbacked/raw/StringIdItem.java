package org.jf.dexlib2.dexbacked.raw;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.raw.util.DexAnnotator;
import org.jf.dexlib2.util.AnnotatedBytes;
import org.jf.util.StringUtils;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/raw/StringIdItem.class */
public class StringIdItem {
    public static final int ITEM_SIZE = 4;

    @Nonnull
    public static SectionAnnotator makeAnnotator(@Nonnull DexAnnotator annotator, @Nonnull MapItem mapItem) {
        return new SectionAnnotator(annotator, mapItem) { // from class: org.jf.dexlib2.dexbacked.raw.StringIdItem.1
            @Override // org.jf.dexlib2.dexbacked.raw.SectionAnnotator
            @Nonnull
            public String getItemName() {
                return "string_id_item";
            }

            @Override // org.jf.dexlib2.dexbacked.raw.SectionAnnotator
            public void annotateItem(@Nonnull AnnotatedBytes out, int itemIndex, @Nullable String itemIdentity) {
                int stringDataOffset = this.dexFile.getBuffer().readSmallUint(out.getCursor());
                try {
                    String stringValue = (String) this.dexFile.getStringSection().get(itemIndex);
                    out.annotate(4, "string_data_item[0x%x]: \"%s\"", Integer.valueOf(stringDataOffset), StringUtils.escapeString(stringValue));
                } catch (Exception ex) {
                    System.err.print("Error while resolving string value at index: ");
                    System.err.print(itemIndex);
                    ex.printStackTrace(System.err);
                    out.annotate(4, "string_id_item[0x%x]", Integer.valueOf(stringDataOffset));
                }
            }
        };
    }

    @Nonnull
    public static String getReferenceAnnotation(@Nonnull DexBackedDexFile dexFile, int stringIndex) {
        return getReferenceAnnotation(dexFile, stringIndex, false);
    }

    public static String getReferenceAnnotation(@Nonnull DexBackedDexFile dexFile, int stringIndex, boolean quote) {
        try {
            String string = (String) dexFile.getStringSection().get(stringIndex);
            if (quote) {
                string = String.format("\"%s\"", StringUtils.escapeString(string));
            }
            return String.format("string_id_item[%d]: %s", Integer.valueOf(stringIndex), string);
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
            return String.format("string_id_item[%d]", Integer.valueOf(stringIndex));
        }
    }

    @Nonnull
    public static String getOptionalReferenceAnnotation(@Nonnull DexBackedDexFile dexFile, int stringIndex) {
        return getOptionalReferenceAnnotation(dexFile, stringIndex, false);
    }

    public static String getOptionalReferenceAnnotation(@Nonnull DexBackedDexFile dexFile, int stringIndex, boolean quote) {
        if (stringIndex == -1) {
            return "string_id_item[NO_INDEX]";
        }
        return getReferenceAnnotation(dexFile, stringIndex, quote);
    }
}
