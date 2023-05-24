package org.mockito.internal.handler;

import org.mockito.internal.matchers.Equality;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Invocation;
import org.mockito.listeners.MethodInvocationReport;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/handler/NotifiedMethodInvocationReport.class */
public class NotifiedMethodInvocationReport implements MethodInvocationReport {
    private final Invocation invocation;
    private final Object returnedValue;
    private final Throwable throwable;

    public NotifiedMethodInvocationReport(Invocation invocation, Object returnedValue) {
        this.invocation = invocation;
        this.returnedValue = returnedValue;
        this.throwable = null;
    }

    public NotifiedMethodInvocationReport(Invocation invocation, Throwable throwable) {
        this.invocation = invocation;
        this.returnedValue = null;
        this.throwable = throwable;
    }

    @Override // org.mockito.listeners.MethodInvocationReport
    public DescribedInvocation getInvocation() {
        return this.invocation;
    }

    @Override // org.mockito.listeners.MethodInvocationReport
    public Object getReturnedValue() {
        return this.returnedValue;
    }

    @Override // org.mockito.listeners.MethodInvocationReport
    public Throwable getThrowable() {
        return this.throwable;
    }

    @Override // org.mockito.listeners.MethodInvocationReport
    public boolean threwException() {
        return this.throwable != null;
    }

    @Override // org.mockito.listeners.MethodInvocationReport
    public String getLocationOfStubbing() {
        if (this.invocation.stubInfo() == null) {
            return null;
        }
        return this.invocation.stubInfo().stubbedAt().toString();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NotifiedMethodInvocationReport that = (NotifiedMethodInvocationReport) o;
        return Equality.areEqual(this.invocation, that.invocation) && Equality.areEqual(this.returnedValue, that.returnedValue) && Equality.areEqual(this.throwable, that.throwable);
    }

    public int hashCode() {
        int result = this.invocation != null ? this.invocation.hashCode() : 0;
        return (31 * ((31 * result) + (this.returnedValue != null ? this.returnedValue.hashCode() : 0))) + (this.throwable != null ? this.throwable.hashCode() : 0);
    }
}
