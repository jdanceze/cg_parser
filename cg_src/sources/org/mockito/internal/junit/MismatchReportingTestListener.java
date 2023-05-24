package org.mockito.internal.junit;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.mockito.mock.MockCreationSettings;
import org.mockito.plugins.MockitoLogger;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/junit/MismatchReportingTestListener.class */
public class MismatchReportingTestListener implements MockitoTestListener {
    private final MockitoLogger logger;
    private List<Object> mocks = new LinkedList();

    public MismatchReportingTestListener(MockitoLogger logger) {
        this.logger = logger;
    }

    @Override // org.mockito.internal.junit.MockitoTestListener
    public void testFinished(TestFinishedEvent event) {
        Collection<Object> createdMocks = this.mocks;
        this.mocks = new LinkedList();
        if (event.getFailure() != null) {
            new ArgMismatchFinder().getStubbingArgMismatches(createdMocks).format(event.getTestName(), this.logger);
        }
    }

    @Override // org.mockito.listeners.MockCreationListener
    public void onMockCreated(Object mock, MockCreationSettings settings) {
        this.mocks.add(mock);
    }
}
