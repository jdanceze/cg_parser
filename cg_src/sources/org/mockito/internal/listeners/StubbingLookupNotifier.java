package org.mockito.internal.listeners;

import java.util.Collection;
import java.util.List;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.invocation.Invocation;
import org.mockito.listeners.StubbingLookupEvent;
import org.mockito.listeners.StubbingLookupListener;
import org.mockito.mock.MockCreationSettings;
import org.mockito.stubbing.Stubbing;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/listeners/StubbingLookupNotifier.class */
public class StubbingLookupNotifier {
    public static void notifyStubbedAnswerLookup(Invocation invocation, Stubbing stubbingFound, Collection<Stubbing> allStubbings, CreationSettings creationSettings) {
        List<StubbingLookupListener> listeners = creationSettings.getStubbingLookupListeners();
        if (listeners.isEmpty()) {
            return;
        }
        StubbingLookupEvent event = new Event(invocation, stubbingFound, allStubbings, creationSettings);
        for (StubbingLookupListener listener : listeners) {
            listener.onStubbingLookup(event);
        }
    }

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/listeners/StubbingLookupNotifier$Event.class */
    static class Event implements StubbingLookupEvent {
        private final Invocation invocation;
        private final Stubbing stubbing;
        private final Collection<Stubbing> allStubbings;
        private final MockCreationSettings mockSettings;

        public Event(Invocation invocation, Stubbing stubbing, Collection<Stubbing> allStubbings, MockCreationSettings mockSettings) {
            this.invocation = invocation;
            this.stubbing = stubbing;
            this.allStubbings = allStubbings;
            this.mockSettings = mockSettings;
        }

        @Override // org.mockito.listeners.StubbingLookupEvent
        public Invocation getInvocation() {
            return this.invocation;
        }

        @Override // org.mockito.listeners.StubbingLookupEvent
        public Stubbing getStubbingFound() {
            return this.stubbing;
        }

        @Override // org.mockito.listeners.StubbingLookupEvent
        public Collection<Stubbing> getAllStubbings() {
            return this.allStubbings;
        }

        @Override // org.mockito.listeners.StubbingLookupEvent
        public MockCreationSettings getMockSettings() {
            return this.mockSettings;
        }
    }
}
