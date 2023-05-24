package org.mockito.internal.util.collections;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/collections/Sets.class */
public abstract class Sets {
    public static Set<Object> newMockSafeHashSet(Iterable<Object> mocks) {
        return HashCodeAndEqualsSafeSet.of(mocks);
    }

    public static Set<Object> newMockSafeHashSet(Object... mocks) {
        return HashCodeAndEqualsSafeSet.of(mocks);
    }

    public static <T> Set<T> newSet(T... elements) {
        if (elements == null) {
            throw new IllegalArgumentException("Expected an array of elements (or empty array) but received a null.");
        }
        return new LinkedHashSet(Arrays.asList(elements));
    }
}
