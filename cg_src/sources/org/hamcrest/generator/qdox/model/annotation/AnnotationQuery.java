package org.hamcrest.generator.qdox.model.annotation;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/model/annotation/AnnotationQuery.class */
public class AnnotationQuery implements AnnotationValue {
    private final AnnotationValue condition;
    private final AnnotationValue trueExpression;
    private final AnnotationValue falseExpression;

    public AnnotationQuery(AnnotationValue condition, AnnotationValue trueExpression, AnnotationValue falseExpression) {
        this.condition = condition;
        this.trueExpression = trueExpression;
        this.falseExpression = falseExpression;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationValue
    public Object accept(AnnotationVisitor visitor) {
        return visitor.visitAnnotationQuery(this);
    }

    public AnnotationValue getCondition() {
        return this.condition;
    }

    public AnnotationValue getTrueExpression() {
        return this.trueExpression;
    }

    public AnnotationValue getFalseExpression() {
        return this.falseExpression;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationValue
    public Object getParameterValue() {
        return new StringBuffer().append(this.condition.getParameterValue().toString()).append(" ? ").append(this.trueExpression.getParameterValue()).append(" : ").append(this.falseExpression.getParameterValue()).toString();
    }

    public String toString() {
        return new StringBuffer().append(this.condition.toString()).append(" ? ").append(this.trueExpression.toString()).append(" : ").append(this.falseExpression.toString()).toString();
    }
}
