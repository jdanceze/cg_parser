package org.junit.runners.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.internal.runners.ErrorReportingRunner;
import org.junit.runner.Description;
import org.junit.runner.OrderWith;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.InvalidOrderingException;
import org.junit.runner.manipulation.Ordering;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/runners/model/RunnerBuilder.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/runners/model/RunnerBuilder.class */
public abstract class RunnerBuilder {
    private final Set<Class<?>> parents = new HashSet();

    public abstract Runner runnerForClass(Class<?> cls) throws Throwable;

    public Runner safeRunnerForClass(Class<?> testClass) {
        try {
            Runner runner = runnerForClass(testClass);
            if (runner != null) {
                configureRunner(runner);
            }
            return runner;
        } catch (Throwable e) {
            return new ErrorReportingRunner(testClass, e);
        }
    }

    private void configureRunner(Runner runner) throws InvalidOrderingException {
        Description description = runner.getDescription();
        OrderWith orderWith = (OrderWith) description.getAnnotation(OrderWith.class);
        if (orderWith != null) {
            Ordering ordering = Ordering.definedBy(orderWith.value(), description);
            ordering.apply(runner);
        }
    }

    Class<?> addParent(Class<?> parent) throws InitializationError {
        if (!this.parents.add(parent)) {
            throw new InitializationError(String.format("class '%s' (possibly indirectly) contains itself as a SuiteClass", parent.getName()));
        }
        return parent;
    }

    void removeParent(Class<?> klass) {
        this.parents.remove(klass);
    }

    public List<Runner> runners(Class<?> parent, Class<?>[] children) throws InitializationError {
        addParent(parent);
        try {
            List<Runner> runners = runners(children);
            removeParent(parent);
            return runners;
        } catch (Throwable th) {
            removeParent(parent);
            throw th;
        }
    }

    public List<Runner> runners(Class<?> parent, List<Class<?>> children) throws InitializationError {
        return runners(parent, (Class[]) children.toArray(new Class[0]));
    }

    private List<Runner> runners(Class<?>[] children) {
        List<Runner> runners = new ArrayList<>();
        for (Class<?> each : children) {
            Runner childRunner = safeRunnerForClass(each);
            if (childRunner != null) {
                runners.add(childRunner);
            }
        }
        return runners;
    }
}
