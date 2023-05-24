package org.mockito.internal.junit;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/junit/TestFinishedEvent.class */
public interface TestFinishedEvent {
    Throwable getFailure();

    String getTestName();
}
