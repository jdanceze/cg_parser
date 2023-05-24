package org.mockito.internal.junit;

import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Map;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.listeners.AutoCleanableListener;
import org.mockito.mock.MockCreationSettings;
import org.mockito.plugins.MockitoLogger;
import org.mockito.quality.Strictness;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/junit/UniversalTestListener.class */
public class UniversalTestListener implements MockitoTestListener, AutoCleanableListener {
    private Strictness currentStrictness;
    private final MockitoLogger logger;
    private Map<Object, MockCreationSettings> mocks = new IdentityHashMap();
    private DefaultStubbingLookupListener stubbingLookupListener;
    private boolean listenerDirty;

    public UniversalTestListener(Strictness initialStrictness, MockitoLogger logger) {
        this.currentStrictness = initialStrictness;
        this.logger = logger;
        this.stubbingLookupListener = new DefaultStubbingLookupListener(this.currentStrictness);
    }

    @Override // org.mockito.internal.junit.MockitoTestListener
    public void testFinished(TestFinishedEvent event) {
        Collection<Object> createdMocks = this.mocks.keySet();
        this.mocks = new IdentityHashMap();
        switch (this.currentStrictness) {
            case WARN:
                emitWarnings(this.logger, event, createdMocks);
                return;
            case STRICT_STUBS:
                reportUnusedStubs(event, createdMocks);
                return;
            case LENIENT:
                return;
            default:
                throw new IllegalStateException("Unknown strictness: " + this.currentStrictness);
        }
    }

    private void reportUnusedStubs(TestFinishedEvent event, Collection<Object> mocks) {
        if (event.getFailure() == null && !this.stubbingLookupListener.isMismatchesReported()) {
            UnusedStubbings unused = new UnusedStubbingsFinder().getUnusedStubbings(mocks);
            unused.reportUnused();
        }
    }

    private static void emitWarnings(MockitoLogger logger, TestFinishedEvent event, Collection<Object> mocks) {
        if (event.getFailure() != null) {
            new ArgMismatchFinder().getStubbingArgMismatches(mocks).format(event.getTestName(), logger);
        } else {
            new UnusedStubbingsFinder().getUnusedStubbings(mocks).format(event.getTestName(), logger);
        }
    }

    @Override // org.mockito.listeners.MockCreationListener
    public void onMockCreated(Object mock, MockCreationSettings settings) {
        this.mocks.put(mock, settings);
        ((CreationSettings) settings).getStubbingLookupListeners().add(this.stubbingLookupListener);
    }

    public void setStrictness(Strictness strictness) {
        this.currentStrictness = strictness;
        this.stubbingLookupListener.setCurrentStrictness(strictness);
    }

    @Override // org.mockito.internal.listeners.AutoCleanableListener
    public boolean isListenerDirty() {
        return this.listenerDirty;
    }

    public void setListenerDirty() {
        this.listenerDirty = true;
    }
}
