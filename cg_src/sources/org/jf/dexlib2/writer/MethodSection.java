package org.jf.dexlib2.writer;

import javax.annotation.Nonnull;
import org.jf.dexlib2.iface.reference.MethodProtoReference;
import org.jf.dexlib2.iface.reference.MethodReference;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/MethodSection.class */
public interface MethodSection<StringKey, TypeKey, ProtoRefKey extends MethodProtoReference, MethodRefKey extends MethodReference, MethodKey> extends IndexSection<MethodRefKey> {
    @Nonnull
    MethodRefKey getMethodReference(@Nonnull MethodKey methodkey);

    @Nonnull
    TypeKey getDefiningClass(@Nonnull MethodRefKey methodrefkey);

    @Nonnull
    ProtoRefKey getPrototype(@Nonnull MethodRefKey methodrefkey);

    @Nonnull
    ProtoRefKey getPrototype(@Nonnull MethodKey methodkey);

    @Nonnull
    StringKey getName(@Nonnull MethodRefKey methodrefkey);

    int getMethodIndex(@Nonnull MethodKey methodkey);
}
