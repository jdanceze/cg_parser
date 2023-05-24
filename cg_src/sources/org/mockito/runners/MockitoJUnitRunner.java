package org.mockito.runners;

import java.lang.reflect.InvocationTargetException;
import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.RunNotifier;
@Deprecated
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/runners/MockitoJUnitRunner.class */
public class MockitoJUnitRunner extends org.mockito.junit.MockitoJUnitRunner {

    @Deprecated
    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/runners/MockitoJUnitRunner$Silent.class */
    public static class Silent extends MockitoJUnitRunner {
        public Silent(Class<?> klass) throws InvocationTargetException {
            super(klass);
        }
    }

    @Deprecated
    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/runners/MockitoJUnitRunner$Strict.class */
    public static class Strict extends MockitoJUnitRunner {
        public Strict(Class<?> klass) throws InvocationTargetException {
            super(klass);
        }
    }

    public MockitoJUnitRunner(Class<?> klass) throws InvocationTargetException {
        super(klass);
    }

    @Override // org.mockito.junit.MockitoJUnitRunner, org.junit.runner.Runner
    @Deprecated
    public void run(RunNotifier notifier) {
        super.run(notifier);
    }

    @Override // org.mockito.junit.MockitoJUnitRunner, org.junit.runner.Runner, org.junit.runner.Describable
    @Deprecated
    public Description getDescription() {
        return super.getDescription();
    }

    @Override // org.mockito.junit.MockitoJUnitRunner, org.junit.runner.manipulation.Filterable
    @Deprecated
    public void filter(Filter filter) throws NoTestsRemainException {
        super.filter(filter);
    }
}
