package org.jf.dexlib2.writer.pool;

import java.util.Collection;
import java.util.Map;
import javax.annotation.Nonnull;
import org.jf.dexlib2.writer.IndexSection;
import org.jf.util.ExceptionWithContext;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/pool/BaseIndexPool.class */
public abstract class BaseIndexPool<Key> extends BasePool<Key, Integer> implements IndexSection<Key> {
    public BaseIndexPool(@Nonnull DexPool dexPool) {
        super(dexPool);
    }

    @Override // org.jf.dexlib2.writer.IndexSection
    @Nonnull
    public Collection<? extends Map.Entry<? extends Key, Integer>> getItems() {
        return this.internedItems.entrySet();
    }

    @Override // org.jf.dexlib2.writer.IndexSection
    public int getItemIndex(@Nonnull Key key) {
        Integer index = (Integer) this.internedItems.get(key);
        if (index == null) {
            throw new ExceptionWithContext("Item not found.: %s", getItemString(key));
        }
        return index.intValue();
    }

    @Nonnull
    protected String getItemString(@Nonnull Key key) {
        return key.toString();
    }
}
