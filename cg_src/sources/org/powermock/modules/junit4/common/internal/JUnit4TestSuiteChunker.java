package org.powermock.modules.junit4.common.internal;

import org.junit.runner.Description;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.manipulation.Sortable;
import org.junit.runner.notification.RunNotifier;
import org.powermock.tests.utils.RunnerTestSuiteChunker;
/* loaded from: gencallgraphv3.jar:powermock-module-junit4-common-2.0.9.jar:org/powermock/modules/junit4/common/internal/JUnit4TestSuiteChunker.class */
public interface JUnit4TestSuiteChunker extends RunnerTestSuiteChunker, Filterable, Sortable {
    Description getDescription();

    void run(RunNotifier runNotifier);
}
