package org.jf.dexlib2.writer.pool;

import java.util.Collection;
import javax.annotation.Nonnull;
import org.jf.dexlib2.iface.Annotation;
import org.jf.dexlib2.iface.AnnotationElement;
import org.jf.dexlib2.iface.value.EncodedValue;
import org.jf.dexlib2.writer.AnnotationSection;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/pool/AnnotationPool.class */
public class AnnotationPool extends BaseOffsetPool<Annotation> implements AnnotationSection<CharSequence, CharSequence, Annotation, AnnotationElement, EncodedValue> {
    public AnnotationPool(@Nonnull DexPool dexPool) {
        super(dexPool);
    }

    public void intern(@Nonnull Annotation annotation) {
        Integer prev = (Integer) this.internedItems.put(annotation, 0);
        if (prev == null) {
            ((TypePool) this.dexPool.typeSection).intern(annotation.getType());
            for (AnnotationElement element : annotation.getElements()) {
                ((StringPool) this.dexPool.stringSection).intern(element.getName());
                this.dexPool.internEncodedValue(element.getValue());
            }
        }
    }

    @Override // org.jf.dexlib2.writer.AnnotationSection
    public int getVisibility(@Nonnull Annotation annotation) {
        return annotation.getVisibility();
    }

    @Override // org.jf.dexlib2.writer.AnnotationSection
    @Nonnull
    public CharSequence getType(@Nonnull Annotation annotation) {
        return annotation.getType();
    }

    @Override // org.jf.dexlib2.writer.AnnotationSection
    @Nonnull
    public Collection<? extends AnnotationElement> getElements(@Nonnull Annotation annotation) {
        return annotation.getElements();
    }

    @Override // org.jf.dexlib2.writer.AnnotationSection
    @Nonnull
    public CharSequence getElementName(@Nonnull AnnotationElement annotationElement) {
        return annotationElement.getName();
    }

    @Override // org.jf.dexlib2.writer.AnnotationSection
    @Nonnull
    public EncodedValue getElementValue(@Nonnull AnnotationElement annotationElement) {
        return annotationElement.getValue();
    }
}
