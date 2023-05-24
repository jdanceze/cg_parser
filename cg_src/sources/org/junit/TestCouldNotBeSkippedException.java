package org.junit;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/TestCouldNotBeSkippedException.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/TestCouldNotBeSkippedException.class */
public class TestCouldNotBeSkippedException extends RuntimeException {
    private static final long serialVersionUID = 1;

    public TestCouldNotBeSkippedException(org.junit.internal.AssumptionViolatedException cause) {
        super("Test could not be skipped due to other failures", cause);
    }
}
