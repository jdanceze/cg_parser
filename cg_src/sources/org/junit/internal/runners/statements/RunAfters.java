package org.junit.internal.runners.statements;

import java.util.ArrayList;
import java.util.List;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.MultipleFailureException;
import org.junit.runners.model.Statement;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/internal/runners/statements/RunAfters.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/internal/runners/statements/RunAfters.class */
public class RunAfters extends Statement {
    private final Statement next;
    private final Object target;
    private final List<FrameworkMethod> afters;

    public RunAfters(Statement next, List<FrameworkMethod> afters, Object target) {
        this.next = next;
        this.afters = afters;
        this.target = target;
    }

    @Override // org.junit.runners.model.Statement
    public void evaluate() throws Throwable {
        List<Throwable> errors = new ArrayList<>();
        try {
            this.next.evaluate();
            for (FrameworkMethod each : this.afters) {
                try {
                    invokeMethod(each);
                } catch (Throwable e) {
                    errors.add(e);
                }
            }
        } catch (Throwable e2) {
            try {
                errors.add(e2);
                for (FrameworkMethod each2 : this.afters) {
                    try {
                        invokeMethod(each2);
                    } catch (Throwable e3) {
                        errors.add(e3);
                    }
                }
            } catch (Throwable th) {
                for (FrameworkMethod each3 : this.afters) {
                    try {
                        invokeMethod(each3);
                    } catch (Throwable e4) {
                        errors.add(e4);
                    }
                }
                throw th;
            }
        }
        MultipleFailureException.assertEmpty(errors);
    }

    protected void invokeMethod(FrameworkMethod method) throws Throwable {
        method.invokeExplosively(this.target, new Object[0]);
    }
}
