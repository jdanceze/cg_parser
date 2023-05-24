package org.mockito.internal.runners;

import org.junit.runner.Description;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.notification.RunNotifier;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/runners/InternalRunner.class */
public interface InternalRunner extends Filterable {
    void run(RunNotifier runNotifier);

    Description getDescription();
}
