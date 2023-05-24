package org.mockito.internal.creation.bytebuddy;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/bytebuddy/BytecodeGenerator.class */
public interface BytecodeGenerator {
    <T> Class<? extends T> mockClass(MockFeatures<T> mockFeatures);

    void mockClassConstruction(Class<?> cls);

    void mockClassStatic(Class<?> cls);
}
