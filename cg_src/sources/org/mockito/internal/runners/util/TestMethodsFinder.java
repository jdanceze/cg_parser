package org.mockito.internal.runners.util;

import java.lang.reflect.Method;
import org.junit.Test;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/runners/util/TestMethodsFinder.class */
public class TestMethodsFinder {
    private TestMethodsFinder() {
    }

    public static boolean hasTestMethods(Class<?> klass) {
        Method[] methods = klass.getMethods();
        for (Method m : methods) {
            if (m.isAnnotationPresent(Test.class)) {
                return true;
            }
        }
        return false;
    }
}
