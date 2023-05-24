package org.jf.dexlib2.writer;

import java.util.Collection;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/TypeListSection.class */
public interface TypeListSection<TypeKey, TypeListKey> extends NullableOffsetSection<TypeListKey> {
    @Override // org.jf.dexlib2.writer.NullableOffsetSection
    int getNullableItemOffset(@Nullable TypeListKey typelistkey);

    @Nonnull
    Collection<? extends TypeKey> getTypes(@Nullable TypeListKey typelistkey);
}
