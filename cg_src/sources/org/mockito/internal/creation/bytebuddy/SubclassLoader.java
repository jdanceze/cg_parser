package org.mockito.internal.creation.bytebuddy;

import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/bytebuddy/SubclassLoader.class */
public interface SubclassLoader {
    boolean isDisrespectingOpenness();

    ClassLoadingStrategy<ClassLoader> resolveStrategy(Class<?> cls, ClassLoader classLoader, boolean z);
}
