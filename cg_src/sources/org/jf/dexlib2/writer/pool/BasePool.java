package org.jf.dexlib2.writer.pool;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.Nonnull;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/pool/BasePool.class */
public class BasePool<Key, Value> implements Markable {
    @Nonnull
    protected final DexPool dexPool;
    @Nonnull
    protected final Map<Key, Value> internedItems = Maps.newLinkedHashMap();
    private int markedItemCount = -1;

    public BasePool(@Nonnull DexPool dexPool) {
        this.dexPool = dexPool;
    }

    @Override // org.jf.dexlib2.writer.pool.Markable
    public void mark() {
        this.markedItemCount = this.internedItems.size();
    }

    @Override // org.jf.dexlib2.writer.pool.Markable
    public void reset() {
        if (this.markedItemCount < 0) {
            throw new IllegalStateException("mark() must be called before calling reset()");
        }
        if (this.markedItemCount == this.internedItems.size()) {
            return;
        }
        Iterator<Key> keys = this.internedItems.keySet().iterator();
        for (int i = 0; i < this.markedItemCount; i++) {
            keys.next();
        }
        while (keys.hasNext()) {
            keys.next();
            keys.remove();
        }
    }

    public int getItemCount() {
        return this.internedItems.size();
    }
}
