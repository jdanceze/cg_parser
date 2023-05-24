package org.mockito.internal.runners;

import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.RunNotifier;
import org.mockito.Mockito;
import org.mockito.internal.junit.UnnecessaryStubbingsReporter;
import org.mockito.internal.runners.util.FailureDetector;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/runners/StrictRunner.class */
public class StrictRunner implements InternalRunner {
    private final Class<?> testClass;
    private final InternalRunner runner;
    private boolean filterRequested;

    public StrictRunner(InternalRunner runner, Class<?> testClass) {
        this.runner = runner;
        this.testClass = testClass;
    }

    @Override // org.mockito.internal.runners.InternalRunner
    public void run(RunNotifier notifier) {
        UnnecessaryStubbingsReporter reporter = new UnnecessaryStubbingsReporter();
        FailureDetector listener = new FailureDetector();
        Mockito.framework().addListener(reporter);
        try {
            notifier.addListener(listener);
            this.runner.run(notifier);
            Mockito.framework().removeListener(reporter);
            if (!this.filterRequested && listener.isSuccessful()) {
                reporter.validateUnusedStubs(this.testClass, notifier);
            }
        } catch (Throwable th) {
            Mockito.framework().removeListener(reporter);
            throw th;
        }
    }

    @Override // org.mockito.internal.runners.InternalRunner
    public Description getDescription() {
        return this.runner.getDescription();
    }

    @Override // org.junit.runner.manipulation.Filterable
    public void filter(Filter filter) throws NoTestsRemainException {
        this.filterRequested = true;
        this.runner.filter(filter);
    }
}
