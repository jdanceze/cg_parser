package org.mockito.internal.configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.ScopedMock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.exceptions.Reporter;
import org.mockito.plugins.AnnotationEngine;
import org.mockito.plugins.MemberAccessor;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/configuration/IndependentAnnotationEngine.class */
public class IndependentAnnotationEngine implements AnnotationEngine, org.mockito.configuration.AnnotationEngine {
    private final Map<Class<? extends Annotation>, FieldAnnotationProcessor<?>> annotationProcessorMap = new HashMap();

    public IndependentAnnotationEngine() {
        registerAnnotationProcessor(Mock.class, new MockAnnotationProcessor());
        registerAnnotationProcessor(Captor.class, new CaptorAnnotationProcessor());
    }

    private Object createMockFor(Annotation annotation, Field field) {
        return forAnnotation(annotation).process(annotation, field);
    }

    private <A extends Annotation> FieldAnnotationProcessor<A> forAnnotation(A annotation) {
        if (this.annotationProcessorMap.containsKey(annotation.annotationType())) {
            return (FieldAnnotationProcessor<A>) this.annotationProcessorMap.get(annotation.annotationType());
        }
        return (FieldAnnotationProcessor<A>) new FieldAnnotationProcessor<A>() { // from class: org.mockito.internal.configuration.IndependentAnnotationEngine.1
            /* JADX WARN: Incorrect types in method signature: (TA;Ljava/lang/reflect/Field;)Ljava/lang/Object; */
            @Override // org.mockito.internal.configuration.FieldAnnotationProcessor
            public Object process(Annotation annotation2, Field field) {
                return null;
            }
        };
    }

    private <A extends Annotation> void registerAnnotationProcessor(Class<A> annotationClass, FieldAnnotationProcessor<A> fieldAnnotationProcessor) {
        this.annotationProcessorMap.put(annotationClass, fieldAnnotationProcessor);
    }

    @Override // org.mockito.plugins.AnnotationEngine
    public AutoCloseable process(Class<?> clazz, Object testInstance) {
        Annotation[] annotations;
        List<ScopedMock> scopedMocks = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            boolean alreadyAssigned = false;
            for (Annotation annotation : field.getAnnotations()) {
                Object mock = createMockFor(annotation, field);
                if (mock instanceof ScopedMock) {
                    scopedMocks.add((ScopedMock) mock);
                }
                if (mock != null) {
                    throwIfAlreadyAssigned(field, alreadyAssigned);
                    alreadyAssigned = true;
                    MemberAccessor accessor = Plugins.getMemberAccessor();
                    try {
                        accessor.set(field, testInstance, mock);
                    } catch (Exception e) {
                        for (ScopedMock scopedMock : scopedMocks) {
                            scopedMock.close();
                        }
                        throw new MockitoException("Problems setting field " + field.getName() + " annotated with " + annotation, e);
                    }
                }
            }
        }
        return () -> {
            Iterator it = scopedMocks.iterator();
            while (it.hasNext()) {
                ScopedMock scopedMock2 = (ScopedMock) it.next();
                scopedMock2.closeOnDemand();
            }
        };
    }

    void throwIfAlreadyAssigned(Field field, boolean alreadyAssigned) {
        if (alreadyAssigned) {
            throw Reporter.moreThanOneAnnotationNotAllowed(field.getName());
        }
    }
}
