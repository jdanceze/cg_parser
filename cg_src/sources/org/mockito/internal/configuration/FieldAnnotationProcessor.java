package org.mockito.internal.configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/configuration/FieldAnnotationProcessor.class */
public interface FieldAnnotationProcessor<A extends Annotation> {
    Object process(A a, Field field);
}
