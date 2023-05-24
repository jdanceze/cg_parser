package org.junit.experimental.categories;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runners.model.FrameworkMethod;
import org.junit.validator.AnnotationValidator;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/experimental/categories/CategoryValidator.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/experimental/categories/CategoryValidator.class */
public final class CategoryValidator extends AnnotationValidator {
    private static final Set<Class<? extends Annotation>> INCOMPATIBLE_ANNOTATIONS = Collections.unmodifiableSet(new HashSet(Arrays.asList(BeforeClass.class, AfterClass.class, Before.class, After.class)));

    @Override // org.junit.validator.AnnotationValidator
    public List<Exception> validateAnnotatedMethod(FrameworkMethod method) {
        List<Exception> errors = new ArrayList<>();
        Annotation[] annotations = method.getAnnotations();
        for (Annotation annotation : annotations) {
            for (Class<? extends Annotation> clazz : INCOMPATIBLE_ANNOTATIONS) {
                if (annotation.annotationType().isAssignableFrom(clazz)) {
                    addErrorMessage(errors, clazz);
                }
            }
        }
        return Collections.unmodifiableList(errors);
    }

    private void addErrorMessage(List<Exception> errors, Class<?> clazz) {
        String message = String.format("@%s can not be combined with @Category", clazz.getSimpleName());
        errors.add(new Exception(message));
    }
}
