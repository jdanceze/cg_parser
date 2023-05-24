package org.mockito.internal.runners;

import java.lang.reflect.InvocationTargetException;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.junit.MismatchReportingTestListener;
import org.mockito.internal.junit.MockitoTestListener;
import org.mockito.internal.junit.NoOpTestListener;
import org.mockito.internal.junit.StrictStubsRunnerTestListener;
import org.mockito.internal.runners.util.RunnerProvider;
import org.mockito.internal.runners.util.TestMethodsFinder;
import org.mockito.internal.util.Supplier;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/runners/RunnerFactory.class */
public class RunnerFactory {
    public InternalRunner create(Class<?> klass) throws InvocationTargetException {
        return create(klass, new Supplier<MockitoTestListener>() { // from class: org.mockito.internal.runners.RunnerFactory.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.mockito.internal.util.Supplier
            public MockitoTestListener get() {
                return new NoOpTestListener();
            }
        });
    }

    public InternalRunner createStrict(Class<?> klass) throws InvocationTargetException {
        return create(klass, new Supplier<MockitoTestListener>() { // from class: org.mockito.internal.runners.RunnerFactory.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.mockito.internal.util.Supplier
            public MockitoTestListener get() {
                return new MismatchReportingTestListener(Plugins.getMockitoLogger());
            }
        });
    }

    public InternalRunner createStrictStubs(Class<?> klass) throws InvocationTargetException {
        return create(klass, new Supplier<MockitoTestListener>() { // from class: org.mockito.internal.runners.RunnerFactory.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.mockito.internal.util.Supplier
            public MockitoTestListener get() {
                return new StrictStubsRunnerTestListener();
            }
        });
    }

    public InternalRunner create(Class<?> klass, Supplier<MockitoTestListener> listenerSupplier) throws InvocationTargetException {
        try {
            return new RunnerProvider().newInstance("org.mockito.internal.runners.DefaultInternalRunner", klass, listenerSupplier);
        } catch (InvocationTargetException e) {
            if (!TestMethodsFinder.hasTestMethods(klass)) {
                throw new MockitoException("\n\nNo tests found in " + klass.getSimpleName() + "\nIs the method annotated with @Test?\nIs the method public?\n", e);
            }
            throw e;
        } catch (Throwable t) {
            throw new MockitoException("\n\nMockitoRunner can only be used with JUnit 4.5 or higher.\nYou can upgrade your JUnit version or write your own Runner (please consider contributing your runner to the Mockito community).\nBear in mind that you can still enjoy all features of the framework without using runners (they are completely optional).\nIf you get this error despite using JUnit 4.5 or higher then please report this error to the mockito mailing list.\n", t);
        }
    }
}
