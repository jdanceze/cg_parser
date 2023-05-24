package org.junit.rules;

import java.util.concurrent.TimeUnit;
import org.junit.internal.runners.statements.FailOnTimeout;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/rules/Timeout.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/rules/Timeout.class */
public class Timeout implements TestRule {
    private final long timeout;
    private final TimeUnit timeUnit;
    private final boolean lookForStuckThread;

    public static Builder builder() {
        return new Builder();
    }

    @Deprecated
    public Timeout(int millis) {
        this(millis, TimeUnit.MILLISECONDS);
    }

    public Timeout(long timeout, TimeUnit timeUnit) {
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.lookForStuckThread = false;
    }

    protected Timeout(Builder builder) {
        this.timeout = builder.getTimeout();
        this.timeUnit = builder.getTimeUnit();
        this.lookForStuckThread = builder.getLookingForStuckThread();
    }

    public static Timeout millis(long millis) {
        return new Timeout(millis, TimeUnit.MILLISECONDS);
    }

    public static Timeout seconds(long seconds) {
        return new Timeout(seconds, TimeUnit.SECONDS);
    }

    protected final long getTimeout(TimeUnit unit) {
        return unit.convert(this.timeout, this.timeUnit);
    }

    protected final boolean getLookingForStuckThread() {
        return this.lookForStuckThread;
    }

    protected Statement createFailOnTimeoutStatement(Statement statement) throws Exception {
        return FailOnTimeout.builder().withTimeout(this.timeout, this.timeUnit).withLookingForStuckThread(this.lookForStuckThread).build(statement);
    }

    @Override // org.junit.rules.TestRule
    public Statement apply(Statement base, Description description) {
        try {
            return createFailOnTimeoutStatement(base);
        } catch (Exception e) {
            return new Statement() { // from class: org.junit.rules.Timeout.1
                @Override // org.junit.runners.model.Statement
                public void evaluate() throws Throwable {
                    throw new RuntimeException("Invalid parameters for Timeout", e);
                }
            };
        }
    }

    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:junit-4.13.2.jar:org/junit/rules/Timeout$Builder.class
     */
    /* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/rules/Timeout$Builder.class */
    public static class Builder {
        private boolean lookForStuckThread = false;
        private long timeout = 0;
        private TimeUnit timeUnit = TimeUnit.SECONDS;

        protected Builder() {
        }

        public Builder withTimeout(long timeout, TimeUnit unit) {
            this.timeout = timeout;
            this.timeUnit = unit;
            return this;
        }

        protected long getTimeout() {
            return this.timeout;
        }

        protected TimeUnit getTimeUnit() {
            return this.timeUnit;
        }

        public Builder withLookingForStuckThread(boolean enable) {
            this.lookForStuckThread = enable;
            return this;
        }

        protected boolean getLookingForStuckThread() {
            return this.lookForStuckThread;
        }

        public Timeout build() {
            return new Timeout(this);
        }
    }
}
