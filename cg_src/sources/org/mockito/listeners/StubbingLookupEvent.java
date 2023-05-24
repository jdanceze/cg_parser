package org.mockito.listeners;

import java.util.Collection;
import org.mockito.invocation.Invocation;
import org.mockito.mock.MockCreationSettings;
import org.mockito.stubbing.Stubbing;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/listeners/StubbingLookupEvent.class */
public interface StubbingLookupEvent {
    Invocation getInvocation();

    Stubbing getStubbingFound();

    Collection<Stubbing> getAllStubbings();

    MockCreationSettings getMockSettings();
}
