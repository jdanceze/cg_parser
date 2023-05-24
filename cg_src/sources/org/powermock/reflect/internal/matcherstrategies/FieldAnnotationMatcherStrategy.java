package org.powermock.reflect.internal.matcherstrategies;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import org.powermock.reflect.exceptions.FieldNotFoundException;
/* loaded from: gencallgraphv3.jar:powermock-reflect-2.0.9.jar:org/powermock/reflect/internal/matcherstrategies/FieldAnnotationMatcherStrategy.class */
public class FieldAnnotationMatcherStrategy extends FieldMatcherStrategy {
    final Class<? extends Annotation>[] annotations;

    public FieldAnnotationMatcherStrategy(Class<? extends Annotation>[] annotations) {
        if (annotations == null || annotations.length == 0) {
            throw new IllegalArgumentException("You must specify atleast one annotation.");
        }
        this.annotations = annotations;
    }

    @Override // org.powermock.reflect.internal.matcherstrategies.FieldMatcherStrategy
    public boolean matches(Field field) {
        Class<? extends Annotation>[] clsArr;
        for (Class<? extends Annotation> annotation : this.annotations) {
            if (field.isAnnotationPresent(annotation)) {
                return true;
            }
        }
        return false;
    }

    @Override // org.powermock.reflect.internal.matcherstrategies.FieldMatcherStrategy
    public void notFound(Class<?> type, boolean isInstanceField) throws FieldNotFoundException {
        throw new FieldNotFoundException("No field that has any of the annotation types \"" + getAnnotationNames() + "\" could be found in the class hierarchy of " + type.getName() + ".");
    }

    public String toString() {
        return "annotations " + getAnnotationNames();
    }

    private String getAnnotationNames() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < this.annotations.length; i++) {
            builder.append(this.annotations[i].getName());
            if (i != this.annotations.length - 1) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }
}
