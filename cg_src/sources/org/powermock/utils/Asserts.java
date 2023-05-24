package org.powermock.utils;

import org.powermock.PowerMockInternalException;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/utils/Asserts.class */
public class Asserts {
    public static void assertNotNull(Object value, String message) {
        if (value == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void internalAssertNotNull(Object value, String message) {
        if (value == null) {
            throw new PowerMockInternalException(message);
        }
    }
}
