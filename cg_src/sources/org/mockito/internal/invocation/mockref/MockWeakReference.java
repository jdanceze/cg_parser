package org.mockito.internal.invocation.mockref;

import java.io.ObjectStreamException;
import java.lang.ref.WeakReference;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/invocation/mockref/MockWeakReference.class */
public class MockWeakReference<T> implements MockReference<T> {
    private final WeakReference<T> ref;

    public MockWeakReference(T t) {
        this.ref = new WeakReference<>(t);
    }

    private Object writeReplace() throws ObjectStreamException {
        return new MockStrongReference(get(), true);
    }

    @Override // org.mockito.internal.invocation.mockref.MockReference
    public T get() {
        T ref = this.ref.get();
        if (ref == null) {
            throw new IllegalStateException("The mock object was garbage collected. This should not happen in normal circumstances when using public API. Typically, the test class keeps strong reference to the mock object and it prevents getting the mock collected. Mockito internally needs to keep weak references to mock objects to avoid memory leaks for certain types of MockMaker implementations. If you see this exception using Mockito public API, please file a bug. For more information see issue #1313.");
        }
        return ref;
    }
}
