package org.hamcrest.generator.qdox.model.annotation;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/model/annotation/AnnotationShiftRight.class */
public class AnnotationShiftRight extends AnnotationBinaryOperator {
    public AnnotationShiftRight(AnnotationValue left, AnnotationValue right) {
        super(left, right);
    }

    public String toString() {
        return new StringBuffer().append(getLeft().toString()).append(" >> ").append(getRight().toString()).toString();
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationValue
    public Object accept(AnnotationVisitor visitor) {
        return visitor.visitAnnotationShiftRight(this);
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationValue
    public Object getParameterValue() {
        return new StringBuffer().append(getLeft().getParameterValue()).append(" >> ").append(getRight().getParameterValue()).toString();
    }
}
