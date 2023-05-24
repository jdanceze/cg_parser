package org.jf.dexlib2.dexbacked.raw;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.raw.util.DexAnnotator;
import org.jf.dexlib2.util.AnnotatedBytes;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/raw/MapItem.class */
public class MapItem {
    public static final int ITEM_SIZE = 12;
    public static final int TYPE_OFFSET = 0;
    public static final int SIZE_OFFSET = 4;
    public static final int OFFSET_OFFSET = 8;
    private final DexBackedDexFile dexFile;
    private final int offset;

    public MapItem(DexBackedDexFile dexFile, int offset) {
        this.dexFile = dexFile;
        this.offset = offset;
    }

    public int getType() {
        return this.dexFile.getDataBuffer().readUshort(this.offset + 0);
    }

    @Nonnull
    public String getName() {
        return ItemType.getItemTypeName(getType());
    }

    public int getItemCount() {
        return this.dexFile.getDataBuffer().readSmallUint(this.offset + 4);
    }

    public int getOffset() {
        return this.dexFile.getDataBuffer().readSmallUint(this.offset + 8);
    }

    @Nonnull
    public static SectionAnnotator makeAnnotator(@Nonnull DexAnnotator annotator, @Nonnull MapItem mapItem) {
        return new SectionAnnotator(annotator, mapItem) { // from class: org.jf.dexlib2.dexbacked.raw.MapItem.1
            @Override // org.jf.dexlib2.dexbacked.raw.SectionAnnotator
            @Nonnull
            public String getItemName() {
                return "map_item";
            }

            @Override // org.jf.dexlib2.dexbacked.raw.SectionAnnotator
            protected void annotateItem(@Nonnull AnnotatedBytes out, int itemIndex, @Nullable String itemIdentity) {
                int itemType = this.dexFile.getBuffer().readUshort(out.getCursor());
                out.annotate(2, "type = 0x%x: %s", Integer.valueOf(itemType), ItemType.getItemTypeName(itemType));
                out.annotate(2, "unused", new Object[0]);
                int size = this.dexFile.getBuffer().readSmallUint(out.getCursor());
                out.annotate(4, "size = %d", Integer.valueOf(size));
                int offset = this.dexFile.getBuffer().readSmallUint(out.getCursor());
                out.annotate(4, "offset = 0x%x", Integer.valueOf(offset));
            }

            @Override // org.jf.dexlib2.dexbacked.raw.SectionAnnotator
            public void annotateSection(@Nonnull AnnotatedBytes out) {
                out.moveTo(this.sectionOffset);
                int mapItemCount = this.dexFile.getBuffer().readSmallUint(out.getCursor());
                out.annotate(4, "size = %d", Integer.valueOf(mapItemCount));
                super.annotateSectionInner(out, mapItemCount);
            }
        };
    }
}
