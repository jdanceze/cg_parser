package org.mockito.internal.creation.bytebuddy;

import java.util.Collections;
import java.util.Set;
import org.mockito.mock.SerializableMode;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/bytebuddy/MockFeatures.class */
class MockFeatures<T> {
    final Class<T> mockedType;
    final Set<Class<?>> interfaces;
    final SerializableMode serializableMode;
    final boolean stripAnnotations;

    private MockFeatures(Class<T> mockedType, Set<Class<?>> interfaces, SerializableMode serializableMode, boolean stripAnnotations) {
        this.mockedType = mockedType;
        this.interfaces = Collections.unmodifiableSet(interfaces);
        this.serializableMode = serializableMode;
        this.stripAnnotations = stripAnnotations;
    }

    public static <T> MockFeatures<T> withMockFeatures(Class<T> mockedType, Set<Class<?>> interfaces, SerializableMode serializableMode, boolean stripAnnotations) {
        return new MockFeatures<>(mockedType, interfaces, serializableMode, stripAnnotations);
    }
}
