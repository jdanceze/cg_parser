package org.powermock.modules.junit4.common.internal.impl;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import junit.framework.TestCase;
import org.junit.Test;
/* loaded from: gencallgraphv3.jar:powermock-module-junit4-common-2.0.9.jar:org/powermock/modules/junit4/common/internal/impl/JUnit4TestMethodChecker.class */
public class JUnit4TestMethodChecker {
    private final Class<?> testClass;
    private final Method potentialTestMethod;

    public JUnit4TestMethodChecker(Class<?> testClass, Method potentialTestMethod) {
        this.testClass = testClass;
        this.potentialTestMethod = potentialTestMethod;
    }

    public boolean isTestMethod() {
        return isJUnit3TestMethod() || isJUnit4TestMethod();
    }

    protected boolean isJUnit4TestMethod() {
        return this.potentialTestMethod.isAnnotationPresent(Test.class);
    }

    protected boolean isJUnit3TestMethod() {
        return this.potentialTestMethod.getName().startsWith("test") && Modifier.isPublic(this.potentialTestMethod.getModifiers()) && this.potentialTestMethod.getReturnType().equals(Void.TYPE) && TestCase.class.isAssignableFrom(this.testClass);
    }
}
