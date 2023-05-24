package org.mockito.internal.junit;

import org.mockito.mock.MockCreationSettings;
import org.mockito.quality.Strictness;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/junit/StrictStubsRunnerTestListener.class */
public class StrictStubsRunnerTestListener implements MockitoTestListener {
    private final DefaultStubbingLookupListener stubbingLookupListener = new DefaultStubbingLookupListener(Strictness.STRICT_STUBS);

    @Override // org.mockito.internal.junit.MockitoTestListener
    public void testFinished(TestFinishedEvent event) {
    }

    @Override // org.mockito.listeners.MockCreationListener
    public void onMockCreated(Object mock, MockCreationSettings settings) {
        settings.getStubbingLookupListeners().add(this.stubbingLookupListener);
    }
}
