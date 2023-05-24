package org.hamcrest.generator.qdox.model.annotation;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/model/annotation/AnnotationParenExpression.class */
public class AnnotationParenExpression implements AnnotationValue {
    private AnnotationValue value;

    public AnnotationParenExpression(AnnotationValue value) {
        this.value = value;
    }

    public AnnotationValue getValue() {
        return this.value;
    }

    public String toString() {
        return new StringBuffer().append("(").append(this.value.toString()).append(")").toString();
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationValue
    public Object accept(AnnotationVisitor visitor) {
        return visitor.visitAnnotationParenExpression(this);
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationValue
    public Object getParameterValue() {
        return new StringBuffer().append("(").append(this.value.getParameterValue()).append(")").toString();
    }
}
