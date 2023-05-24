package org.mockito.stubbing;

import org.mockito.Incubating;
import org.mockito.NotExtensible;
import org.mockito.invocation.Invocation;
import org.mockito.quality.Strictness;
@NotExtensible
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/stubbing/Stubbing.class */
public interface Stubbing extends Answer {
    Invocation getInvocation();

    boolean wasUsed();

    @Incubating
    Strictness getStrictness();
}
