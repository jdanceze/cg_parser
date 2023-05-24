package org.mockito.internal.util.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.plugins.MemberAccessor;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/reflection/LenientCopyTool.class */
public class LenientCopyTool {
    MemberAccessor accessor = Plugins.getMemberAccessor();

    public <T> void copyToMock(T from, T mock) {
        copy(from, mock, from.getClass());
    }

    public <T> void copyToRealObject(T from, T to) {
        copy(from, to, from.getClass());
    }

    private <T> void copy(T from, T to, Class<?> fromClazz) {
        while (fromClazz != Object.class) {
            copyValues(from, to, fromClazz);
            fromClazz = fromClazz.getSuperclass();
        }
    }

    private <T> void copyValues(T from, T mock, Class<?> classFrom) {
        Field[] fields = classFrom.getDeclaredFields();
        for (Field field : fields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                try {
                    Object value = this.accessor.get(field, from);
                    this.accessor.set(field, mock, value);
                } catch (Throwable th) {
                }
            }
        }
    }
}
