package org.mockito.plugins;

import org.mockito.Incubating;
@Incubating
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/plugins/InlineMockMaker.class */
public interface InlineMockMaker extends MockMaker {
    @Incubating
    void clearMock(Object obj);

    @Incubating
    void clearAllMocks();
}
