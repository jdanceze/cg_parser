package org.mockito.internal.creation.bytebuddy;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/bytebuddy/ConstructionCallback.class */
public interface ConstructionCallback {
    Object apply(Class<?> cls, Object obj, Object[] objArr, String[] strArr);
}
