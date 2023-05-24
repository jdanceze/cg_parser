package org.jf.dexlib2.writer.pool;

import java.util.Collection;
import java.util.Set;
import javax.annotation.Nonnull;
import org.jf.dexlib2.iface.Annotation;
import org.jf.dexlib2.writer.AnnotationSetSection;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/pool/AnnotationSetPool.class */
public class AnnotationSetPool extends BaseNullableOffsetPool<Set<? extends Annotation>> implements AnnotationSetSection<Annotation, Set<? extends Annotation>> {
    public AnnotationSetPool(@Nonnull DexPool dexPool) {
        super(dexPool);
    }

    public void intern(@Nonnull Set<? extends Annotation> annotationSet) {
        if (annotationSet.size() > 0) {
            Integer prev = (Integer) this.internedItems.put(annotationSet, 0);
            if (prev == null) {
                for (Annotation annotation : annotationSet) {
                    ((AnnotationPool) this.dexPool.annotationSection).intern(annotation);
                }
            }
        }
    }

    @Override // org.jf.dexlib2.writer.AnnotationSetSection
    @Nonnull
    public Collection<? extends Annotation> getAnnotations(@Nonnull Set<? extends Annotation> annotations) {
        return annotations;
    }
}
