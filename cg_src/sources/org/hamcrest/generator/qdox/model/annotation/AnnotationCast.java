package org.hamcrest.generator.qdox.model.annotation;

import org.hamcrest.generator.qdox.model.Type;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/model/annotation/AnnotationCast.class */
public class AnnotationCast implements AnnotationValue {
    private final Type type;
    private final AnnotationValue value;

    public AnnotationCast(Type type, AnnotationValue value) {
        this.type = type;
        this.value = value;
    }

    public Type getType() {
        return this.type;
    }

    public AnnotationValue getValue() {
        return this.value;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationValue
    public Object accept(AnnotationVisitor visitor) {
        return visitor.visitAnnotationCast(this);
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationValue
    public Object getParameterValue() {
        return new StringBuffer().append("(").append(this.type.getValue()).append(") ").append(this.value.getParameterValue()).toString();
    }

    public String toString() {
        return new StringBuffer().append("(").append(this.type.getValue()).append(") ").append(this.value.toString()).toString();
    }
}
