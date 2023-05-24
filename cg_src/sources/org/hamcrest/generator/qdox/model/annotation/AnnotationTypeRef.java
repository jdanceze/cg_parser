package org.hamcrest.generator.qdox.model.annotation;

import org.hamcrest.generator.qdox.model.Type;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/model/annotation/AnnotationTypeRef.class */
public class AnnotationTypeRef implements AnnotationValue {
    private Type type;

    public AnnotationTypeRef(Type type) {
        this.type = type;
    }

    public Type getType() {
        return this.type;
    }

    public String toString() {
        return new StringBuffer().append(this.type.getValue()).append(".class").toString();
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationValue
    public Object accept(AnnotationVisitor visitor) {
        return visitor.visitAnnotationTypeRef(this);
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationValue
    public Object getParameterValue() {
        return new StringBuffer().append(this.type.getValue()).append(".class").toString();
    }
}
