package org.powermock.tests.utils.impl;

import java.lang.reflect.Method;
import org.powermock.core.MockRepository;
import org.powermock.core.spi.PowerMockTestListener;
import org.powermock.core.spi.testresult.Result;
import org.powermock.core.spi.testresult.TestMethodResult;
import org.powermock.core.spi.testresult.TestSuiteResult;
import org.powermock.core.spi.testresult.impl.TestMethodResultImpl;
import org.powermock.tests.utils.Keys;
import org.powermock.tests.utils.PowerMockTestNotifier;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/tests/utils/impl/PowerMockTestNotifierImpl.class */
public class PowerMockTestNotifierImpl implements PowerMockTestNotifier {
    private static final String ERROR_MESSAGE_TEMPLATE = "Invoking the %s method on PowerMock test listener %s failed.";
    private final PowerMockTestListener[] powerMockTestListeners;

    public PowerMockTestNotifierImpl(PowerMockTestListener[] powerMockTestListeners) {
        if (powerMockTestListeners == null) {
            this.powerMockTestListeners = new PowerMockTestListener[0];
        } else {
            this.powerMockTestListeners = powerMockTestListeners;
        }
    }

    @Override // org.powermock.tests.utils.PowerMockTestNotifier
    public void notifyAfterTestMethod(Object testInstance, Method method, Object[] arguments, TestMethodResult testResult) {
        PowerMockTestListener[] powerMockTestListenerArr;
        for (PowerMockTestListener testListener : this.powerMockTestListeners) {
            try {
                testListener.afterTestMethod(testInstance, method, arguments, testResult);
            } catch (Exception e) {
                throw new RuntimeException(String.format(ERROR_MESSAGE_TEMPLATE, "afterTestMethod", testListener), e);
            }
        }
    }

    @Override // org.powermock.tests.utils.PowerMockTestNotifier
    public void notifyAfterTestSuiteEnded(Class<?> testClass, Method[] methods, TestSuiteResult testResult) {
        PowerMockTestListener[] powerMockTestListenerArr;
        for (PowerMockTestListener powerMockTestListener : this.powerMockTestListeners) {
            try {
                powerMockTestListener.afterTestSuiteEnded(testClass, methods, testResult);
            } catch (Exception e) {
                throw new RuntimeException(String.format(ERROR_MESSAGE_TEMPLATE, "afterTestSuiteEnded", powerMockTestListener), e);
            }
        }
    }

    @Override // org.powermock.tests.utils.PowerMockTestNotifier
    public void notifyBeforeTestMethod(Object testInstance, Method testMethod, Object[] arguments) {
        PowerMockTestListener[] powerMockTestListenerArr;
        MockRepository.putAdditionalState(Keys.CURRENT_TEST_INSTANCE, testInstance);
        MockRepository.putAdditionalState(Keys.CURRENT_TEST_METHOD, testMethod);
        MockRepository.putAdditionalState(Keys.CURRENT_TEST_METHOD_ARGUMENTS, arguments);
        for (PowerMockTestListener testListener : this.powerMockTestListeners) {
            try {
                testListener.beforeTestMethod(testInstance, testMethod, arguments);
            } catch (Exception e) {
                throw new RuntimeException(String.format(ERROR_MESSAGE_TEMPLATE, "beforeTestMethod", testListener), e);
            }
        }
    }

    @Override // org.powermock.tests.utils.PowerMockTestNotifier
    public void notifyBeforeTestSuiteStarted(Class<?> testClass, Method[] testMethods) {
        PowerMockTestListener[] powerMockTestListenerArr;
        for (PowerMockTestListener powerMockTestListener : this.powerMockTestListeners) {
            try {
                powerMockTestListener.beforeTestSuiteStarted(testClass, testMethods);
            } catch (Exception e) {
                throw new RuntimeException(String.format(ERROR_MESSAGE_TEMPLATE, "beforeTestSuiteStarted", powerMockTestListener), e);
            }
        }
    }

    @Override // org.powermock.tests.utils.PowerMockTestNotifier
    public void notifyAfterTestMethod(boolean successful) {
        Object test = MockRepository.getAdditionalState(Keys.CURRENT_TEST_INSTANCE);
        Method testMethod = (Method) MockRepository.getAdditionalState(Keys.CURRENT_TEST_METHOD);
        Object[] testArguments = (Object[]) MockRepository.getAdditionalState(Keys.CURRENT_TEST_METHOD_ARGUMENTS);
        TestMethodResult testResult = new TestMethodResultImpl(successful ? Result.SUCCESSFUL : Result.FAILED);
        notifyAfterTestMethod(test, testMethod, testArguments, testResult);
    }
}
