package org.mockito.internal.util.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/reflection/GenericMaster.class */
public class GenericMaster {
    public Class<?> getGenericType(Field field) {
        Type generic = field.getGenericType();
        if (generic instanceof ParameterizedType) {
            Type actual = ((ParameterizedType) generic).getActualTypeArguments()[0];
            if (actual instanceof Class) {
                return (Class) actual;
            }
            if (actual instanceof ParameterizedType) {
                return (Class) ((ParameterizedType) actual).getRawType();
            }
            return Object.class;
        }
        return Object.class;
    }
}
