package org.hamcrest.generator.qdox.model.annotation;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/model/annotation/AnnotationLogicalNot.class */
public class AnnotationLogicalNot extends AnnotationUnaryOperator {
    public AnnotationLogicalNot(AnnotationValue value) {
        super(value);
    }

    public String toString() {
        return new StringBuffer().append("!").append(getValue().toString()).toString();
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationValue
    public Object accept(AnnotationVisitor visitor) {
        return visitor.visitAnnotationLogicalNot(this);
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationValue
    public Object getParameterValue() {
        return new StringBuffer().append("!").append(getValue().toString()).toString();
    }
}
