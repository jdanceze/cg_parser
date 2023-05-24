package org.powermock.reflect.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.powermock.reflect.internal.comparator.ComparatorFactory;
/* loaded from: gencallgraphv3.jar:powermock-reflect-2.0.9.jar:org/powermock/reflect/internal/CandidateConstructorSearcher.class */
class CandidateConstructorSearcher<T> {
    private final Class<T> classThatContainsTheConstructorToTest;
    private final Class<?>[] argumentTypes;

    public CandidateConstructorSearcher(Class<T> classThatContainsTheConstructorToTest, Class<?>[] argumentTypes) {
        this.classThatContainsTheConstructorToTest = classThatContainsTheConstructorToTest;
        this.argumentTypes = argumentTypes;
    }

    public java.lang.reflect.Constructor<T> findConstructor() {
        java.lang.reflect.Constructor<T>[] constructors = getConstructors();
        if (constructors.length == 0) {
            return null;
        }
        if (constructors.length == 1) {
            return constructors[0];
        }
        return findBestCandidate(constructors);
    }

    private java.lang.reflect.Constructor<T> findBestCandidate(java.lang.reflect.Constructor<T>[] constructors) {
        Arrays.sort(constructors, ComparatorFactory.createConstructorComparator());
        return constructors[0];
    }

    private java.lang.reflect.Constructor<T>[] getConstructors() {
        try {
            java.lang.reflect.Constructor<?>[] declaredConstructors = this.classThatContainsTheConstructorToTest.getDeclaredConstructors();
            List<java.lang.reflect.Constructor<?>> constructors = new ArrayList<>();
            for (java.lang.reflect.Constructor<?> constructor : declaredConstructors) {
                if (argumentsApplied(constructor)) {
                    constructors.add(constructor);
                }
            }
            return (java.lang.reflect.Constructor[]) constructors.toArray(new java.lang.reflect.Constructor[constructors.size()]);
        } catch (Exception e) {
            return new java.lang.reflect.Constructor[0];
        }
    }

    private boolean argumentsApplied(java.lang.reflect.Constructor<?> constructor) {
        Class<?>[] constructorArgumentTypes = constructor.getParameterTypes();
        if (constructorArgumentTypes.length != this.argumentTypes.length) {
            return false;
        }
        for (int index = 0; index < this.argumentTypes.length; index++) {
            if (!constructorArgumentTypes[index].isAssignableFrom(this.argumentTypes[index])) {
                return false;
            }
        }
        return true;
    }
}
