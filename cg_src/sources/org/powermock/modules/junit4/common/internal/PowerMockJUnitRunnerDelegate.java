package org.powermock.modules.junit4.common.internal;

import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
/* loaded from: gencallgraphv3.jar:powermock-module-junit4-common-2.0.9.jar:org/powermock/modules/junit4/common/internal/PowerMockJUnitRunnerDelegate.class */
public interface PowerMockJUnitRunnerDelegate {
    void run(RunNotifier runNotifier);

    Description getDescription();

    int getTestCount();

    Class<?> getTestClass();
}
