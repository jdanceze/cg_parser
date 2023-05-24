package org.mockito.internal.junit;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/junit/DefaultTestFinishedEvent.class */
public class DefaultTestFinishedEvent implements TestFinishedEvent {
    private final Object testClassInstance;
    private final String testMethodName;
    private final Throwable testFailure;

    public DefaultTestFinishedEvent(Object testClassInstance, String testMethodName, Throwable testFailure) {
        this.testClassInstance = testClassInstance;
        this.testMethodName = testMethodName;
        this.testFailure = testFailure;
    }

    @Override // org.mockito.internal.junit.TestFinishedEvent
    public Throwable getFailure() {
        return this.testFailure;
    }

    @Override // org.mockito.internal.junit.TestFinishedEvent
    public String getTestName() {
        return this.testClassInstance.getClass().getSimpleName() + "." + this.testMethodName;
    }
}
