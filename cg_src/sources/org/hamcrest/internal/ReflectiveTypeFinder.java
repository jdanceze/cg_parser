package org.hamcrest.internal;

import java.lang.reflect.Method;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/internal/ReflectiveTypeFinder.class
  gencallgraphv3.jar:hamcrest-core-1.3.jar:org/hamcrest/internal/ReflectiveTypeFinder.class
 */
/* loaded from: gencallgraphv3.jar:org.hamcrest.core_1.3.0.v20180420-1519.jar:org/hamcrest/internal/ReflectiveTypeFinder.class */
public class ReflectiveTypeFinder {
    private final String methodName;
    private final int expectedNumberOfParameters;
    private final int typedParameter;

    public ReflectiveTypeFinder(String methodName, int expectedNumberOfParameters, int typedParameter) {
        this.methodName = methodName;
        this.expectedNumberOfParameters = expectedNumberOfParameters;
        this.typedParameter = typedParameter;
    }

    public Class<?> findExpectedType(Class<?> fromClass) {
        Class<?> cls = fromClass;
        while (true) {
            Class<?> c = cls;
            if (c != Object.class) {
                Method[] arr$ = c.getDeclaredMethods();
                for (Method method : arr$) {
                    if (canObtainExpectedTypeFrom(method)) {
                        return expectedTypeFrom(method);
                    }
                }
                cls = c.getSuperclass();
            } else {
                throw new Error("Cannot determine correct type for " + this.methodName + "() method.");
            }
        }
    }

    protected boolean canObtainExpectedTypeFrom(Method method) {
        return method.getName().equals(this.methodName) && method.getParameterTypes().length == this.expectedNumberOfParameters && !method.isSynthetic();
    }

    protected Class<?> expectedTypeFrom(Method method) {
        return method.getParameterTypes()[this.typedParameter];
    }
}
