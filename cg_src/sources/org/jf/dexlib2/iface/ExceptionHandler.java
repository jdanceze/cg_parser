package org.jf.dexlib2.iface;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.reference.TypeReference;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/iface/ExceptionHandler.class */
public interface ExceptionHandler extends Comparable<ExceptionHandler> {
    @Nullable
    String getExceptionType();

    @Nullable
    TypeReference getExceptionTypeReference();

    int getHandlerCodeAddress();

    int hashCode();

    boolean equals(@Nullable Object obj);

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.lang.Comparable
    int compareTo(@Nonnull ExceptionHandler exceptionHandler);
}
