package org.powermock.modules.junit4.internal.impl;

import java.lang.reflect.Method;
import java.util.LinkedList;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runner.notification.StoppedByUserException;
import org.powermock.core.testlisteners.GlobalNotificationBuildSupport;
import org.powermock.reflect.Whitebox;
import org.powermock.tests.utils.PowerMockTestNotifier;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:powermock-module-junit4-2.0.9.jar:org/powermock/modules/junit4/internal/impl/PowerMockRunNotifier.class */
public class PowerMockRunNotifier extends RunNotifier implements GlobalNotificationBuildSupport.Callback {
    private final RunNotifier junitRunNotifier;
    private final PowerMockTestNotifier powerMockTestNotifier;
    private final Method[] testMethods;
    private Class<?> suiteClass = null;
    private final Thread motherThread = Thread.currentThread();
    private final LinkedList<Object> pendingTestInstancesOnMotherThread = new LinkedList<>();
    private final ThreadLocal<NotificationBuilder> notificationBuilder = new ThreadLocal<NotificationBuilder>() { // from class: org.powermock.modules.junit4.internal.impl.PowerMockRunNotifier.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.lang.ThreadLocal
        public NotificationBuilder initialValue() {
            return new NotificationBuilder((Method[]) PowerMockRunNotifier.this.testMethods.clone(), PowerMockRunNotifier.this.powerMockTestNotifier, PowerMockRunNotifier.this.pendingTestInstancesOnMotherThread);
        }
    };

    /* JADX INFO: Access modifiers changed from: package-private */
    public PowerMockRunNotifier(RunNotifier junitRunNotifier, PowerMockTestNotifier powerMockTestNotifier, Method[] testMethods) {
        this.junitRunNotifier = junitRunNotifier;
        this.powerMockTestNotifier = powerMockTestNotifier;
        this.testMethods = testMethods;
    }

    Class<?> getSuiteClass() {
        return this.suiteClass;
    }

    @Override // org.powermock.core.testlisteners.GlobalNotificationBuildSupport.Callback
    public void suiteClassInitiated(Class<?> testClass) {
        this.suiteClass = testClass;
        this.notificationBuilder.get().testSuiteStarted(testClass);
    }

    @Override // org.powermock.core.testlisteners.GlobalNotificationBuildSupport.Callback
    public void testInstanceCreated(Object testInstance) {
        if (Thread.currentThread() == this.motherThread) {
            this.pendingTestInstancesOnMotherThread.add(testInstance);
        }
        this.notificationBuilder.get().testInstanceCreated(testInstance);
    }

    @Override // org.junit.runner.notification.RunNotifier
    public void addListener(RunListener listener) {
        invoke("addListener", listener);
    }

    @Override // org.junit.runner.notification.RunNotifier
    public void removeListener(RunListener listener) {
        invoke("removeListener", listener);
    }

    @Override // org.junit.runner.notification.RunNotifier
    public void fireTestRunStarted(Description description) {
        invoke("fireTestRunStarted", description);
    }

    @Override // org.junit.runner.notification.RunNotifier
    public void fireTestRunFinished(Result result) {
        invoke("fireTestRunFinished", result);
    }

    @Override // org.junit.runner.notification.RunNotifier
    public void fireTestStarted(Description description) throws StoppedByUserException {
        invoke("fireTestStarted", description);
        this.notificationBuilder.get().testStartHasBeenFired(description);
    }

    @Override // org.junit.runner.notification.RunNotifier
    public void fireTestFailure(Failure failure) {
        this.notificationBuilder.get().failure(failure);
        invoke("fireTestFailure", failure);
    }

    @Override // org.junit.runner.notification.RunNotifier
    public void fireTestAssumptionFailed(Failure failure) {
        this.notificationBuilder.get().assumptionFailed(failure.getDescription());
        invoke("fireTestAssumptionFailed", failure);
    }

    @Override // org.junit.runner.notification.RunNotifier
    public void fireTestIgnored(Description description) {
        this.notificationBuilder.get().testIgnored(description);
        invoke("fireTestIgnored", description);
    }

    @Override // org.junit.runner.notification.RunNotifier
    public void fireTestFinished(Description description) {
        try {
            this.notificationBuilder.get().testFinished(description);
            invoke("fireTestFinished", description);
        } catch (Throwable failure) {
            fireTestFailure(new Failure(description, failure));
        }
    }

    @Override // org.junit.runner.notification.RunNotifier
    public void pleaseStop() {
        invoke("pleaseStop", new Object[0]);
    }

    @Override // org.junit.runner.notification.RunNotifier
    public void addFirstListener(RunListener listener) {
        invoke("addFirstListener", listener);
    }

    private void invoke(String methodName, Object... args) {
        try {
            Whitebox.invokeMethod(this.junitRunNotifier, methodName, args);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
