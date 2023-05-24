package org.jf.dexlib2.iface.reference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/iface/reference/FieldReference.class */
public interface FieldReference extends Reference, Comparable<FieldReference> {
    @Nonnull
    String getDefiningClass();

    @Nonnull
    String getName();

    @Nonnull
    String getType();

    int hashCode();

    boolean equals(@Nullable Object obj);

    @Override // java.lang.Comparable
    int compareTo(@Nonnull FieldReference fieldReference);
}
