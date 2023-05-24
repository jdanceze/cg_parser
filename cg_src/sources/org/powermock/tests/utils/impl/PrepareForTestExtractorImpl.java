package org.powermock.tests.utils.impl;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;
import org.powermock.core.IndicateReloadClass;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/tests/utils/impl/PrepareForTestExtractorImpl.class */
public class PrepareForTestExtractorImpl extends AbstractTestClassExtractor {
    public PrepareForTestExtractorImpl() {
        this(false);
    }

    public PrepareForTestExtractorImpl(boolean includeMethods) {
        super(includeMethods);
    }

    @Override // org.powermock.tests.utils.impl.AbstractTestClassExtractor
    protected String[] getClassesToModify(AnnotatedElement element) {
        Set<String> all = new LinkedHashSet<>();
        addTestCase(all, element);
        PrepareForTest prepareForTestAnnotation = (PrepareForTest) element.getAnnotation(PrepareForTest.class);
        PrepareOnlyThisForTest prepareOnlyThisForTestAnnotation = (PrepareOnlyThisForTest) element.getAnnotation(PrepareOnlyThisForTest.class);
        boolean prepareForTestAnnotationPresent = prepareForTestAnnotation != null;
        boolean prepareOnlyThisForTestAnnotationPresent = prepareOnlyThisForTestAnnotation != null;
        if (!prepareForTestAnnotationPresent && !prepareOnlyThisForTestAnnotationPresent) {
            return null;
        }
        if (prepareForTestAnnotationPresent) {
            Class<?>[] classesToMock = prepareForTestAnnotation.value();
            for (Class<?> classToMock : classesToMock) {
                if (!classToMock.equals(IndicateReloadClass.class)) {
                    addClassHierarchy(all, classToMock);
                }
            }
            addFullyQualifiedNames(all, prepareForTestAnnotation);
        }
        if (prepareOnlyThisForTestAnnotationPresent) {
            Class<?>[] classesToMock2 = prepareOnlyThisForTestAnnotation.value();
            for (Class<?> classToMock2 : classesToMock2) {
                if (!classToMock2.equals(IndicateReloadClass.class)) {
                    all.add(classToMock2.getName());
                }
            }
            addFullyQualifiedNames(all, prepareOnlyThisForTestAnnotation);
        }
        return (String[]) all.toArray(new String[all.size()]);
    }

    private void addTestCase(Set<String> all, AnnotatedElement element) {
        Class<?> testClass = null;
        if (element instanceof Class) {
            testClass = (Class) element;
        } else if (element instanceof Method) {
            testClass = ((Method) element).getDeclaringClass();
        }
        addClassHierarchy(all, testClass);
    }

    private void addFullyQualifiedNames(Set<String> all, PrepareForTest annotation) {
        String[] fullyQualifiedNames = annotation.fullyQualifiedNames();
        addFullyQualifiedNames(all, fullyQualifiedNames);
    }

    private void addFullyQualifiedNames(Set<String> all, PrepareOnlyThisForTest annotation) {
        String[] fullyQualifiedNames = annotation.fullyQualifiedNames();
        addFullyQualifiedNames(all, fullyQualifiedNames);
    }

    private void addFullyQualifiedNames(Set<String> all, String[] fullyQualifiedNames) {
        for (String string : fullyQualifiedNames) {
            if (!"".equals(string)) {
                all.add(string);
            }
        }
    }

    private void addClassHierarchy(Set<String> all, Class<?> classToMock) {
        while (classToMock != null && !classToMock.equals(Object.class)) {
            addInnerClassesAndInterfaces(all, classToMock);
            all.add(classToMock.getName());
            classToMock = classToMock.getSuperclass();
        }
    }

    private void addInnerClassesAndInterfaces(Set<String> all, Class<?> classToMock) {
        Class<?>[] declaredClasses = classToMock.getDeclaredClasses();
        for (Class<?> innerClass : declaredClasses) {
            all.add(innerClass.getName());
        }
    }
}
