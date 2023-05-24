package org.powermock.modules.junit4.common.internal.impl;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.powermock.core.MockRepository;
import org.powermock.reflect.Whitebox;
import org.powermock.tests.utils.PowerMockTestNotifier;
/* loaded from: gencallgraphv3.jar:powermock-module-junit4-common-2.0.9.jar:org/powermock/modules/junit4/common/internal/impl/PowerMockJUnit4RunListener.class */
public class PowerMockJUnit4RunListener extends RunListener {
    private final ClassLoader mockClassLoader;
    private int failureCount;
    private int ignoreCount;
    private boolean currentTestSuccessful = true;
    private final PowerMockTestNotifier powerMockTestNotifier;

    public PowerMockJUnit4RunListener(ClassLoader mockClassLoader, PowerMockTestNotifier powerMockTestNotifier) {
        this.mockClassLoader = mockClassLoader;
        this.powerMockTestNotifier = powerMockTestNotifier;
    }

    @Override // org.junit.runner.notification.RunListener
    public void testFinished(Description description1) throws Exception {
        Class<?> mockRepositoryClass = this.mockClassLoader.loadClass(MockRepository.class.getName());
        try {
            notifyListenersOfTestResult();
            Whitebox.invokeMethod(mockRepositoryClass, "clear", new Object[0]);
        } catch (Throwable th) {
            Whitebox.invokeMethod(mockRepositoryClass, "clear", new Object[0]);
            throw th;
        }
    }

    public int getFailureCount() {
        return this.failureCount;
    }

    public int getIgnoreCount() {
        return this.ignoreCount;
    }

    @Override // org.junit.runner.notification.RunListener
    public void testFailure(Failure failure) throws Exception {
        this.currentTestSuccessful = false;
        this.failureCount++;
    }

    @Override // org.junit.runner.notification.RunListener
    public void testIgnored(Description description) throws Exception {
        this.ignoreCount++;
    }

    private void notifyListenersOfTestResult() {
        try {
            this.powerMockTestNotifier.notifyAfterTestMethod(this.currentTestSuccessful);
        } finally {
            this.currentTestSuccessful = true;
        }
    }
}
