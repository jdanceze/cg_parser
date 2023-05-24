package org.powermock.reflect.internal.primitivesupport;

import java.util.HashMap;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:powermock-reflect-2.0.9.jar:org/powermock/reflect/internal/primitivesupport/BoxedWrapper.class */
public class BoxedWrapper {
    private static final Map<Class<?>, Class<?>> boxedWrapper = new HashMap();

    static {
        boxedWrapper.put(Integer.TYPE, Integer.class);
        boxedWrapper.put(Long.TYPE, Long.class);
        boxedWrapper.put(Float.TYPE, Float.class);
        boxedWrapper.put(Double.TYPE, Double.class);
        boxedWrapper.put(Boolean.TYPE, Boolean.class);
        boxedWrapper.put(Byte.TYPE, Byte.class);
        boxedWrapper.put(Short.TYPE, Short.class);
        boxedWrapper.put(Character.TYPE, Character.class);
    }

    public static Class<?> getBoxedFromPrimitiveType(Class<?> primitiveType) {
        return boxedWrapper.get(primitiveType);
    }

    public static boolean hasBoxedCounterPart(Class<?> type) {
        return boxedWrapper.containsKey(type);
    }
}
