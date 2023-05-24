package org.jf.dexlib2.writer;

import javax.annotation.Nullable;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/NullableOffsetSection.class */
public interface NullableOffsetSection<Key> extends OffsetSection<Key> {
    int getNullableItemOffset(@Nullable Key key);
}
