package org.mockito.internal.creation;

import java.util.Arrays;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/SuspendMethod.class */
public class SuspendMethod {
    private static final String KOTLIN_EXPERIMENTAL_CONTINUATION = "kotlin.coroutines.experimental.Continuation";
    private static final String KOTLIN_CONTINUATION = "kotlin.coroutines.Continuation";

    public static Class<?>[] trimSuspendParameterTypes(Class<?>[] parameterTypes) {
        int n = parameterTypes.length;
        if (n > 0 && isContinuationType(parameterTypes[n - 1])) {
            return (Class[]) Arrays.copyOf(parameterTypes, n - 1);
        }
        return parameterTypes;
    }

    private static boolean isContinuationType(Class<?> parameterType) {
        String name = parameterType.getName();
        return name.equals(KOTLIN_CONTINUATION) || name.equals(KOTLIN_EXPERIMENTAL_CONTINUATION);
    }
}
