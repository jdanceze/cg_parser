package org.mockito.internal.configuration;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.mockito.ScopedMock;
import org.mockito.internal.configuration.injection.scanner.InjectMocksScanner;
import org.mockito.internal.configuration.injection.scanner.MockScanner;
import org.mockito.internal.util.collections.Sets;
import org.mockito.plugins.AnnotationEngine;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/configuration/InjectingAnnotationEngine.class */
public class InjectingAnnotationEngine implements AnnotationEngine, org.mockito.configuration.AnnotationEngine {
    private final AnnotationEngine delegate = new IndependentAnnotationEngine();
    private final AnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();

    @Override // org.mockito.plugins.AnnotationEngine
    public AutoCloseable process(Class<?> clazz, Object testInstance) {
        List<AutoCloseable> closeables = new ArrayList<>();
        closeables.addAll(processIndependentAnnotations(testInstance.getClass(), testInstance));
        closeables.addAll(processInjectMocks(testInstance.getClass(), testInstance));
        return () -> {
            Iterator it = closeables.iterator();
            while (it.hasNext()) {
                AutoCloseable closeable = (AutoCloseable) it.next();
                closeable.close();
            }
        };
    }

    private List<AutoCloseable> processInjectMocks(Class<?> clazz, Object testInstance) {
        List<AutoCloseable> closeables = new ArrayList<>();
        Class<?> cls = clazz;
        while (true) {
            Class<?> classContext = cls;
            if (classContext != Object.class) {
                closeables.add(injectCloseableMocks(testInstance));
                cls = classContext.getSuperclass();
            } else {
                return closeables;
            }
        }
    }

    private List<AutoCloseable> processIndependentAnnotations(Class<?> clazz, Object testInstance) {
        List<AutoCloseable> closeables = new ArrayList<>();
        Class<?> cls = clazz;
        while (true) {
            Class<?> classContext = cls;
            if (classContext != Object.class) {
                closeables.add(this.delegate.process(classContext, testInstance));
                closeables.add(this.spyAnnotationEngine.process(classContext, testInstance));
                cls = classContext.getSuperclass();
            } else {
                return closeables;
            }
        }
    }

    @Deprecated
    public void injectMocks(Object testClassInstance) {
        try {
            injectCloseableMocks(testClassInstance).close();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private AutoCloseable injectCloseableMocks(Object testClassInstance) {
        Set<Field> mockDependentFields = new HashSet<>();
        Set<Object> mocks = Sets.newMockSafeHashSet(new Object[0]);
        for (Class<?> clazz = testClassInstance.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            new InjectMocksScanner(clazz).addTo(mockDependentFields);
            new MockScanner(testClassInstance, clazz).addPreparedMocks(mocks);
            onInjection(testClassInstance, clazz, mockDependentFields, mocks);
        }
        new DefaultInjectionEngine().injectMocksOnFields(mockDependentFields, mocks, testClassInstance);
        return () -> {
            for (Object mock : mocks) {
                if (mock instanceof ScopedMock) {
                    ((ScopedMock) mock).closeOnDemand();
                }
            }
        };
    }

    protected void onInjection(Object testClassInstance, Class<?> clazz, Set<Field> mockDependentFields, Set<Object> mocks) {
    }
}
