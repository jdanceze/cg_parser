package org.mockito.internal.util;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/Checks.class */
public class Checks {
    public static <T> T checkNotNull(T value, String checkedValue) {
        return (T) checkNotNull(value, checkedValue, null);
    }

    public static <T> T checkNotNull(T value, String checkedValue, String additionalMessage) {
        if (value == null) {
            String message = checkedValue + " should not be null";
            if (additionalMessage != null) {
                message = message + ". " + additionalMessage;
            }
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static <T extends Iterable<?>> T checkItemsNotNull(T iterable, String checkedIterable) {
        checkNotNull(iterable, checkedIterable);
        for (Object item : iterable) {
            checkNotNull(item, "item in " + checkedIterable);
        }
        return iterable;
    }
}
