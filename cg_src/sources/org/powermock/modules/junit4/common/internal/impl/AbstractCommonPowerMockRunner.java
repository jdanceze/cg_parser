package org.powermock.modules.junit4.common.internal.impl;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.manipulation.Sortable;
import org.junit.runner.manipulation.Sorter;
import org.junit.runner.notification.RunNotifier;
import org.powermock.core.MockRepository;
import org.powermock.modules.junit4.common.internal.JUnit4TestSuiteChunker;
import org.powermock.modules.junit4.common.internal.PowerMockJUnitRunnerDelegate;
/* loaded from: gencallgraphv3.jar:powermock-module-junit4-common-2.0.9.jar:org/powermock/modules/junit4/common/internal/impl/AbstractCommonPowerMockRunner.class */
public abstract class AbstractCommonPowerMockRunner extends Runner implements Filterable, Sortable {
    private JUnit4TestSuiteChunker suiteChunker;

    public AbstractCommonPowerMockRunner(Class<?> klass, Class<? extends PowerMockJUnitRunnerDelegate> runnerDelegateImplClass) throws Exception {
        this.suiteChunker = new JUnit4TestSuiteChunkerImpl(klass, runnerDelegateImplClass);
        MockRepository.clear();
    }

    @Override // org.junit.runner.Runner, org.junit.runner.Describable
    public Description getDescription() {
        return this.suiteChunker.getDescription();
    }

    @Override // org.junit.runner.Runner
    public void run(RunNotifier notifier) {
        try {
            this.suiteChunker.run(notifier);
        } finally {
            this.suiteChunker = null;
        }
    }

    @Override // org.junit.runner.Runner
    public synchronized int testCount() {
        return this.suiteChunker.getTestCount();
    }

    @Override // org.junit.runner.manipulation.Filterable
    public void filter(Filter filter) throws NoTestsRemainException {
        this.suiteChunker.filter(filter);
    }

    @Override // org.junit.runner.manipulation.Sortable
    public void sort(Sorter sorter) {
        this.suiteChunker.sort(sorter);
    }
}
