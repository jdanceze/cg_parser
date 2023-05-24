package org.junit.internal.runners.statements;

import org.junit.runners.model.Statement;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/internal/runners/statements/Fail.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/internal/runners/statements/Fail.class */
public class Fail extends Statement {
    private final Throwable error;

    public Fail(Throwable e) {
        this.error = e;
    }

    @Override // org.junit.runners.model.Statement
    public void evaluate() throws Throwable {
        throw this.error;
    }
}
