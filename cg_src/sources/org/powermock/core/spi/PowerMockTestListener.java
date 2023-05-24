package org.powermock.core.spi;

import java.lang.reflect.Method;
import org.powermock.core.spi.testresult.TestMethodResult;
import org.powermock.core.spi.testresult.TestSuiteResult;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/spi/PowerMockTestListener.class */
public interface PowerMockTestListener {
    void beforeTestSuiteStarted(Class<?> cls, Method[] methodArr) throws Exception;

    void beforeTestMethod(Object obj, Method method, Object[] objArr) throws Exception;

    void afterTestMethod(Object obj, Method method, Object[] objArr, TestMethodResult testMethodResult) throws Exception;

    void afterTestSuiteEnded(Class<?> cls, Method[] methodArr, TestSuiteResult testSuiteResult) throws Exception;
}
