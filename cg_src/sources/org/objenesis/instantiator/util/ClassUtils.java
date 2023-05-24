package org.objenesis.instantiator.util;

import org.objenesis.ObjenesisException;
/* loaded from: gencallgraphv3.jar:objenesis-3.1.jar:org/objenesis/instantiator/util/ClassUtils.class */
public final class ClassUtils {
    private ClassUtils() {
    }

    public static String classNameToInternalClassName(String className) {
        return className.replace('.', '/');
    }

    public static String classNameToResource(String className) {
        return classNameToInternalClassName(className) + ".class";
    }

    public static <T> Class<T> getExistingClass(ClassLoader classLoader, String className) {
        try {
            return (Class<T>) Class.forName(className, true, classLoader);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new ObjenesisException(e);
        }
    }
}
