package org.junit.internal.runners.statements;

import java.util.List;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/internal/runners/statements/RunBefores.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/internal/runners/statements/RunBefores.class */
public class RunBefores extends Statement {
    private final Statement next;
    private final Object target;
    private final List<FrameworkMethod> befores;

    public RunBefores(Statement next, List<FrameworkMethod> befores, Object target) {
        this.next = next;
        this.befores = befores;
        this.target = target;
    }

    @Override // org.junit.runners.model.Statement
    public void evaluate() throws Throwable {
        for (FrameworkMethod before : this.befores) {
            invokeMethod(before);
        }
        this.next.evaluate();
    }

    protected void invokeMethod(FrameworkMethod method) throws Throwable {
        method.invokeExplosively(this.target, new Object[0]);
    }
}
