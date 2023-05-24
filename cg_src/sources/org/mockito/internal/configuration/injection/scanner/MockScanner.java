package org.mockito.internal.configuration.injection.scanner;

import java.lang.reflect.Field;
import java.util.Set;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.collections.Sets;
import org.mockito.internal.util.reflection.FieldReader;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/configuration/injection/scanner/MockScanner.class */
public class MockScanner {
    private final Object instance;
    private final Class<?> clazz;

    public MockScanner(Object instance, Class<?> clazz) {
        this.instance = instance;
        this.clazz = clazz;
    }

    public void addPreparedMocks(Set<Object> mocks) {
        mocks.addAll(scan());
    }

    private Set<Object> scan() {
        Field[] declaredFields;
        Set<Object> mocks = Sets.newMockSafeHashSet(new Object[0]);
        for (Field field : this.clazz.getDeclaredFields()) {
            FieldReader fieldReader = new FieldReader(this.instance, field);
            Object mockInstance = preparedMock(fieldReader.read(), field);
            if (mockInstance != null) {
                mocks.add(mockInstance);
            }
        }
        return mocks;
    }

    private Object preparedMock(Object instance, Field field) {
        if (isAnnotatedByMockOrSpy(field)) {
            return instance;
        }
        if (isMockOrSpy(instance)) {
            MockUtil.maybeRedefineMockName(instance, field.getName());
            return instance;
        }
        return null;
    }

    private boolean isAnnotatedByMockOrSpy(Field field) {
        return field.isAnnotationPresent(Spy.class) || field.isAnnotationPresent(Mock.class);
    }

    private boolean isMockOrSpy(Object instance) {
        return MockUtil.isMock(instance) || MockUtil.isSpy(instance);
    }
}
