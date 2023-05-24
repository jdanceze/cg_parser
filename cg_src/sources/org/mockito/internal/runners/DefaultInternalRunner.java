package org.mockito.internal.runners;

import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.junit.DefaultTestFinishedEvent;
import org.mockito.internal.junit.MockitoTestListener;
import org.mockito.internal.util.Supplier;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/runners/DefaultInternalRunner.class */
public class DefaultInternalRunner implements InternalRunner {
    private final BlockJUnit4ClassRunner runner;

    public DefaultInternalRunner(Class<?> testClass, Supplier<MockitoTestListener> listenerSupplier) throws InitializationError {
        this.runner = new AnonymousClass1(testClass, listenerSupplier);
    }

    /* renamed from: org.mockito.internal.runners.DefaultInternalRunner$1  reason: invalid class name */
    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/runners/DefaultInternalRunner$1.class */
    class AnonymousClass1 extends BlockJUnit4ClassRunner {
        public Object target;
        private MockitoTestListener mockitoTestListener;
        final /* synthetic */ Supplier val$listenerSupplier;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(Class cls, Supplier supplier) throws InitializationError {
            super(cls);
            this.val$listenerSupplier = supplier;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.junit.runners.BlockJUnit4ClassRunner
        public Statement withBefores(FrameworkMethod method, final Object target, Statement statement) {
            this.target = target;
            final Statement base = super.withBefores(method, target, statement);
            return new Statement() { // from class: org.mockito.internal.runners.DefaultInternalRunner.1.1
                @Override // org.junit.runners.model.Statement
                public void evaluate() throws Throwable {
                    AutoCloseable closeable;
                    if (AnonymousClass1.this.mockitoTestListener == null) {
                        AnonymousClass1.this.mockitoTestListener = (MockitoTestListener) AnonymousClass1.this.val$listenerSupplier.get();
                        Mockito.framework().addListener(AnonymousClass1.this.mockitoTestListener);
                        closeable = MockitoAnnotations.openMocks(target);
                    } else {
                        closeable = null;
                    }
                    try {
                        base.evaluate();
                        if (closeable != null) {
                            closeable.close();
                        }
                    } catch (Throwable th) {
                        if (closeable != null) {
                            closeable.close();
                        }
                        throw th;
                    }
                }
            };
        }

        @Override // org.junit.runners.ParentRunner, org.junit.runner.Runner
        public void run(final RunNotifier notifier) {
            RunListener listener = new RunListener() { // from class: org.mockito.internal.runners.DefaultInternalRunner.1.2
                Throwable failure;

                @Override // org.junit.runner.notification.RunListener
                public void testFailure(Failure failure) throws Exception {
                    this.failure = failure.getException();
                }

                @Override // org.junit.runner.notification.RunListener
                public void testFinished(Description description) throws Exception {
                    try {
                        if (AnonymousClass1.this.mockitoTestListener != null) {
                            Mockito.framework().removeListener(AnonymousClass1.this.mockitoTestListener);
                            AnonymousClass1.this.mockitoTestListener.testFinished(new DefaultTestFinishedEvent(AnonymousClass1.this.target, description.getMethodName(), this.failure));
                            AnonymousClass1.this.mockitoTestListener = null;
                        }
                        Mockito.validateMockitoUsage();
                    } catch (Throwable t) {
                        notifier.fireTestFailure(new Failure(description, t));
                    }
                }
            };
            notifier.addListener(listener);
            super.run(notifier);
        }
    }

    @Override // org.mockito.internal.runners.InternalRunner
    public void run(RunNotifier notifier) {
        this.runner.run(notifier);
    }

    @Override // org.mockito.internal.runners.InternalRunner
    public Description getDescription() {
        return this.runner.getDescription();
    }

    @Override // org.junit.runner.manipulation.Filterable
    public void filter(Filter filter) throws NoTestsRemainException {
        this.runner.filter(filter);
    }
}
