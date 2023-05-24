package org.junit.internal.runners;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/internal/runners/TestMethod.class
 */
@Deprecated
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/internal/runners/TestMethod.class */
public class TestMethod {
    private final Method method;
    private TestClass testClass;

    public TestMethod(Method method, TestClass testClass) {
        this.method = method;
        this.testClass = testClass;
    }

    public boolean isIgnored() {
        return this.method.getAnnotation(Ignore.class) != null;
    }

    public long getTimeout() {
        Test annotation = (Test) this.method.getAnnotation(Test.class);
        if (annotation == null) {
            return 0L;
        }
        long timeout = annotation.timeout();
        return timeout;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Class<? extends Throwable> getExpectedException() {
        Test annotation = (Test) this.method.getAnnotation(Test.class);
        if (annotation == null || annotation.expected() == Test.None.class) {
            return null;
        }
        return annotation.expected();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isUnexpected(Throwable exception) {
        return !getExpectedException().isAssignableFrom(exception.getClass());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean expectsException() {
        return getExpectedException() != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<Method> getBefores() {
        return this.testClass.getAnnotatedMethods(Before.class);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<Method> getAfters() {
        return this.testClass.getAnnotatedMethods(After.class);
    }

    public void invoke(Object test) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        this.method.invoke(test, new Object[0]);
    }
}
