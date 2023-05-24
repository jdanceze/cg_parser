package org.mockito.listeners;

import org.mockito.invocation.DescribedInvocation;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/listeners/MethodInvocationReport.class */
public interface MethodInvocationReport {
    DescribedInvocation getInvocation();

    Object getReturnedValue();

    Throwable getThrowable();

    boolean threwException();

    String getLocationOfStubbing();
}
