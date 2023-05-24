package org.powermock.core.spi.support;

import java.lang.reflect.Method;
import org.powermock.core.spi.PowerMockTestListener;
import org.powermock.core.spi.testresult.TestMethodResult;
import org.powermock.core.spi.testresult.TestSuiteResult;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/spi/support/AbstractPowerMockTestListenerBase.class */
public class AbstractPowerMockTestListenerBase implements PowerMockTestListener {
    @Override // org.powermock.core.spi.PowerMockTestListener
    public void afterTestMethod(Object testInstance, Method method, Object[] arguments, TestMethodResult testResult) throws Exception {
    }

    @Override // org.powermock.core.spi.PowerMockTestListener
    public void beforeTestMethod(Object testInstance, Method method, Object[] arguments) throws Exception {
    }

    @Override // org.powermock.core.spi.PowerMockTestListener
    public void beforeTestSuiteStarted(Class<?> testClass, Method[] testMethods) throws Exception {
    }

    @Override // org.powermock.core.spi.PowerMockTestListener
    public void afterTestSuiteEnded(Class<?> testClass, Method[] methods, TestSuiteResult testResult) throws Exception {
    }
}
