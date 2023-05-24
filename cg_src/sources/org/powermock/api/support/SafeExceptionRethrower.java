package org.powermock.api.support;
/* loaded from: gencallgraphv3.jar:powermock-api-support-2.0.9.jar:org/powermock/api/support/SafeExceptionRethrower.class */
public class SafeExceptionRethrower {
    public static void safeRethrow(Throwable t) {
        safeRethrow0(t);
    }

    private static <T extends Throwable> void safeRethrow0(Throwable t) throws Throwable {
        throw t;
    }
}
