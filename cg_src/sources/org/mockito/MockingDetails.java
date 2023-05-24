package org.mockito;

import java.util.Collection;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.stubbing.Stubbing;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/MockingDetails.class */
public interface MockingDetails {
    boolean isMock();

    boolean isSpy();

    Collection<Invocation> getInvocations();

    MockCreationSettings<?> getMockCreationSettings();

    Collection<Stubbing> getStubbings();

    String printInvocations();

    @Incubating
    MockHandler getMockHandler();

    Object getMock();
}
