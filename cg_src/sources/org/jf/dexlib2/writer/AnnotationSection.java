package org.jf.dexlib2.writer;

import java.util.Collection;
import javax.annotation.Nonnull;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/AnnotationSection.class */
public interface AnnotationSection<StringKey, TypeKey, AnnotationKey, AnnotationElement, EncodedValue> extends OffsetSection<AnnotationKey> {
    int getVisibility(@Nonnull AnnotationKey annotationkey);

    @Nonnull
    TypeKey getType(@Nonnull AnnotationKey annotationkey);

    @Nonnull
    Collection<? extends AnnotationElement> getElements(@Nonnull AnnotationKey annotationkey);

    @Nonnull
    StringKey getElementName(@Nonnull AnnotationElement annotationelement);

    @Nonnull
    EncodedValue getElementValue(@Nonnull AnnotationElement annotationelement);
}
