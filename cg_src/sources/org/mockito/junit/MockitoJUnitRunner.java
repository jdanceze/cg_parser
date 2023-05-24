package org.mockito.junit;

import java.lang.reflect.InvocationTargetException;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.RunNotifier;
import org.mockito.internal.runners.InternalRunner;
import org.mockito.internal.runners.RunnerFactory;
import org.mockito.internal.runners.StrictRunner;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/junit/MockitoJUnitRunner.class */
public class MockitoJUnitRunner extends Runner implements Filterable {
    private final InternalRunner runner;

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/junit/MockitoJUnitRunner$Silent.class */
    public static class Silent extends MockitoJUnitRunner {
        public Silent(Class<?> klass) throws InvocationTargetException {
            super(new RunnerFactory().create(klass));
        }
    }

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/junit/MockitoJUnitRunner$Strict.class */
    public static class Strict extends MockitoJUnitRunner {
        public Strict(Class<?> klass) throws InvocationTargetException {
            super(new StrictRunner(new RunnerFactory().createStrict(klass), klass));
        }
    }

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/junit/MockitoJUnitRunner$StrictStubs.class */
    public static class StrictStubs extends MockitoJUnitRunner {
        public StrictStubs(Class<?> klass) throws InvocationTargetException {
            super(new StrictRunner(new RunnerFactory().createStrictStubs(klass), klass));
        }
    }

    public MockitoJUnitRunner(Class<?> klass) throws InvocationTargetException {
        this(new StrictRunner(new RunnerFactory().createStrict(klass), klass));
    }

    MockitoJUnitRunner(InternalRunner runner) throws InvocationTargetException {
        this.runner = runner;
    }

    @Override // org.junit.runner.Runner
    public void run(RunNotifier notifier) {
        this.runner.run(notifier);
    }

    @Override // org.junit.runner.Runner, org.junit.runner.Describable
    public Description getDescription() {
        return this.runner.getDescription();
    }

    @Override // org.junit.runner.manipulation.Filterable
    public void filter(Filter filter) throws NoTestsRemainException {
        this.runner.filter(filter);
    }
}
