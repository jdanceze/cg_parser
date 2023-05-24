package org.powermock.modules.junit4.internal.impl.testcaseworkaround;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.runners.MethodValidator;
import org.junit.internal.runners.TestClass;
import org.powermock.modules.junit4.common.internal.impl.JUnitVersion;
import org.powermock.reflect.Whitebox;
import polyglot.main.Report;
/* loaded from: gencallgraphv3.jar:powermock-module-junit4-2.0.9.jar:org/powermock/modules/junit4/internal/impl/testcaseworkaround/PowerMockJUnit4MethodValidator.class */
public class PowerMockJUnit4MethodValidator extends MethodValidator {
    private static final String TEST_CLASS_FIELD;
    private static final String CLASS_UNDER_TEST_FIELD;
    private static final String ERRORS_FIELD;

    static {
        if (JUnitVersion.isGreaterThanOrEqualTo("4.12")) {
            TEST_CLASS_FIELD = "testClass";
            CLASS_UNDER_TEST_FIELD = "klass";
            ERRORS_FIELD = Report.errors;
            return;
        }
        TEST_CLASS_FIELD = "fTestClass";
        CLASS_UNDER_TEST_FIELD = "fClass";
        ERRORS_FIELD = "fErrors";
    }

    public PowerMockJUnit4MethodValidator(TestClass testClass) {
        super(testClass);
    }

    @Override // org.junit.internal.runners.MethodValidator
    public void validateInstanceMethods() {
        validateTestMethods(After.class, false);
        validateTestMethods(Before.class, false);
        validateTestMethods(Test.class, false);
        TestClass testClass = (TestClass) Whitebox.getInternalState(this, TEST_CLASS_FIELD, MethodValidator.class);
        Class<?> classUnderTest = (Class) Whitebox.getInternalState(testClass, CLASS_UNDER_TEST_FIELD);
        List<Throwable> fErrors = (List) Whitebox.getInternalState(this, ERRORS_FIELD, MethodValidator.class);
        List<Method> methods = getTestMethods(testClass, classUnderTest);
        if (methods.size() == 0) {
            fErrors.add(new Exception("No runnable methods"));
        }
    }

    private List<Method> getTestMethods(TestClass testClass, Class<?> classUnderTest) {
        List<Method> methods = testClass.getAnnotatedMethods(Test.class);
        if (methods.isEmpty()) {
            methods.addAll(getTestMethodsWithNoAnnotation(classUnderTest));
        }
        return methods;
    }

    private void validateTestMethods(Class<? extends Annotation> annotation, boolean isStatic) {
        List<Method> methods;
        TestClass testClass = (TestClass) Whitebox.getInternalState(this, TEST_CLASS_FIELD, MethodValidator.class);
        Class<?> classUnderTest = (Class) Whitebox.getInternalState(testClass, CLASS_UNDER_TEST_FIELD);
        if (TestCase.class.equals(classUnderTest.getSuperclass()) && !isStatic) {
            methods = getTestMethodsWithNoAnnotation(classUnderTest);
        } else {
            methods = testClass.getAnnotatedMethods(annotation);
        }
        List<Throwable> fErrors = (List) Whitebox.getInternalState(this, ERRORS_FIELD, MethodValidator.class);
        for (Method each : methods) {
            if (Modifier.isStatic(each.getModifiers()) != isStatic) {
                String state = isStatic ? "should" : "should not";
                fErrors.add(new Exception("Method " + each.getName() + "() " + state + " be static"));
            }
            if (!Modifier.isPublic(each.getDeclaringClass().getModifiers())) {
                fErrors.add(new Exception("Class " + each.getDeclaringClass().getName() + " should be public"));
            }
            if (!Modifier.isPublic(each.getModifiers())) {
                fErrors.add(new Exception("Method " + each.getName() + " should be public"));
            }
            if (each.getReturnType() != Void.TYPE) {
                fErrors.add(new Exception("Method " + each.getName() + " should be void"));
            }
            if (each.getParameterTypes().length != 0) {
                fErrors.add(new Exception("Method " + each.getName() + " should have no parameters"));
            }
        }
    }

    private List<Method> getTestMethodsWithNoAnnotation(Class<?> testClass) {
        List<Method> potentialTestMethods = new LinkedList<>();
        Method[] methods = testClass.getMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("test")) {
                potentialTestMethods.add(method);
            }
        }
        return potentialTestMethods;
    }
}
