package org.jf.dexlib2.iface;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.value.EncodedValue;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/iface/AnnotationElement.class */
public interface AnnotationElement extends Comparable<AnnotationElement> {
    @Nonnull
    String getName();

    @Nonnull
    EncodedValue getValue();

    int hashCode();

    boolean equals(@Nullable Object obj);

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.lang.Comparable
    int compareTo(AnnotationElement annotationElement);
}
