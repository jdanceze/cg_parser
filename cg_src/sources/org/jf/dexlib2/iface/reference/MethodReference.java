package org.jf.dexlib2.iface.reference;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/iface/reference/MethodReference.class */
public interface MethodReference extends Reference, Comparable<MethodReference> {
    @Nonnull
    String getDefiningClass();

    @Nonnull
    String getName();

    @Nonnull
    List<? extends CharSequence> getParameterTypes();

    @Nonnull
    String getReturnType();

    int hashCode();

    boolean equals(@Nullable Object obj);

    @Override // java.lang.Comparable
    int compareTo(@Nonnull MethodReference methodReference);
}
