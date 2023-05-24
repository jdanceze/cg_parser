package org.mockito.internal.util.reflection;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.mockito.plugins.MemberAccessor;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/reflection/ReflectionMemberAccessor.class */
public class ReflectionMemberAccessor implements MemberAccessor {
    @Override // org.mockito.plugins.MemberAccessor
    public Object newInstance(Constructor<?> constructor, Object... arguments) throws InstantiationException, InvocationTargetException, IllegalAccessException {
        return newInstance(constructor, (v0) -> {
            return v0.newInstance();
        }, arguments);
    }

    @Override // org.mockito.plugins.MemberAccessor
    public Object newInstance(Constructor<?> constructor, MemberAccessor.OnConstruction onConstruction, Object... arguments) throws InstantiationException, InvocationTargetException, IllegalAccessException {
        silentSetAccessible(constructor, true);
        try {
            try {
                Object invoke = onConstruction.invoke(() -> {
                    return constructor.newInstance(arguments);
                });
                silentSetAccessible(constructor, false);
                return invoke;
            } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | InvocationTargetException e) {
                throw e;
            } catch (Exception e2) {
                throw new IllegalStateException("Failed to invoke " + constructor + " with " + Arrays.toString(arguments), e2);
            }
        } catch (Throwable th) {
            silentSetAccessible(constructor, false);
            throw th;
        }
    }

    @Override // org.mockito.plugins.MemberAccessor
    public Object invoke(Method method, Object target, Object... arguments) throws InvocationTargetException, IllegalAccessException {
        silentSetAccessible(method, true);
        try {
            try {
                try {
                    Object invoke = method.invoke(target, arguments);
                    silentSetAccessible(method, false);
                    return invoke;
                } catch (Exception e) {
                    throw new IllegalStateException("Could not invoke " + method + " on " + target, e);
                }
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e2) {
                throw e2;
            }
        } catch (Throwable th) {
            silentSetAccessible(method, false);
            throw th;
        }
    }

    @Override // org.mockito.plugins.MemberAccessor
    public Object get(Field field, Object target) throws IllegalAccessException {
        silentSetAccessible(field, true);
        try {
            try {
                Object obj = field.get(target);
                silentSetAccessible(field, false);
                return obj;
            } catch (IllegalAccessException | IllegalArgumentException e) {
                throw e;
            } catch (Exception e2) {
                throw new IllegalStateException("Could not read " + field + " from " + target);
            }
        } catch (Throwable th) {
            silentSetAccessible(field, false);
            throw th;
        }
    }

    @Override // org.mockito.plugins.MemberAccessor
    public void set(Field field, Object target, Object value) throws IllegalAccessException {
        silentSetAccessible(field, true);
        try {
            try {
                try {
                    field.set(target, value);
                    silentSetAccessible(field, false);
                } catch (IllegalAccessException | IllegalArgumentException e) {
                    throw e;
                }
            } catch (Exception e2) {
                throw new IllegalStateException("Could not write " + field + " to " + target, e2);
            }
        } catch (Throwable th) {
            silentSetAccessible(field, false);
            throw th;
        }
    }

    private static void silentSetAccessible(AccessibleObject object, boolean value) {
        try {
            object.setAccessible(value);
        } catch (Exception e) {
        }
    }
}
