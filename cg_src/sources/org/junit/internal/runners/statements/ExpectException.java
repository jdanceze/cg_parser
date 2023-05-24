package org.junit.internal.runners.statements;

import org.junit.internal.AssumptionViolatedException;
import org.junit.runners.model.Statement;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/internal/runners/statements/ExpectException.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/internal/runners/statements/ExpectException.class */
public class ExpectException extends Statement {
    private final Statement next;
    private final Class<? extends Throwable> expected;

    public ExpectException(Statement next, Class<? extends Throwable> expected) {
        this.next = next;
        this.expected = expected;
    }

    @Override // org.junit.runners.model.Statement
    public void evaluate() throws Exception {
        boolean complete = false;
        try {
            this.next.evaluate();
            complete = true;
        } catch (AssumptionViolatedException e) {
            if (!this.expected.isAssignableFrom(e.getClass())) {
                throw e;
            }
        } catch (Throwable e2) {
            if (!this.expected.isAssignableFrom(e2.getClass())) {
                String message = "Unexpected exception, expected<" + this.expected.getName() + "> but was<" + e2.getClass().getName() + ">";
                throw new Exception(message, e2);
            }
        }
        if (complete) {
            throw new AssertionError("Expected exception: " + this.expected.getName());
        }
    }
}
