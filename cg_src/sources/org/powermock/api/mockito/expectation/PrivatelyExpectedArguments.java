package org.powermock.api.mockito.expectation;
/* loaded from: gencallgraphv3.jar:powermock-api-mockito2-2.0.9.jar:org/powermock/api/mockito/expectation/PrivatelyExpectedArguments.class */
public interface PrivatelyExpectedArguments {
    <T> void withArguments(Object obj, Object... objArr) throws Exception;

    <T> void withNoArguments() throws Exception;
}
