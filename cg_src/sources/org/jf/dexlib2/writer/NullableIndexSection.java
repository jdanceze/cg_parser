package org.jf.dexlib2.writer;

import javax.annotation.Nullable;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/NullableIndexSection.class */
public interface NullableIndexSection<Key> extends IndexSection<Key> {
    int getNullableItemIndex(@Nullable Key key);
}
