package org.jf.dexlib2.iface.reference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/iface/reference/TypeReference.class */
public interface TypeReference extends Reference, CharSequence, Comparable<CharSequence> {
    @Nonnull
    String getType();

    int hashCode();

    boolean equals(@Nullable Object obj);

    @Override // java.lang.Comparable
    int compareTo(@Nonnull CharSequence charSequence);
}
