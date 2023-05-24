package org.mockito.internal.util;

import java.io.Serializable;
import org.mockito.mock.MockName;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/MockNameImpl.class */
public class MockNameImpl implements MockName, Serializable {
    private static final long serialVersionUID = 8014974700844306925L;
    private final String mockName;
    private boolean defaultName;

    public MockNameImpl(String mockName, Class<?> type, boolean mockedStatic) {
        if (mockName == null) {
            this.mockName = mockedStatic ? toClassName(type) : toInstanceName(type);
            this.defaultName = true;
            return;
        }
        this.mockName = mockName;
    }

    public MockNameImpl(String mockName) {
        this.mockName = mockName;
    }

    private static String toInstanceName(Class<?> clazz) {
        String className = clazz.getSimpleName();
        if (className.length() == 0) {
            className = clazz.getSuperclass().getSimpleName();
        }
        return className.substring(0, 1).toLowerCase() + className.substring(1);
    }

    private static String toClassName(Class<?> clazz) {
        String className = clazz.getSimpleName();
        if (className.length() == 0) {
            className = clazz.getSuperclass().getSimpleName() + "$";
        }
        return className + ".class";
    }

    @Override // org.mockito.mock.MockName
    public boolean isDefault() {
        return this.defaultName;
    }

    @Override // org.mockito.mock.MockName
    public String toString() {
        return this.mockName;
    }
}
