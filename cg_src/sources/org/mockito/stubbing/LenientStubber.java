package org.mockito.stubbing;

import org.mockito.CheckReturnValue;
import org.mockito.NotExtensible;
@NotExtensible
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/stubbing/LenientStubber.class */
public interface LenientStubber extends BaseStubber {
    @CheckReturnValue
    <T> OngoingStubbing<T> when(T t);
}
