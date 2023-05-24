package org.jf.dexlib2.iface.value;

import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.AnnotationElement;
import org.jf.dexlib2.iface.BasicAnnotation;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/iface/value/AnnotationEncodedValue.class */
public interface AnnotationEncodedValue extends EncodedValue, BasicAnnotation {
    @Nonnull
    String getType();

    @Nonnull
    Set<? extends AnnotationElement> getElements();

    int hashCode();

    boolean equals(@Nullable Object obj);

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.lang.Comparable
    int compareTo(@Nonnull EncodedValue encodedValue);
}
