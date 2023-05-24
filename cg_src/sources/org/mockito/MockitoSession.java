package org.mockito;

import org.mockito.quality.Strictness;
@NotExtensible
@Incubating
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/MockitoSession.class */
public interface MockitoSession {
    @Incubating
    void setStrictness(Strictness strictness);

    @Incubating
    void finishMocking();

    @Incubating
    void finishMocking(Throwable th);
}
