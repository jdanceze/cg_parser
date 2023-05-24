package org.hamcrest.generator.qdox.model.annotation;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/model/annotation/AnnotationBinaryOperator.class */
public abstract class AnnotationBinaryOperator implements AnnotationValue {
    private AnnotationValue left;
    private AnnotationValue right;

    public AnnotationBinaryOperator(AnnotationValue left, AnnotationValue right) {
        this.left = left;
        this.right = right;
    }

    public AnnotationValue getLeft() {
        return this.left;
    }

    public AnnotationValue getRight() {
        return this.right;
    }
}
