package org.junit.internal.requests;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.junit.runner.Request;
import org.junit.runner.Runner;
/* JADX INFO: Access modifiers changed from: package-private */
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/internal/requests/MemoizingRequest.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/internal/requests/MemoizingRequest.class */
public abstract class MemoizingRequest extends Request {
    private final Lock runnerLock = new ReentrantLock();
    private volatile Runner runner;

    protected abstract Runner createRunner();

    @Override // org.junit.runner.Request
    public final Runner getRunner() {
        if (this.runner == null) {
            this.runnerLock.lock();
            try {
                if (this.runner == null) {
                    this.runner = createRunner();
                }
            } finally {
                this.runnerLock.unlock();
            }
        }
        return this.runner;
    }
}
