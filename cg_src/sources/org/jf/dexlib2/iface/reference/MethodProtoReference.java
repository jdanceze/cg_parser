package org.jf.dexlib2.iface.reference;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/iface/reference/MethodProtoReference.class */
public interface MethodProtoReference extends Reference, Comparable<MethodProtoReference> {
    @Nonnull
    List<? extends CharSequence> getParameterTypes();

    @Nonnull
    String getReturnType();

    int hashCode();

    boolean equals(@Nullable Object obj);

    @Override // java.lang.Comparable
    int compareTo(@Nonnull MethodProtoReference methodProtoReference);
}
