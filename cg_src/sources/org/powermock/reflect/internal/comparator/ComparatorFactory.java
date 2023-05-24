package org.powermock.reflect.internal.comparator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Comparator;
/* loaded from: gencallgraphv3.jar:powermock-reflect-2.0.9.jar:org/powermock/reflect/internal/comparator/ComparatorFactory.class */
public class ComparatorFactory {
    private ComparatorFactory() {
    }

    public static Comparator<Constructor> createConstructorComparator() {
        return new ConstructorComparator(new ParametersComparator());
    }

    public static Comparator<Method> createMethodComparator() {
        return new MethodComparator(new ParametersComparator());
    }

    /* loaded from: gencallgraphv3.jar:powermock-reflect-2.0.9.jar:org/powermock/reflect/internal/comparator/ComparatorFactory$ConstructorComparator.class */
    public static class ConstructorComparator implements Comparator<Constructor> {
        private final ParametersComparator parametersComparator;

        private ConstructorComparator(ParametersComparator parametersComparator) {
            this.parametersComparator = parametersComparator;
        }

        @Override // java.util.Comparator
        public int compare(Constructor constructor1, Constructor constructor2) {
            Class<?>[] parameters1 = constructor1.getParameterTypes();
            Class<?>[] parameters2 = constructor2.getParameterTypes();
            return this.parametersComparator.compare((Class[]) parameters1, (Class[]) parameters2);
        }
    }

    /* loaded from: gencallgraphv3.jar:powermock-reflect-2.0.9.jar:org/powermock/reflect/internal/comparator/ComparatorFactory$MethodComparator.class */
    public static class MethodComparator implements Comparator<Method> {
        private final ParametersComparator parametersComparator;

        private MethodComparator(ParametersComparator parametersComparator) {
            this.parametersComparator = parametersComparator;
        }

        @Override // java.util.Comparator
        public int compare(Method m1, Method m2) {
            Class<?>[] typesMethod1 = m1.getParameterTypes();
            Class<?>[] typesMethod2 = m2.getParameterTypes();
            return this.parametersComparator.compare((Class[]) typesMethod1, (Class[]) typesMethod2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:powermock-reflect-2.0.9.jar:org/powermock/reflect/internal/comparator/ComparatorFactory$ParametersComparator.class */
    public static class ParametersComparator implements Comparator<Class[]> {
        private ParametersComparator() {
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.Comparator
        public int compare(Class[] params1, Class[] params2) {
            int size = params1.length;
            for (int i = 0; i < size; i++) {
                Class cls = params1[i];
                Class cls2 = params2[i];
                if (!cls.equals(cls2)) {
                    if (cls.isAssignableFrom(cls2)) {
                        if (!cls.isArray() && cls2.isArray()) {
                            return -1;
                        }
                        return 1;
                    } else if (cls.isArray() && !cls2.isArray()) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            }
            return 0;
        }
    }
}
