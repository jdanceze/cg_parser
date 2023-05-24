package org.hamcrest.generator.qdox.model.annotation;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/model/annotation/AnnotationValue.class */
public interface AnnotationValue {
    Object accept(AnnotationVisitor annotationVisitor);

    Object getParameterValue();
}
