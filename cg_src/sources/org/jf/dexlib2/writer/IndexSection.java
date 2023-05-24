package org.jf.dexlib2.writer;

import java.util.Collection;
import java.util.Map;
import javax.annotation.Nonnull;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/IndexSection.class */
public interface IndexSection<Key> {
    int getItemIndex(@Nonnull Key key);

    @Nonnull
    Collection<? extends Map.Entry<? extends Key, Integer>> getItems();

    int getItemCount();
}
