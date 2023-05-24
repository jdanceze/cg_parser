package org.jf.dexlib2.iface.reference;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.value.EncodedValue;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/iface/reference/CallSiteReference.class */
public interface CallSiteReference extends Reference {
    @Nonnull
    String getName();

    @Nonnull
    MethodHandleReference getMethodHandle();

    @Nonnull
    String getMethodName();

    @Nonnull
    MethodProtoReference getMethodProto();

    @Nonnull
    List<? extends EncodedValue> getExtraArguments();

    int hashCode();

    boolean equals(@Nullable Object obj);
}
