package org.junit.rules;

import java.util.ArrayList;
import java.util.List;
import org.junit.runner.Description;
import org.junit.runners.model.MultipleFailureException;
import org.junit.runners.model.Statement;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/rules/ExternalResource.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/rules/ExternalResource.class */
public abstract class ExternalResource implements TestRule {
    @Override // org.junit.rules.TestRule
    public Statement apply(Statement base, Description description) {
        return statement(base);
    }

    private Statement statement(final Statement base) {
        return new Statement() { // from class: org.junit.rules.ExternalResource.1
            @Override // org.junit.runners.model.Statement
            public void evaluate() throws Throwable {
                ExternalResource.this.before();
                List<Throwable> errors = new ArrayList<>();
                try {
                    base.evaluate();
                } catch (Throwable t) {
                    try {
                        errors.add(t);
                        try {
                            ExternalResource.this.after();
                        } catch (Throwable t2) {
                            errors.add(t2);
                        }
                    } finally {
                        try {
                            ExternalResource.this.after();
                        } catch (Throwable t3) {
                            errors.add(t3);
                        }
                    }
                }
                MultipleFailureException.assertEmpty(errors);
            }
        };
    }

    protected void before() throws Throwable {
    }

    protected void after() {
    }
}
