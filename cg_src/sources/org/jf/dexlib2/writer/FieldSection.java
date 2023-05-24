package org.jf.dexlib2.writer;

import javax.annotation.Nonnull;
import org.jf.dexlib2.iface.reference.FieldReference;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/FieldSection.class */
public interface FieldSection<StringKey, TypeKey, FieldRefKey extends FieldReference, FieldKey> extends IndexSection<FieldRefKey> {
    @Nonnull
    TypeKey getDefiningClass(@Nonnull FieldRefKey fieldrefkey);

    @Nonnull
    TypeKey getFieldType(@Nonnull FieldRefKey fieldrefkey);

    @Nonnull
    StringKey getName(@Nonnull FieldRefKey fieldrefkey);

    int getFieldIndex(@Nonnull FieldKey fieldkey);
}
