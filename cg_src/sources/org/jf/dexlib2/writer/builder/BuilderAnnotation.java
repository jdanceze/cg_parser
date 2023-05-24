package org.jf.dexlib2.writer.builder;

import java.util.Set;
import javax.annotation.Nonnull;
import org.jf.dexlib2.base.BaseAnnotation;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/builder/BuilderAnnotation.class */
class BuilderAnnotation extends BaseAnnotation {
    int visibility;
    @Nonnull
    final BuilderTypeReference type;
    @Nonnull
    final Set<? extends BuilderAnnotationElement> elements;
    int offset = 0;

    public BuilderAnnotation(int visibility, @Nonnull BuilderTypeReference type, @Nonnull Set<? extends BuilderAnnotationElement> elements) {
        this.visibility = visibility;
        this.type = type;
        this.elements = elements;
    }

    @Override // org.jf.dexlib2.iface.Annotation
    public int getVisibility() {
        return this.visibility;
    }

    @Override // org.jf.dexlib2.iface.Annotation, org.jf.dexlib2.iface.BasicAnnotation
    @Nonnull
    public String getType() {
        return this.type.getType();
    }

    @Override // org.jf.dexlib2.iface.Annotation, org.jf.dexlib2.iface.BasicAnnotation
    @Nonnull
    public Set<? extends BuilderAnnotationElement> getElements() {
        return this.elements;
    }
}
