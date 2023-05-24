package org.mockito.internal.verification;

import java.util.List;
import org.mockito.invocation.Invocation;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/verification/RegisteredInvocations.class */
public interface RegisteredInvocations {
    void add(Invocation invocation);

    void removeLast();

    List<Invocation> getAll();

    void clear();

    boolean isEmpty();
}
