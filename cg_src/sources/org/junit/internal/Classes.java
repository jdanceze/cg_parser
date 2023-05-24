package org.junit.internal;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/internal/Classes.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/internal/Classes.class */
public class Classes {
    public static Class<?> getClass(String className) throws ClassNotFoundException {
        return getClass(className, Classes.class);
    }

    public static Class<?> getClass(String className, Class<?> callingClass) throws ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return Class.forName(className, true, classLoader == null ? callingClass.getClassLoader() : classLoader);
    }
}
