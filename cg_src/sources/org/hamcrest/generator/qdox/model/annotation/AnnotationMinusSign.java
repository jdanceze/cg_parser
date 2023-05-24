package org.hamcrest.generator.qdox.model.annotation;

import org.apache.commons.cli.HelpFormatter;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/model/annotation/AnnotationMinusSign.class */
public class AnnotationMinusSign extends AnnotationUnaryOperator {
    public AnnotationMinusSign(AnnotationValue value) {
        super(value);
    }

    public String toString() {
        return new StringBuffer().append(HelpFormatter.DEFAULT_OPT_PREFIX).append(getValue().toString()).toString();
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationValue
    public Object accept(AnnotationVisitor visitor) {
        return visitor.visitAnnotationMinusSign(this);
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationValue
    public Object getParameterValue() {
        return new StringBuffer().append(HelpFormatter.DEFAULT_OPT_PREFIX).append(getValue().toString()).toString();
    }
}
