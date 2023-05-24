package org.junit.internal.runners.model;

import org.junit.internal.AssumptionViolatedException;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/internal/runners/model/EachTestNotifier.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/internal/runners/model/EachTestNotifier.class */
public class EachTestNotifier {
    private final RunNotifier notifier;
    private final Description description;

    public EachTestNotifier(RunNotifier notifier, Description description) {
        this.notifier = notifier;
        this.description = description;
    }

    public void addFailure(Throwable targetException) {
        if (targetException instanceof org.junit.runners.model.MultipleFailureException) {
            addMultipleFailureException((org.junit.runners.model.MultipleFailureException) targetException);
        } else {
            this.notifier.fireTestFailure(new Failure(this.description, targetException));
        }
    }

    private void addMultipleFailureException(org.junit.runners.model.MultipleFailureException mfe) {
        for (Throwable each : mfe.getFailures()) {
            addFailure(each);
        }
    }

    public void addFailedAssumption(AssumptionViolatedException e) {
        this.notifier.fireTestAssumptionFailed(new Failure(this.description, e));
    }

    public void fireTestFinished() {
        this.notifier.fireTestFinished(this.description);
    }

    public void fireTestStarted() {
        this.notifier.fireTestStarted(this.description);
    }

    public void fireTestIgnored() {
        this.notifier.fireTestIgnored(this.description);
    }

    public void fireTestSuiteStarted() {
        this.notifier.fireTestSuiteStarted(this.description);
    }

    public void fireTestSuiteFinished() {
        this.notifier.fireTestSuiteFinished(this.description);
    }
}
