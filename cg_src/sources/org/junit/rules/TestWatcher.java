package org.junit.rules;

import java.util.ArrayList;
import java.util.List;
import org.junit.internal.AssumptionViolatedException;
import org.junit.runner.Description;
import org.junit.runners.model.MultipleFailureException;
import org.junit.runners.model.Statement;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/rules/TestWatcher.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/rules/TestWatcher.class */
public abstract class TestWatcher implements TestRule {
    static /* synthetic */ void access$300(TestWatcher x0, Throwable x1, Description x2, List x3) {
        x0.failedQuietly(x1, x2, x3);
    }

    @Override // org.junit.rules.TestRule
    public Statement apply(final Statement base, final Description description) {
        return new Statement() { // from class: org.junit.rules.TestWatcher.1
            @Override // org.junit.runners.model.Statement
            public void evaluate() throws Throwable {
                List<Throwable> errors = new ArrayList<>();
                TestWatcher.this.startingQuietly(description, errors);
                try {
                    try {
                        base.evaluate();
                        TestWatcher.this.succeededQuietly(description, errors);
                        TestWatcher.this.finishedQuietly(description, errors);
                    } catch (AssumptionViolatedException e) {
                        errors.add(e);
                        TestWatcher.this.skippedQuietly(e, description, errors);
                        TestWatcher.this.finishedQuietly(description, errors);
                    }
                    MultipleFailureException.assertEmpty(errors);
                } catch (Throwable th) {
                    TestWatcher.this.finishedQuietly(description, errors);
                    throw th;
                }
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void succeededQuietly(Description description, List<Throwable> errors) {
        try {
            succeeded(description);
        } catch (Throwable e) {
            errors.add(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void failedQuietly(Throwable e, Description description, List<Throwable> errors) {
        try {
            failed(e, description);
        } catch (Throwable e1) {
            errors.add(e1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void skippedQuietly(AssumptionViolatedException e, Description description, List<Throwable> errors) {
        try {
            if (e instanceof org.junit.AssumptionViolatedException) {
                skipped((org.junit.AssumptionViolatedException) e, description);
            } else {
                skipped(e, description);
            }
        } catch (Throwable e1) {
            errors.add(e1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startingQuietly(Description description, List<Throwable> errors) {
        try {
            starting(description);
        } catch (Throwable e) {
            errors.add(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finishedQuietly(Description description, List<Throwable> errors) {
        try {
            finished(description);
        } catch (Throwable e) {
            errors.add(e);
        }
    }

    protected void succeeded(Description description) {
    }

    protected void failed(Throwable e, Description description) {
    }

    protected void skipped(org.junit.AssumptionViolatedException e, Description description) {
        skipped((AssumptionViolatedException) e, description);
    }

    @Deprecated
    protected void skipped(AssumptionViolatedException e, Description description) {
    }

    protected void starting(Description description) {
    }

    protected void finished(Description description) {
    }
}
