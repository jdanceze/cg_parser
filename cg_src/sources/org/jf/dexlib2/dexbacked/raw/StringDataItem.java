package org.jf.dexlib2.dexbacked.raw;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.dexbacked.DexReader;
import org.jf.dexlib2.dexbacked.raw.util.DexAnnotator;
import org.jf.dexlib2.util.AnnotatedBytes;
import org.jf.util.StringUtils;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/raw/StringDataItem.class */
public class StringDataItem {
    @Nonnull
    public static SectionAnnotator makeAnnotator(@Nonnull DexAnnotator annotator, @Nonnull MapItem mapItem) {
        return new SectionAnnotator(annotator, mapItem) { // from class: org.jf.dexlib2.dexbacked.raw.StringDataItem.1
            @Override // org.jf.dexlib2.dexbacked.raw.SectionAnnotator
            @Nonnull
            public String getItemName() {
                return "string_data_item";
            }

            @Override // org.jf.dexlib2.dexbacked.raw.SectionAnnotator
            protected void annotateItem(@Nonnull AnnotatedBytes out, int itemIndex, @Nullable String itemIdentity) {
                DexReader reader = this.dexFile.getBuffer().readerAt(out.getCursor());
                int utf16Length = reader.readSmallUleb128();
                out.annotateTo(reader.getOffset(), "utf16_size = %d", Integer.valueOf(utf16Length));
                String value = reader.readString(utf16Length);
                out.annotateTo(reader.getOffset() + 1, "data = \"%s\"", StringUtils.escapeString(value));
            }
        };
    }
}
