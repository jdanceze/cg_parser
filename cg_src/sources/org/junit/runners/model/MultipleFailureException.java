package org.junit.runners.model;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.TestCouldNotBeSkippedException;
import org.junit.internal.AssumptionViolatedException;
import org.junit.internal.Throwables;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/runners/model/MultipleFailureException.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/runners/model/MultipleFailureException.class */
public class MultipleFailureException extends Exception {
    private static final long serialVersionUID = 1;
    private final List<Throwable> fErrors;

    public MultipleFailureException(List<Throwable> errors) {
        if (errors.isEmpty()) {
            throw new IllegalArgumentException("List of Throwables must not be empty");
        }
        this.fErrors = new ArrayList(errors.size());
        for (Throwable error : errors) {
            if (error instanceof AssumptionViolatedException) {
                error = new TestCouldNotBeSkippedException((AssumptionViolatedException) error);
            }
            this.fErrors.add(error);
        }
    }

    public List<Throwable> getFailures() {
        return Collections.unmodifiableList(this.fErrors);
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        StringBuilder sb = new StringBuilder(String.format("There were %d errors:", Integer.valueOf(this.fErrors.size())));
        for (Throwable e : this.fErrors) {
            sb.append(String.format("%n  %s(%s)", e.getClass().getName(), e.getMessage()));
        }
        return sb.toString();
    }

    @Override // java.lang.Throwable
    public void printStackTrace() {
        for (Throwable e : this.fErrors) {
            e.printStackTrace();
        }
    }

    @Override // java.lang.Throwable
    public void printStackTrace(PrintStream s) {
        for (Throwable e : this.fErrors) {
            e.printStackTrace(s);
        }
    }

    @Override // java.lang.Throwable
    public void printStackTrace(PrintWriter s) {
        for (Throwable e : this.fErrors) {
            e.printStackTrace(s);
        }
    }

    public static void assertEmpty(List<Throwable> errors) throws Exception {
        if (errors.isEmpty()) {
            return;
        }
        if (errors.size() == 1) {
            throw Throwables.rethrowAsException(errors.get(0));
        }
        throw new org.junit.internal.runners.model.MultipleFailureException(errors);
    }
}
