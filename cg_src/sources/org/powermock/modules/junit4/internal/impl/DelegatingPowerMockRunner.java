package org.powermock.modules.junit4.internal.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import org.junit.Test;
import org.junit.experimental.theories.Theory;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.RunNotifier;
import org.powermock.core.spi.PowerMockTestListener;
import org.powermock.core.testlisteners.GlobalNotificationBuildSupport;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.powermock.modules.junit4.common.internal.PowerMockJUnitRunnerDelegate;
import org.powermock.modules.junit4.common.internal.impl.JUnitVersion;
import org.powermock.reflect.Whitebox;
import org.powermock.reflect.exceptions.ConstructorNotFoundException;
import org.powermock.tests.utils.PowerMockTestNotifier;
import org.powermock.tests.utils.impl.PowerMockTestNotifierImpl;
/* loaded from: gencallgraphv3.jar:powermock-module-junit4-2.0.9.jar:org/powermock/modules/junit4/internal/impl/DelegatingPowerMockRunner.class */
public class DelegatingPowerMockRunner extends Runner implements PowerMockJUnitRunnerDelegate, Filterable {
    private final String testClassName;
    private final Runner delegate;
    private final ClassLoader testClassLoader;
    private final Method[] testMethods;
    private final PowerMockTestNotifier powerMockTestNotifier;

    public DelegatingPowerMockRunner(Class<?> klass) throws Throwable {
        this(klass, null);
    }

    public DelegatingPowerMockRunner(Class<?> klass, String[] methodsToRun) throws Throwable {
        this(klass, methodsToRun, null);
    }

    public DelegatingPowerMockRunner(Class<?> klass, String[] methodsToRun, PowerMockTestListener[] listeners) throws Exception {
        this.testClassName = klass.getName();
        this.delegate = createDelegate(klass);
        this.testClassLoader = klass.getClassLoader();
        this.testMethods = determineTestMethods(klass, methodsToRun);
        this.powerMockTestNotifier = new PowerMockTestNotifierImpl(listeners == null ? new PowerMockTestListener[0] : listeners);
    }

    private static Method[] determineTestMethods(Class<?> testClass, String[] testMethodNames) {
        Method[] methods;
        List<Method> testMethods = new ArrayList<>();
        for (Method m : testClass.getMethods()) {
            if (m.isAnnotationPresent(Test.class) || m.isAnnotationPresent(Theory.class)) {
                testMethods.add(m);
            }
        }
        if (testMethods.isEmpty()) {
            for (String testMethodName : testMethodNames) {
                try {
                    testMethods.add(testClass.getMethod(testMethodName, new Class[0]));
                } catch (NoSuchMethodException ignore) {
                    System.err.println(ignore.getMessage());
                }
            }
        }
        return (Method[]) testMethods.toArray(new Method[testMethods.size()]);
    }

    private static Runner createDelegate(final Class<?> testClass) throws Exception {
        return (Runner) withContextClassLoader(testClass.getClassLoader(), new Callable<Runner>() { // from class: org.powermock.modules.junit4.internal.impl.DelegatingPowerMockRunner.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Runner call() throws Exception {
                try {
                    return (Runner) Whitebox.invokeConstructor(testClass.isAnnotationPresent(PowerMockRunnerDelegate.class) ? ((PowerMockRunnerDelegate) testClass.getAnnotation(PowerMockRunnerDelegate.class)).value() : PowerMockRunnerDelegate.DefaultJUnitRunner.class, new Class[]{Class.class}, new Object[]{testClass});
                } catch (ConstructorNotFoundException rootProblem) {
                    if (testClass.isAnnotationPresent(PowerMockRunnerDelegate.class) && JUnitVersion.isGreaterThanOrEqualTo("4.5")) {
                        try {
                            return (Runner) Whitebox.invokeConstructor(((PowerMockRunnerDelegate) testClass.getAnnotation(PowerMockRunnerDelegate.class)).value(), PowerMockRunnerDelegate.SinceJUnit_4_5.runnerAlternativeConstructorParams(), new Object[]{testClass, PowerMockRunnerDelegate.SinceJUnit_4_5.newRunnerBuilder()});
                        } catch (ConstructorNotFoundException e) {
                            throw rootProblem;
                        }
                    }
                    throw rootProblem;
                }
            }
        });
    }

    private static <T> T withContextClassLoader(ClassLoader loader, Callable<T> callable) throws Exception {
        ClassLoader originalClassLoaderBackup = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(loader);
            T call = callable.call();
            Thread.currentThread().setContextClassLoader(originalClassLoaderBackup);
            return call;
        } catch (Throwable th) {
            Thread.currentThread().setContextClassLoader(originalClassLoaderBackup);
            throw th;
        }
    }

    @Override // org.junit.runner.Runner
    public void run(final RunNotifier notifier) {
        try {
            withContextClassLoader(this.testClassLoader, new Callable<Void>() { // from class: org.powermock.modules.junit4.internal.impl.DelegatingPowerMockRunner.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public Void call() {
                    PowerMockRunNotifier powerNotifier = new PowerMockRunNotifier(notifier, DelegatingPowerMockRunner.this.powerMockTestNotifier, DelegatingPowerMockRunner.this.testMethods);
                    try {
                        GlobalNotificationBuildSupport.prepareTestSuite(DelegatingPowerMockRunner.this.testClassName, powerNotifier);
                        DelegatingPowerMockRunner.this.delegate.run(powerNotifier);
                        return null;
                    } finally {
                        GlobalNotificationBuildSupport.closePendingTestSuites(powerNotifier);
                    }
                }
            });
        } catch (Exception cannotHappen) {
            throw new Error(cannotHappen);
        }
    }

    @Override // org.junit.runner.Runner, org.junit.runner.Describable
    public Description getDescription() {
        return this.delegate.getDescription();
    }

    @Override // org.powermock.modules.junit4.common.internal.PowerMockJUnitRunnerDelegate
    public int getTestCount() {
        return this.delegate.testCount();
    }

    @Override // org.powermock.modules.junit4.common.internal.PowerMockJUnitRunnerDelegate
    public Class<?> getTestClass() {
        return getDescription().getTestClass();
    }

    @Override // org.junit.runner.manipulation.Filterable
    public void filter(Filter filter) throws NoTestsRemainException {
        if (this.delegate instanceof Filterable) {
            ((Filterable) this.delegate).filter(filter);
        }
    }
}
