package org.mockito.internal.verification;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.invocation.Invocation;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/verification/DefaultRegisteredInvocations.class */
public class DefaultRegisteredInvocations implements RegisteredInvocations, Serializable {
    private static final long serialVersionUID = -2674402327380736290L;
    private final LinkedList<Invocation> invocations = new LinkedList<>();

    @Override // org.mockito.internal.verification.RegisteredInvocations
    public void add(Invocation invocation) {
        synchronized (this.invocations) {
            this.invocations.add(invocation);
        }
    }

    @Override // org.mockito.internal.verification.RegisteredInvocations
    public void removeLast() {
        synchronized (this.invocations) {
            if (!this.invocations.isEmpty()) {
                this.invocations.removeLast();
            }
        }
    }

    @Override // org.mockito.internal.verification.RegisteredInvocations
    public List<Invocation> getAll() {
        List<Invocation> copiedList;
        synchronized (this.invocations) {
            copiedList = new LinkedList<>(this.invocations);
        }
        return ListUtil.filter(copiedList, new RemoveToString());
    }

    @Override // org.mockito.internal.verification.RegisteredInvocations
    public void clear() {
        synchronized (this.invocations) {
            this.invocations.clear();
        }
    }

    @Override // org.mockito.internal.verification.RegisteredInvocations
    public boolean isEmpty() {
        boolean isEmpty;
        synchronized (this.invocations) {
            isEmpty = this.invocations.isEmpty();
        }
        return isEmpty;
    }

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/verification/DefaultRegisteredInvocations$RemoveToString.class */
    private static class RemoveToString implements ListUtil.Filter<Invocation> {
        private RemoveToString() {
        }

        @Override // org.mockito.internal.util.collections.ListUtil.Filter
        public boolean isOut(Invocation invocation) {
            return ObjectMethodsGuru.isToStringMethod(invocation.getMethod());
        }
    }
}
