package org.powermock.api.mockito.expectation;

import org.mockito.stubbing.OngoingStubbing;
/* loaded from: gencallgraphv3.jar:powermock-api-mockito2-2.0.9.jar:org/powermock/api/mockito/expectation/WithExpectedArguments.class */
public interface WithExpectedArguments<T> {
    OngoingStubbing<T> withArguments(Object obj, Object... objArr) throws Exception;
}
