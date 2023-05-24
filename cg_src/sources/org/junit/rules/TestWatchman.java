package org.junit.rules;

import org.junit.internal.AssumptionViolatedException;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/rules/TestWatchman.class
 */
@Deprecated
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/rules/TestWatchman.class */
public class TestWatchman implements MethodRule {
    @Override // org.junit.rules.MethodRule
    public Statement apply(final Statement base, final FrameworkMethod method, Object target) {
        return new Statement() { // from class: org.junit.rules.TestWatchman.1
            @Override // org.junit.runners.model.Statement
            public void evaluate() throws Throwable {
                TestWatchman.this.starting(method);
                try {
                    try {
                        base.evaluate();
                        TestWatchman.this.succeeded(method);
                        TestWatchman.this.finished(method);
                    } catch (AssumptionViolatedException e) {
                        throw e;
                    } catch (Throwable e2) {
                        TestWatchman.this.failed(e2, method);
                        throw e2;
                    }
                } catch (Throwable th) {
                    TestWatchman.this.finished(method);
                    throw th;
                }
            }
        };
    }

    public void succeeded(FrameworkMethod method) {
    }

    public void failed(Throwable e, FrameworkMethod method) {
    }

    public void starting(FrameworkMethod method) {
    }

    public void finished(FrameworkMethod method) {
    }
}
