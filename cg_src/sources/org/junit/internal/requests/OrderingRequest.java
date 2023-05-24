package org.junit.internal.requests;

import org.junit.internal.runners.ErrorReportingRunner;
import org.junit.runner.Request;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.InvalidOrderingException;
import org.junit.runner.manipulation.Ordering;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/internal/requests/OrderingRequest.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/internal/requests/OrderingRequest.class */
public class OrderingRequest extends MemoizingRequest {
    private final Request request;
    private final Ordering ordering;

    public OrderingRequest(Request request, Ordering ordering) {
        this.request = request;
        this.ordering = ordering;
    }

    @Override // org.junit.internal.requests.MemoizingRequest
    protected Runner createRunner() {
        Runner runner = this.request.getRunner();
        try {
            this.ordering.apply(runner);
            return runner;
        } catch (InvalidOrderingException e) {
            return new ErrorReportingRunner(this.ordering.getClass(), e);
        }
    }
}
