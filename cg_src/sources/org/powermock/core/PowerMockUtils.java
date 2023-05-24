package org.powermock.core;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Vector;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/PowerMockUtils.class */
public class PowerMockUtils {
    public static Iterator<Class<?>> getClassIterator(ClassLoader classLoader) throws NoSuchFieldException, IllegalAccessException {
        Class<?> cls = classLoader.getClass();
        while (true) {
            Class<?> classLoaderClass = cls;
            if (classLoaderClass != ClassLoader.class) {
                cls = classLoaderClass.getSuperclass();
            } else {
                Field classesField = classLoaderClass.getDeclaredField("classes");
                classesField.setAccessible(true);
                Vector<Class<?>> classes = (Vector) classesField.get(classLoader);
                return classes.iterator();
            }
        }
    }

    public static void printClassesLoadedByClassloader(ClassLoader classLoader, boolean includeParent) throws NoSuchFieldException, IllegalAccessException {
        while (classLoader != null) {
            System.out.println("ClassLoader: " + classLoader);
            Iterator<?> iter = getClassIterator(classLoader);
            while (iter.hasNext()) {
                System.out.println("\t" + iter.next());
            }
            if (includeParent) {
                classLoader = classLoader.getParent();
            } else {
                classLoader = null;
            }
        }
    }
}
