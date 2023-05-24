package org.jf.dexlib2.writer;

import java.util.Collection;
import java.util.Map;
import javax.annotation.Nonnull;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/OffsetSection.class */
public interface OffsetSection<Key> {
    int getItemOffset(@Nonnull Key key);

    @Nonnull
    Collection<? extends Map.Entry<? extends Key, Integer>> getItems();
}
