package org.mockito.internal.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.mockito.creation.instance.InstantiationException;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/JavaEightUtil.class */
public final class JavaEightUtil {
    private static Object emptyOptional;
    private static Object emptyOptionalDouble;
    private static Object emptyOptionalInt;
    private static Object emptyOptionalLong;
    private static Object emptyDuration;
    private static Object emptyPeriod;

    private JavaEightUtil() {
    }

    public static Object emptyOptional() {
        if (emptyOptional != null) {
            return emptyOptional;
        }
        Object invokeNullaryFactoryMethod = invokeNullaryFactoryMethod("java.util.Optional", "empty");
        emptyOptional = invokeNullaryFactoryMethod;
        return invokeNullaryFactoryMethod;
    }

    public static Object emptyOptionalDouble() {
        if (emptyOptionalDouble != null) {
            return emptyOptionalDouble;
        }
        Object invokeNullaryFactoryMethod = invokeNullaryFactoryMethod("java.util.OptionalDouble", "empty");
        emptyOptionalDouble = invokeNullaryFactoryMethod;
        return invokeNullaryFactoryMethod;
    }

    public static Object emptyOptionalInt() {
        if (emptyOptionalInt != null) {
            return emptyOptionalInt;
        }
        Object invokeNullaryFactoryMethod = invokeNullaryFactoryMethod("java.util.OptionalInt", "empty");
        emptyOptionalInt = invokeNullaryFactoryMethod;
        return invokeNullaryFactoryMethod;
    }

    public static Object emptyOptionalLong() {
        if (emptyOptionalLong != null) {
            return emptyOptionalLong;
        }
        Object invokeNullaryFactoryMethod = invokeNullaryFactoryMethod("java.util.OptionalLong", "empty");
        emptyOptionalLong = invokeNullaryFactoryMethod;
        return invokeNullaryFactoryMethod;
    }

    public static Object emptyStream() {
        return invokeNullaryFactoryMethod("java.util.stream.Stream", "empty");
    }

    public static Object emptyDoubleStream() {
        return invokeNullaryFactoryMethod("java.util.stream.DoubleStream", "empty");
    }

    public static Object emptyIntStream() {
        return invokeNullaryFactoryMethod("java.util.stream.IntStream", "empty");
    }

    public static Object emptyLongStream() {
        return invokeNullaryFactoryMethod("java.util.stream.LongStream", "empty");
    }

    public static Object emptyDuration() {
        if (emptyDuration != null) {
            return emptyDuration;
        }
        Object staticFieldValue = getStaticFieldValue("java.time.Duration", "ZERO");
        emptyDuration = staticFieldValue;
        return staticFieldValue;
    }

    public static Object emptyPeriod() {
        if (emptyPeriod != null) {
            return emptyPeriod;
        }
        Object staticFieldValue = getStaticFieldValue("java.time.Period", "ZERO");
        emptyPeriod = staticFieldValue;
        return staticFieldValue;
    }

    private static Object invokeNullaryFactoryMethod(String fqcn, String methodName) {
        try {
            Method method = getMethod(fqcn, methodName, new Class[0]);
            return method.invoke(null, new Object[0]);
        } catch (Exception e) {
            throw new InstantiationException(String.format("Could not create %s#%s(): %s", fqcn, methodName, e), e);
        }
    }

    private static Object getStaticFieldValue(String fqcn, String fieldName) {
        try {
            Class<?> type = getClass(fqcn);
            Field field = type.getField(fieldName);
            return field.get(null);
        } catch (Exception e) {
            throw new InstantiationException(String.format("Could not get %s#%s(): %s", fqcn, fieldName, e), e);
        }
    }

    private static Class<?> getClass(String fqcn) {
        try {
            return Class.forName(fqcn);
        } catch (ClassNotFoundException e) {
            throw new InstantiationException(String.format("Could not find %s: %s", fqcn, e), e);
        }
    }

    private static Method getMethod(String fqcn, String methodName, Class<?>... parameterClasses) {
        try {
            Class<?> type = getClass(fqcn);
            return type.getMethod(methodName, parameterClasses);
        } catch (Exception e) {
            throw new InstantiationException(String.format("Could not find %s#%s(): %s", fqcn, methodName, e), e);
        }
    }
}
