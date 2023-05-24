package org.powermock.reflect.internal.primitivesupport;

import java.util.HashMap;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:powermock-reflect-2.0.9.jar:org/powermock/reflect/internal/primitivesupport/PrimitiveWrapper.class */
public class PrimitiveWrapper {
    private static final Map<Class<?>, Class<?>> primitiveWrapper = new HashMap();

    static {
        primitiveWrapper.put(Integer.class, Integer.TYPE);
        primitiveWrapper.put(Long.class, Long.TYPE);
        primitiveWrapper.put(Float.class, Float.TYPE);
        primitiveWrapper.put(Double.class, Double.TYPE);
        primitiveWrapper.put(Boolean.class, Boolean.TYPE);
        primitiveWrapper.put(Byte.class, Byte.TYPE);
        primitiveWrapper.put(Short.class, Short.TYPE);
        primitiveWrapper.put(Character.class, Character.TYPE);
    }

    public static Class<?>[] toPrimitiveType(Class<?>[] types) {
        if (types == null) {
            throw new IllegalArgumentException("types cannot be null");
        }
        Class<?>[] convertedTypes = new Class[types.length];
        for (int i = 0; i < types.length; i++) {
            Class<?> originalType = types[i];
            Class<?> primitiveType = primitiveWrapper.get(originalType);
            if (primitiveType == null) {
                convertedTypes[i] = originalType;
            } else {
                convertedTypes[i] = primitiveType;
            }
        }
        return convertedTypes;
    }

    public static Class<?> getPrimitiveFromWrapperType(Class<?> wrapperType) {
        return primitiveWrapper.get(wrapperType);
    }

    public static boolean hasPrimitiveCounterPart(Class<?> type) {
        return primitiveWrapper.containsKey(type);
    }
}
