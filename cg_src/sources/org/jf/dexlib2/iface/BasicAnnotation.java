package org.jf.dexlib2.iface;

import java.util.Set;
import javax.annotation.Nonnull;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/iface/BasicAnnotation.class */
public interface BasicAnnotation {
    @Nonnull
    String getType();

    @Nonnull
    Set<? extends AnnotationElement> getElements();
}
