package org.junit.experimental.results;

import java.util.List;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/experimental/results/FailureList.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/experimental/results/FailureList.class */
class FailureList {
    private final List<Failure> failures;

    public FailureList(List<Failure> failures) {
        this.failures = failures;
    }

    public Result result() {
        Result result = new Result();
        RunListener listener = result.createListener();
        for (Failure failure : this.failures) {
            try {
                listener.testFailure(failure);
            } catch (Exception e) {
                throw new RuntimeException("I can't believe this happened");
            }
        }
        return result;
    }
}
