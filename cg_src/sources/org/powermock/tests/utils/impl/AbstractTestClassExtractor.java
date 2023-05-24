package org.powermock.tests.utils.impl;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.powermock.tests.utils.TestClassesExtractor;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/tests/utils/impl/AbstractTestClassExtractor.class */
public abstract class AbstractTestClassExtractor implements TestClassesExtractor {
    protected final boolean includeMethods;

    protected abstract String[] getClassesToModify(AnnotatedElement annotatedElement);

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractTestClassExtractor() {
        this(false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractTestClassExtractor(boolean includeMethods) {
        this.includeMethods = includeMethods;
    }

    @Override // org.powermock.tests.utils.TestClassesExtractor
    public final String[] getTestClasses(AnnotatedElement element) {
        Set<String> classesToPrepareForTest = new HashSet<>();
        if (element instanceof Class) {
            extractClassesFromTestClass((Class) element, classesToPrepareForTest);
        } else {
            extractClassesAndAddThemToList(element, classesToPrepareForTest);
        }
        return (String[]) classesToPrepareForTest.toArray(new String[classesToPrepareForTest.size()]);
    }

    private void extractClassesFromTestClass(Class<?> element, Set<String> classesToPrepareForTest) {
        Class<?> cls = element;
        while (true) {
            Class<?> classToInvestigate = cls;
            if (classToInvestigate != null && !classToInvestigate.equals(Object.class)) {
                extractClassesAndAddThemToList(classToInvestigate, classesToPrepareForTest);
                if (this.includeMethods) {
                    classesToPrepareForTest.addAll(lookOverMethods(classToInvestigate));
                }
                cls = classToInvestigate.getSuperclass();
            } else {
                return;
            }
        }
    }

    private Collection<String> lookOverMethods(Class<?> classToInvestigate) {
        Method[] methods;
        Set<String> classesToPrepareForTest = new HashSet<>();
        for (Method method : classToInvestigate.getMethods()) {
            extractClassesAndAddThemToList(method, classesToPrepareForTest);
        }
        return classesToPrepareForTest;
    }

    private void extractClassesAndAddThemToList(AnnotatedElement elementToExtractClassFrom, Set<String> classesToPrepareForTest) {
        String[] classesToModify = getClassesToModify(elementToExtractClassFrom);
        if (classesToModify != null) {
            Collections.addAll(classesToPrepareForTest, classesToModify);
        }
    }

    @Override // org.powermock.tests.utils.TestClassesExtractor
    public boolean isPrepared(AnnotatedElement element, String fullyQualifiedClassName) {
        if (fullyQualifiedClassName == null) {
            throw new IllegalArgumentException("fullyQualifiedClassName cannot be null.");
        }
        String[] testClasses = getTestClasses(element);
        for (String className : testClasses) {
            if (className.equals(fullyQualifiedClassName)) {
                return true;
            }
        }
        return false;
    }
}
