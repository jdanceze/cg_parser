package org.jf.dexlib2.iface;

import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/iface/Annotation.class */
public interface Annotation extends BasicAnnotation, Comparable<Annotation> {
    int getVisibility();

    @Override // org.jf.dexlib2.iface.BasicAnnotation
    @Nonnull
    String getType();

    @Override // org.jf.dexlib2.iface.BasicAnnotation
    @Nonnull
    Set<? extends AnnotationElement> getElements();

    int hashCode();

    boolean equals(@Nullable Object obj);

    @Override // java.lang.Comparable
    int compareTo(Annotation annotation);
}
