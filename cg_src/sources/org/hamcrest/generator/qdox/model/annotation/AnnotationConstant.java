package org.hamcrest.generator.qdox.model.annotation;

import java.io.Serializable;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/model/annotation/AnnotationConstant.class */
public class AnnotationConstant implements AnnotationValue, Serializable {
    private final Object value;
    private final String image;

    public AnnotationConstant(Object value, String image) {
        this.value = value;
        this.image = image;
    }

    public Object getValue() {
        return this.value;
    }

    public String getImage() {
        return this.image;
    }

    public String toString() {
        return this.image;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationValue
    public Object accept(AnnotationVisitor visitor) {
        return visitor.visitAnnotationConstant(this);
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationValue
    public Object getParameterValue() {
        return this.image;
    }
}
