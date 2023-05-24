package org.jf.dexlib2.writer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/ProtoSection.class */
public interface ProtoSection<StringKey, TypeKey, ProtoKey, TypeListKey> extends IndexSection<ProtoKey> {
    @Nonnull
    StringKey getShorty(@Nonnull ProtoKey protokey);

    @Nonnull
    TypeKey getReturnType(@Nonnull ProtoKey protokey);

    @Nullable
    TypeListKey getParameters(@Nonnull ProtoKey protokey);
}
