package org.junit.internal.matchers;

import java.lang.reflect.Method;
import org.hamcrest.BaseMatcher;
import org.junit.internal.MethodSorter;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/internal/matchers/TypeSafeMatcher.class
 */
@Deprecated
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/internal/matchers/TypeSafeMatcher.class */
public abstract class TypeSafeMatcher<T> extends BaseMatcher<T> {
    private Class<?> expectedType;

    public abstract boolean matchesSafely(T t);

    protected TypeSafeMatcher() {
        this.expectedType = findExpectedType(getClass());
    }

    private static Class<?> findExpectedType(Class<?> fromClass) {
        Class<?> cls = fromClass;
        while (true) {
            Class<?> c = cls;
            if (c != Object.class) {
                Method[] arr$ = MethodSorter.getDeclaredMethods(c);
                for (Method method : arr$) {
                    if (isMatchesSafelyMethod(method)) {
                        return method.getParameterTypes()[0];
                    }
                }
                cls = c.getSuperclass();
            } else {
                throw new Error("Cannot determine correct type for matchesSafely() method.");
            }
        }
    }

    private static boolean isMatchesSafelyMethod(Method method) {
        return "matchesSafely".equals(method.getName()) && method.getParameterTypes().length == 1 && !method.isSynthetic();
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected TypeSafeMatcher(Class<T> expectedType) {
        this.expectedType = expectedType;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.hamcrest.Matcher
    public final boolean matches(Object item) {
        return item != 0 && this.expectedType.isInstance(item) && matchesSafely(item);
    }
}
