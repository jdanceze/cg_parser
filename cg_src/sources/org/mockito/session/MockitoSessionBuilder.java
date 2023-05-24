package org.mockito.session;

import org.mockito.Incubating;
import org.mockito.MockitoSession;
import org.mockito.NotExtensible;
import org.mockito.exceptions.misusing.UnfinishedMockingSessionException;
import org.mockito.quality.Strictness;
@NotExtensible
@Incubating
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/session/MockitoSessionBuilder.class */
public interface MockitoSessionBuilder {
    @Incubating
    MockitoSessionBuilder initMocks(Object obj);

    @Incubating
    MockitoSessionBuilder initMocks(Object... objArr);

    @Incubating
    MockitoSessionBuilder name(String str);

    @Incubating
    MockitoSessionBuilder strictness(Strictness strictness);

    @Incubating
    MockitoSessionBuilder logger(MockitoSessionLogger mockitoSessionLogger);

    @Incubating
    MockitoSession startMocking() throws UnfinishedMockingSessionException;
}
