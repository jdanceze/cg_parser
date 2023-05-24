package org.powermock.api.support;
/* loaded from: gencallgraphv3.jar:powermock-api-support-2.0.9.jar:org/powermock/api/support/ClassLoaderUtil.class */
public class ClassLoaderUtil {
    public static <T> Class<T> loadClass(Class<T> type, ClassLoader classloader) {
        return loadClass(type.getName(), classloader);
    }

    public static <T> Class<T> loadClass(String className) {
        return loadClass(className, ClassLoaderUtil.class.getClassLoader());
    }

    public static <T> boolean hasClass(Class<T> type, ClassLoader classloader) {
        try {
            loadClass(type.getName(), classloader);
            return true;
        } catch (RuntimeException e) {
            if (e.getCause() instanceof ClassNotFoundException) {
                return false;
            }
            throw e;
        }
    }

    public static <T> Class<T> loadClass(String className, ClassLoader classloader) {
        if (className == null) {
            throw new IllegalArgumentException("className cannot be null");
        }
        if (classloader == null) {
            throw new IllegalArgumentException("classloader cannot be null");
        }
        try {
            return (Class<T>) Class.forName(className, false, classloader);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
