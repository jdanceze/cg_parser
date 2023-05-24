package org.junit.internal;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import org.junit.FixMethodOrder;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/internal/MethodSorter.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/internal/MethodSorter.class */
public class MethodSorter {
    public static final Comparator<Method> DEFAULT = new Comparator<Method>() { // from class: org.junit.internal.MethodSorter.1
        @Override // java.util.Comparator
        public int compare(Method m1, Method m2) {
            int i1 = m1.getName().hashCode();
            int i2 = m2.getName().hashCode();
            if (i1 != i2) {
                return i1 < i2 ? -1 : 1;
            }
            return MethodSorter.NAME_ASCENDING.compare(m1, m2);
        }
    };
    public static final Comparator<Method> NAME_ASCENDING = new Comparator<Method>() { // from class: org.junit.internal.MethodSorter.2
        @Override // java.util.Comparator
        public int compare(Method m1, Method m2) {
            int comparison = m1.getName().compareTo(m2.getName());
            if (comparison != 0) {
                return comparison;
            }
            return m1.toString().compareTo(m2.toString());
        }
    };

    public static Method[] getDeclaredMethods(Class<?> clazz) {
        Comparator<Method> comparator = getSorter((FixMethodOrder) clazz.getAnnotation(FixMethodOrder.class));
        Method[] methods = clazz.getDeclaredMethods();
        if (comparator != null) {
            Arrays.sort(methods, comparator);
        }
        return methods;
    }

    private MethodSorter() {
    }

    private static Comparator<Method> getSorter(FixMethodOrder fixMethodOrder) {
        if (fixMethodOrder == null) {
            return DEFAULT;
        }
        return fixMethodOrder.value().getComparator();
    }
}
