package org.jf.dexlib2.writer.pool;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.writer.NullableOffsetSection;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/pool/BaseNullableOffsetPool.class */
public abstract class BaseNullableOffsetPool<Key> extends BaseOffsetPool<Key> implements NullableOffsetSection<Key> {
    public BaseNullableOffsetPool(@Nonnull DexPool dexPool) {
        super(dexPool);
    }

    @Override // org.jf.dexlib2.writer.NullableOffsetSection
    public int getNullableItemOffset(@Nullable Key key) {
        if (key == null) {
            return 0;
        }
        return getItemOffset(key);
    }
}
