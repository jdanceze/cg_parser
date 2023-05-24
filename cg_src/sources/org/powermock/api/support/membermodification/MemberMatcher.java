package org.powermock.api.support.membermodification;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.powermock.reflect.Whitebox;
import org.powermock.reflect.internal.WhiteboxImpl;
import org.powermock.tests.utils.impl.ArrayMergerImpl;
/* loaded from: gencallgraphv3.jar:powermock-api-support-2.0.9.jar:org/powermock/api/support/membermodification/MemberMatcher.class */
public class MemberMatcher {
    public static Method[] methodsDeclaredIn(Class<?> cls, Class<?>... additionalClasses) {
        if (cls == null) {
            throw new IllegalArgumentException("You need to supply at least one class.");
        }
        Set<Method> methods = new HashSet<>();
        methods.addAll(Arrays.asList(WhiteboxImpl.getAllMethods(cls)));
        for (Class<?> klass : additionalClasses) {
            methods.addAll(Arrays.asList(WhiteboxImpl.getAllMethods(klass)));
        }
        return (Method[]) methods.toArray(new Method[methods.size()]);
    }

    public static Method method(Class<?> declaringClass, String methodName, Class<?>... parameterTypes) {
        Method method = WhiteboxImpl.findMethod(declaringClass, methodName, parameterTypes);
        WhiteboxImpl.throwExceptionIfMethodWasNotFound(declaringClass, methodName, method, parameterTypes);
        return method;
    }

    public static Method method(Class<?> declaringClass, Class<?>... parameterTypes) {
        return Whitebox.getMethod(declaringClass, parameterTypes);
    }

    public static Method[] methods(Class<?> clazz, String methodName, String... additionalMethodNames) {
        return Whitebox.getMethods(clazz, merge(methodName, additionalMethodNames));
    }

    public static Method[] methods(Method method, Method... additionalMethods) {
        return merge(method, additionalMethods);
    }

    public static Method[] methods(Class<?> clazz, String[] methodNames) {
        return Whitebox.getMethods(clazz, methodNames);
    }

    public static Field field(Class<?> declaringClass, String fieldName) {
        return Whitebox.getField(declaringClass, fieldName);
    }

    public static Field[] fields(Class<?> clazz, String firstFieldName, String... additionalfieldNames) {
        return Whitebox.getFields(clazz, merge(firstFieldName, additionalfieldNames));
    }

    public static Field[] fields(Class<?> clazz) {
        return WhiteboxImpl.getAllFields(clazz);
    }

    public static Field[] fields(Field field, Field... additionalFields) {
        return merge(field, additionalFields);
    }

    public static Field[] fields(Class<?> clazz, String[] fieldNames) {
        return Whitebox.getFields(clazz, fieldNames);
    }

    public static <T> Constructor<T> constructor(Class<T> declaringClass, Class<?>... parameterTypes) {
        return (Constructor<T>) WhiteboxImpl.findUniqueConstructorOrThrowException(declaringClass, parameterTypes);
    }

    public static <T> Constructor<T> constructor(Class<T> declaringClass) {
        return (Constructor<T>) WhiteboxImpl.findConstructorOrThrowException(declaringClass);
    }

    public static <T> Constructor<T> defaultConstructorIn(Class<T> declaringClass) {
        return (Constructor<T>) WhiteboxImpl.findDefaultConstructorOrThrowException(declaringClass);
    }

    public static Constructor<?>[] constructorsDeclaredIn(Class<?> cls, Class<?>... additionalClasses) {
        if (cls == null) {
            throw new IllegalArgumentException("You need to supply at least one class.");
        }
        Set<Constructor<?>> constructors = new HashSet<>();
        constructors.addAll(Arrays.asList(WhiteboxImpl.getAllConstructors(cls)));
        for (Class<?> klass : additionalClasses) {
            constructors.addAll(Arrays.asList(WhiteboxImpl.getAllConstructors(klass)));
        }
        return (Constructor[]) constructors.toArray(new Constructor[constructors.size()]);
    }

    public static Constructor<?>[] constructors(Constructor<?> constructor, Constructor<?>... additionalConstructors) {
        return merge(constructor, additionalConstructors);
    }

    public static AccessibleObject[] everythingDeclaredIn(Class<?> cls, Class<?>... additionalClasses) {
        if (cls == null) {
            throw new IllegalArgumentException("You need to supply at least one class.");
        }
        Set<AccessibleObject> accessibleObjects = new HashSet<>();
        accessibleObjects.addAll(Collections.unmodifiableCollection(Arrays.asList(methodsDeclaredIn(cls, additionalClasses))));
        accessibleObjects.addAll(Collections.unmodifiableCollection(Arrays.asList(constructorsDeclaredIn(cls, additionalClasses))));
        return (AccessibleObject[]) accessibleObjects.toArray(new AccessibleObject[accessibleObjects.size()]);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static String[] merge(String first, String... additional) {
        return (String[]) new ArrayMergerImpl().mergeArrays(String.class, new String[]{first}, additional);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static Method[] merge(Method first, Method... additional) {
        return (Method[]) new ArrayMergerImpl().mergeArrays(Method.class, new Method[]{first}, additional);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static Field[] merge(Field first, Field... additional) {
        return (Field[]) new ArrayMergerImpl().mergeArrays(Field.class, new Field[]{first}, additional);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static Constructor<?>[] merge(Constructor<?> first, Constructor<?>... additional) {
        return (Constructor[]) new ArrayMergerImpl().mergeArrays(Constructor.class, new Constructor[]{first}, additional);
    }
}
