package org.mockito.internal.invocation.mockref;

import java.io.ObjectStreamException;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/invocation/mockref/MockStrongReference.class */
public class MockStrongReference<T> implements MockReference<T> {
    private final T ref;
    private final boolean deserializeAsWeakRef;

    public MockStrongReference(T ref, boolean deserializeAsWeakRef) {
        this.ref = ref;
        this.deserializeAsWeakRef = deserializeAsWeakRef;
    }

    @Override // org.mockito.internal.invocation.mockref.MockReference
    public T get() {
        return this.ref;
    }

    private Object readResolve() throws ObjectStreamException {
        if (this.deserializeAsWeakRef) {
            return new MockWeakReference(this.ref);
        }
        return this;
    }
}
