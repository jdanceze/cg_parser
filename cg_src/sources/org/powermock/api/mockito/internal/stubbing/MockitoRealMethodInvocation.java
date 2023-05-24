package org.powermock.api.mockito.internal.stubbing;
/* loaded from: gencallgraphv3.jar:powermock-api-mockito2-2.0.9.jar:org/powermock/api/mockito/internal/stubbing/MockitoRealMethodInvocation.class */
public class MockitoRealMethodInvocation {
    private static final ThreadLocal<Boolean> handledByMockito = new ThreadLocal<>();

    private MockitoRealMethodInvocation() {
    }

    public static void mockitoInvocationStarted() {
        handledByMockito.set(true);
    }

    public static void mockitoInvocationFinished() {
        handledByMockito.set(false);
    }

    public static boolean isHandledByMockito() {
        Boolean handled = handledByMockito.get();
        if (handled == null) {
            return false;
        }
        return handled.booleanValue();
    }
}
