package org.powermock.tests.utils;

import java.lang.reflect.Method;
import org.powermock.core.spi.testresult.TestMethodResult;
import org.powermock.core.spi.testresult.TestSuiteResult;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/tests/utils/PowerMockTestNotifier.class */
public interface PowerMockTestNotifier {
    void notifyBeforeTestMethod(Object obj, Method method, Object[] objArr);

    void notifyAfterTestMethod(Object obj, Method method, Object[] objArr, TestMethodResult testMethodResult);

    void notifyAfterTestMethod(boolean z);

    void notifyBeforeTestSuiteStarted(Class<?> cls, Method[] methodArr);

    void notifyAfterTestSuiteEnded(Class<?> cls, Method[] methodArr, TestSuiteResult testSuiteResult);
}
