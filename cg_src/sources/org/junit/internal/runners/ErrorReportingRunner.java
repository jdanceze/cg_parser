package org.junit.internal.runners;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InvalidTestClassError;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/internal/runners/ErrorReportingRunner.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/internal/runners/ErrorReportingRunner.class */
public class ErrorReportingRunner extends Runner {
    private final List<Throwable> causes;
    private final String classNames;

    public ErrorReportingRunner(Class<?> testClass, Throwable cause) {
        this(cause, testClass);
    }

    public ErrorReportingRunner(Throwable cause, Class<?>... testClasses) {
        if (testClasses == null || testClasses.length == 0) {
            throw new NullPointerException("Test classes cannot be null or empty");
        }
        for (Class<?> testClass : testClasses) {
            if (testClass == null) {
                throw new NullPointerException("Test class cannot be null");
            }
        }
        this.classNames = getClassNames(testClasses);
        this.causes = getCauses(cause);
    }

    @Override // org.junit.runner.Runner, org.junit.runner.Describable
    public Description getDescription() {
        Description description = Description.createSuiteDescription(this.classNames, new Annotation[0]);
        for (Throwable th : this.causes) {
            description.addChild(describeCause());
        }
        return description;
    }

    @Override // org.junit.runner.Runner
    public void run(RunNotifier notifier) {
        for (Throwable each : this.causes) {
            runCause(each, notifier);
        }
    }

    private String getClassNames(Class<?>... testClasses) {
        StringBuilder builder = new StringBuilder();
        for (Class<?> testClass : testClasses) {
            if (builder.length() != 0) {
                builder.append(", ");
            }
            builder.append(testClass.getName());
        }
        return builder.toString();
    }

    private List<Throwable> getCauses(Throwable cause) {
        if (cause instanceof InvocationTargetException) {
            return getCauses(cause.getCause());
        }
        if (cause instanceof InvalidTestClassError) {
            return Collections.singletonList(cause);
        }
        if (cause instanceof org.junit.runners.model.InitializationError) {
            return ((org.junit.runners.model.InitializationError) cause).getCauses();
        }
        if (cause instanceof InitializationError) {
            return ((InitializationError) cause).getCauses();
        }
        return Collections.singletonList(cause);
    }

    private Description describeCause() {
        return Description.createTestDescription(this.classNames, "initializationError", new Annotation[0]);
    }

    private void runCause(Throwable child, RunNotifier notifier) {
        Description description = describeCause();
        notifier.fireTestStarted(description);
        notifier.fireTestFailure(new Failure(description, child));
        notifier.fireTestFinished(description);
    }
}
