package org.jf.dexlib2.iface.value;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.reference.MethodHandleReference;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/iface/value/MethodHandleEncodedValue.class */
public interface MethodHandleEncodedValue extends EncodedValue {
    @Nonnull
    MethodHandleReference getValue();

    int hashCode();

    boolean equals(@Nullable Object obj);

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.lang.Comparable
    int compareTo(@Nonnull EncodedValue encodedValue);
}
