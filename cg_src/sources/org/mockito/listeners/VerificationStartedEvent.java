package org.mockito.listeners;

import org.mockito.Incubating;
@Incubating
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/listeners/VerificationStartedEvent.class */
public interface VerificationStartedEvent {
    @Incubating
    void setMock(Object obj);

    @Incubating
    Object getMock();
}
