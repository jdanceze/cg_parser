package org.mockito.stubbing;

import org.mockito.NotExtensible;
@NotExtensible
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/stubbing/Stubber.class */
public interface Stubber extends BaseStubber {
    <T> T when(T t);
}
