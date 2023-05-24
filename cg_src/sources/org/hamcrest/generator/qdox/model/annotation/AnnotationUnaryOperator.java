package org.hamcrest.generator.qdox.model.annotation;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/model/annotation/AnnotationUnaryOperator.class */
public abstract class AnnotationUnaryOperator implements AnnotationValue {
    private AnnotationValue value;

    public AnnotationUnaryOperator(AnnotationValue value) {
        this.value = value;
    }

    public AnnotationValue getValue() {
        return this.value;
    }
}
