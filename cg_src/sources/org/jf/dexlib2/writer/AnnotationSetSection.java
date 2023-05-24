package org.jf.dexlib2.writer;

import java.util.Collection;
import javax.annotation.Nonnull;
import org.jf.dexlib2.iface.Annotation;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/AnnotationSetSection.class */
public interface AnnotationSetSection<AnnotationKey extends Annotation, AnnotationSetKey> extends NullableOffsetSection<AnnotationSetKey> {
    @Nonnull
    Collection<? extends AnnotationKey> getAnnotations(@Nonnull AnnotationSetKey annotationsetkey);
}
