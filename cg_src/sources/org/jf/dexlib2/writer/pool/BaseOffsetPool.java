package org.jf.dexlib2.writer.pool;

import java.util.Collection;
import java.util.Map;
import javax.annotation.Nonnull;
import org.jf.dexlib2.writer.OffsetSection;
import org.jf.util.ExceptionWithContext;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/pool/BaseOffsetPool.class */
public abstract class BaseOffsetPool<Key> extends BasePool<Key, Integer> implements OffsetSection<Key> {
    public BaseOffsetPool(@Nonnull DexPool dexPool) {
        super(dexPool);
    }

    @Override // org.jf.dexlib2.writer.OffsetSection
    @Nonnull
    public Collection<? extends Map.Entry<? extends Key, Integer>> getItems() {
        return this.internedItems.entrySet();
    }

    @Override // org.jf.dexlib2.writer.OffsetSection
    public int getItemOffset(@Nonnull Key key) {
        Integer offset = (Integer) this.internedItems.get(key);
        if (offset == null) {
            throw new ExceptionWithContext("Item not found.: %s", getItemString(key));
        }
        return offset.intValue();
    }

    @Nonnull
    protected String getItemString(@Nonnull Key key) {
        return key.toString();
    }
}
