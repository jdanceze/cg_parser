package org.mockito.internal.verification;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import org.mockito.invocation.Invocation;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/verification/SingleRegisteredInvocation.class */
public class SingleRegisteredInvocation implements RegisteredInvocations, Serializable {
    private Invocation invocation;

    @Override // org.mockito.internal.verification.RegisteredInvocations
    public void add(Invocation invocation) {
        this.invocation = invocation;
    }

    @Override // org.mockito.internal.verification.RegisteredInvocations
    public void removeLast() {
        this.invocation = null;
    }

    @Override // org.mockito.internal.verification.RegisteredInvocations
    public List<Invocation> getAll() {
        return Collections.emptyList();
    }

    @Override // org.mockito.internal.verification.RegisteredInvocations
    public void clear() {
        this.invocation = null;
    }

    @Override // org.mockito.internal.verification.RegisteredInvocations
    public boolean isEmpty() {
        return this.invocation == null;
    }
}
