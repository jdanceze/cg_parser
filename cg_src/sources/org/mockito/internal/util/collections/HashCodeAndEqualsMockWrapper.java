package org.mockito.internal.util.collections;

import org.mockito.internal.util.MockUtil;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/collections/HashCodeAndEqualsMockWrapper.class */
public class HashCodeAndEqualsMockWrapper {
    private final Object mockInstance;

    public HashCodeAndEqualsMockWrapper(Object mockInstance) {
        this.mockInstance = mockInstance;
    }

    public Object get() {
        return this.mockInstance;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof HashCodeAndEqualsMockWrapper) {
            HashCodeAndEqualsMockWrapper that = (HashCodeAndEqualsMockWrapper) o;
            return this.mockInstance == that.mockInstance;
        }
        return false;
    }

    public int hashCode() {
        return System.identityHashCode(this.mockInstance);
    }

    public static HashCodeAndEqualsMockWrapper of(Object mock) {
        return new HashCodeAndEqualsMockWrapper(mock);
    }

    public String toString() {
        Object typeInstanceString;
        StringBuilder append = new StringBuilder().append("HashCodeAndEqualsMockWrapper{mockInstance=");
        if (MockUtil.isMock(this.mockInstance)) {
            typeInstanceString = MockUtil.getMockName(this.mockInstance);
        } else {
            typeInstanceString = typeInstanceString();
        }
        return append.append(typeInstanceString).append('}').toString();
    }

    private String typeInstanceString() {
        return this.mockInstance.getClass().getSimpleName() + "(" + System.identityHashCode(this.mockInstance) + ")";
    }
}
