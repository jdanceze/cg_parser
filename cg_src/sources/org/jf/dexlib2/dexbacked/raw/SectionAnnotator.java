package org.jf.dexlib2.dexbacked.raw;

import com.google.common.collect.Maps;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.raw.util.DexAnnotator;
import org.jf.dexlib2.util.AlignmentUtils;
import org.jf.dexlib2.util.AnnotatedBytes;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/raw/SectionAnnotator.class */
public abstract class SectionAnnotator {
    @Nonnull
    public final DexAnnotator annotator;
    @Nonnull
    public final DexBackedDexFile dexFile;
    public final int itemType;
    public final int sectionOffset;
    public final int itemCount;
    protected Map<Integer, String> itemIdentities = Maps.newHashMap();

    @Nonnull
    public abstract String getItemName();

    protected abstract void annotateItem(@Nonnull AnnotatedBytes annotatedBytes, int i, @Nullable String str);

    public SectionAnnotator(@Nonnull DexAnnotator annotator, @Nonnull MapItem mapItem) {
        this.annotator = annotator;
        this.dexFile = annotator.dexFile;
        this.itemType = mapItem.getType();
        if (mapItem.getType() >= 4096) {
            this.sectionOffset = mapItem.getOffset() + this.dexFile.getBaseDataOffset();
        } else {
            this.sectionOffset = mapItem.getOffset();
        }
        this.itemCount = mapItem.getItemCount();
    }

    public void annotateSection(@Nonnull AnnotatedBytes out) {
        out.moveTo(this.sectionOffset);
        annotateSectionInner(out, this.itemCount);
    }

    protected int getItemOffset(int itemIndex, int currentOffset) {
        return AlignmentUtils.alignOffset(currentOffset, getItemAlignment());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void annotateSectionInner(@Nonnull AnnotatedBytes out, int itemCount) {
        String itemName = getItemName();
        if (itemCount > 0) {
            out.annotate(0, "", new Object[0]);
            out.annotate(0, "-----------------------------", new Object[0]);
            out.annotate(0, "%s section", itemName);
            out.annotate(0, "-----------------------------", new Object[0]);
            out.annotate(0, "", new Object[0]);
            for (int i = 0; i < itemCount; i++) {
                out.moveTo(getItemOffset(i, out.getCursor()));
                String itemIdentity = getItemIdentity(out.getCursor());
                if (itemIdentity != null) {
                    out.annotate(0, "[%d] %s: %s", Integer.valueOf(i), itemName, itemIdentity);
                } else {
                    out.annotate(0, "[%d] %s", Integer.valueOf(i), itemName);
                }
                out.indent();
                annotateItem(out, i, itemIdentity);
                out.deindent();
            }
        }
    }

    @Nullable
    private String getItemIdentity(int itemOffset) {
        return this.itemIdentities.get(Integer.valueOf(itemOffset));
    }

    public void setItemIdentity(int itemOffset, String identity) {
        this.itemIdentities.put(Integer.valueOf(itemOffset + this.dexFile.getBaseDataOffset()), identity);
    }

    public int getItemAlignment() {
        return 1;
    }
}
