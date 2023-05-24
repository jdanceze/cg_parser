package org.mockito.internal.configuration.injection.scanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.exceptions.Reporter;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/configuration/injection/scanner/InjectMocksScanner.class */
public class InjectMocksScanner {
    private final Class<?> clazz;

    public InjectMocksScanner(Class<?> clazz) {
        this.clazz = clazz;
    }

    public void addTo(Set<Field> mockDependentFields) {
        mockDependentFields.addAll(scan());
    }

    private Set<Field> scan() {
        Set<Field> mockDependentFields = new HashSet<>();
        Field[] fields = this.clazz.getDeclaredFields();
        for (Field field : fields) {
            if (null != field.getAnnotation(InjectMocks.class)) {
                assertNoAnnotations(field, Mock.class, Captor.class);
                mockDependentFields.add(field);
            }
        }
        return mockDependentFields;
    }

    private static void assertNoAnnotations(Field field, Class<? extends Annotation>... annotations) {
        for (Class<? extends Annotation> annotation : annotations) {
            if (field.isAnnotationPresent(annotation)) {
                throw Reporter.unsupportedCombinationOfAnnotations(annotation.getSimpleName(), InjectMocks.class.getSimpleName());
            }
        }
    }
}
