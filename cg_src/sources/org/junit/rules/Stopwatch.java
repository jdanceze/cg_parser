package org.junit.rules;

import java.util.concurrent.TimeUnit;
import org.junit.AssumptionViolatedException;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/rules/Stopwatch.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/rules/Stopwatch.class */
public class Stopwatch implements TestRule {
    private final Clock clock;
    private volatile long startNanos;
    private volatile long endNanos;

    public Stopwatch() {
        this(new Clock());
    }

    Stopwatch(Clock clock) {
        this.clock = clock;
    }

    public long runtime(TimeUnit unit) {
        return unit.convert(getNanos(), TimeUnit.NANOSECONDS);
    }

    protected void succeeded(long nanos, Description description) {
    }

    protected void failed(long nanos, Throwable e, Description description) {
    }

    protected void skipped(long nanos, AssumptionViolatedException e, Description description) {
    }

    protected void finished(long nanos, Description description) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long getNanos() {
        if (this.startNanos == 0) {
            throw new IllegalStateException("Test has not started");
        }
        long currentEndNanos = this.endNanos;
        if (currentEndNanos == 0) {
            currentEndNanos = this.clock.nanoTime();
        }
        return currentEndNanos - this.startNanos;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void starting() {
        this.startNanos = this.clock.nanoTime();
        this.endNanos = 0L;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopping() {
        this.endNanos = this.clock.nanoTime();
    }

    @Override // org.junit.rules.TestRule
    public final Statement apply(Statement base, Description description) {
        return new InternalWatcher().apply(base, description);
    }

    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:junit-4.13.2.jar:org/junit/rules/Stopwatch$InternalWatcher.class
     */
    /* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/rules/Stopwatch$InternalWatcher.class */
    private class InternalWatcher extends TestWatcher {
        private InternalWatcher() {
        }

        @Override // org.junit.rules.TestWatcher
        protected void starting(Description description) {
            Stopwatch.this.starting();
        }

        @Override // org.junit.rules.TestWatcher
        protected void finished(Description description) {
            Stopwatch.this.finished(Stopwatch.this.getNanos(), description);
        }

        @Override // org.junit.rules.TestWatcher
        protected void succeeded(Description description) {
            Stopwatch.this.stopping();
            Stopwatch.this.succeeded(Stopwatch.this.getNanos(), description);
        }

        @Override // org.junit.rules.TestWatcher
        protected void failed(Throwable e, Description description) {
            Stopwatch.this.stopping();
            Stopwatch.this.failed(Stopwatch.this.getNanos(), e, description);
        }

        @Override // org.junit.rules.TestWatcher
        protected void skipped(AssumptionViolatedException e, Description description) {
            Stopwatch.this.stopping();
            Stopwatch.this.skipped(Stopwatch.this.getNanos(), e, description);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:junit-4.13.2.jar:org/junit/rules/Stopwatch$Clock.class
     */
    /* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/rules/Stopwatch$Clock.class */
    public static class Clock {
        Clock() {
        }

        public long nanoTime() {
            return System.nanoTime();
        }
    }
}
