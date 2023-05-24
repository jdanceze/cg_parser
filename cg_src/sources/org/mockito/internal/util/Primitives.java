package org.mockito.internal.util;

import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import java.util.HashMap;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/Primitives.class */
public class Primitives {
    private static final Map<Class<?>, Class<?>> PRIMITIVE_TYPES = new HashMap();
    private static final Map<Class<?>, Object> PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES = new HashMap();

    static {
        PRIMITIVE_TYPES.put(Boolean.class, Boolean.TYPE);
        PRIMITIVE_TYPES.put(Character.class, Character.TYPE);
        PRIMITIVE_TYPES.put(Byte.class, Byte.TYPE);
        PRIMITIVE_TYPES.put(Short.class, Short.TYPE);
        PRIMITIVE_TYPES.put(Integer.class, Integer.TYPE);
        PRIMITIVE_TYPES.put(Long.class, Long.TYPE);
        PRIMITIVE_TYPES.put(Float.class, Float.TYPE);
        PRIMITIVE_TYPES.put(Double.class, Double.TYPE);
        PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES.put(Boolean.class, false);
        PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES.put(Character.class, (char) 0);
        PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES.put(Byte.class, (byte) 0);
        PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES.put(Short.class, (short) 0);
        PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES.put(Integer.class, 0);
        PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES.put(Long.class, 0L);
        PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES.put(Float.class, Float.valueOf(0.0f));
        PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES.put(Double.class, Double.valueOf((double) Const.default_value_double));
        PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES.put(Boolean.TYPE, false);
        PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES.put(Character.TYPE, (char) 0);
        PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES.put(Byte.TYPE, (byte) 0);
        PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES.put(Short.TYPE, (short) 0);
        PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES.put(Integer.TYPE, 0);
        PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES.put(Long.TYPE, 0L);
        PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES.put(Float.TYPE, Float.valueOf(0.0f));
        PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES.put(Double.TYPE, Double.valueOf((double) Const.default_value_double));
    }

    public static <T> Class<T> primitiveTypeOf(Class<T> clazz) {
        if (clazz.isPrimitive()) {
            return clazz;
        }
        return (Class<T>) PRIMITIVE_TYPES.get(clazz);
    }

    public static boolean isPrimitiveOrWrapper(Class<?> type) {
        return PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES.containsKey(type);
    }

    public static boolean isAssignableFromWrapper(Class<?> valueClass, Class<?> referenceType) {
        if (isPrimitiveOrWrapper(valueClass) && isPrimitiveOrWrapper(referenceType)) {
            return primitiveTypeOf(valueClass).isAssignableFrom(primitiveTypeOf(referenceType));
        }
        return false;
    }

    public static <T> T defaultValue(Class<T> primitiveOrWrapperType) {
        return (T) PRIMITIVE_OR_WRAPPER_DEFAULT_VALUES.get(primitiveOrWrapperType);
    }
}
