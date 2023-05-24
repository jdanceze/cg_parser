package org.mockito.internal.util.reflection;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/reflection/GenericTypeExtractor.class */
public class GenericTypeExtractor {
    public static Class<?> genericTypeOf(Class<?> rootClass, Class<?> targetBaseClass, Class<?> targetBaseInterface) {
        Class<?> cls = rootClass;
        while (true) {
            Class<?> match = cls;
            if (match != Object.class) {
                if (match.getSuperclass() == targetBaseClass) {
                    return extractGeneric(match.getGenericSuperclass());
                }
                Type genericInterface = findGenericInterface(match, targetBaseInterface);
                if (genericInterface != null) {
                    return extractGeneric(genericInterface);
                }
                cls = match.getSuperclass();
            } else {
                return Object.class;
            }
        }
    }

    private static Type findGenericInterface(Class<?> sourceClass, Class<?> targetBaseInterface) {
        for (int i = 0; i < sourceClass.getInterfaces().length; i++) {
            Class<?> inter = sourceClass.getInterfaces()[i];
            if (inter == targetBaseInterface) {
                return sourceClass.getGenericInterfaces()[i];
            }
            Type deeper = findGenericInterface(inter, targetBaseInterface);
            if (deeper != null) {
                return deeper;
            }
        }
        return null;
    }

    private static Class<?> extractGeneric(Type type) {
        if (type instanceof ParameterizedType) {
            Type[] genericTypes = ((ParameterizedType) type).getActualTypeArguments();
            if (genericTypes.length > 0 && (genericTypes[0] instanceof Class)) {
                return (Class) genericTypes[0];
            }
            return Object.class;
        }
        return Object.class;
    }
}
