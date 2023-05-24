package org.powermock.api.support.membermodification;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.powermock.api.support.SuppressCode;
import org.powermock.api.support.membermodification.strategy.MethodReplaceStrategy;
import org.powermock.api.support.membermodification.strategy.MethodStubStrategy;
import org.powermock.api.support.membermodification.strategy.impl.MethodReplaceStrategyImpl;
import org.powermock.api.support.membermodification.strategy.impl.MethodStubStrategyImpl;
/* loaded from: gencallgraphv3.jar:powermock-api-support-2.0.9.jar:org/powermock/api/support/membermodification/MemberModifier.class */
public class MemberModifier extends MemberMatcher {
    public static void suppress(Method method) {
        SuppressCode.suppressMethod(method);
    }

    public static void suppress(Method[] methods) {
        SuppressCode.suppressMethod(methods);
    }

    public static void suppress(Constructor<?> constructor) {
        SuppressCode.suppressConstructor(constructor);
    }

    public static void suppress(Constructor<?>[] constructors) {
        SuppressCode.suppressConstructor(constructors);
    }

    public static void suppress(Field field) {
        SuppressCode.suppressField(field);
    }

    public static void suppress(Field[] fields) {
        SuppressCode.suppressField(fields);
    }

    public static void suppress(AccessibleObject[] accessibleObjects) {
        if (accessibleObjects == null) {
            throw new IllegalArgumentException("accessibleObjects cannot be null");
        }
        for (AccessibleObject accessibleObject : accessibleObjects) {
            if (accessibleObject instanceof Constructor) {
                SuppressCode.suppressConstructor((Constructor) accessibleObject);
            } else if (accessibleObject instanceof Field) {
                SuppressCode.suppressField((Field) accessibleObject);
            } else if (accessibleObject instanceof Method) {
                SuppressCode.suppressMethod((Method) accessibleObject);
            }
        }
    }

    public static <T> MethodStubStrategy<T> stub(Method method) {
        return new MethodStubStrategyImpl(method);
    }

    public static MethodReplaceStrategy replace(Method method) {
        return new MethodReplaceStrategyImpl(method);
    }
}
