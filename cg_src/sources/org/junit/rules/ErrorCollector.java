package org.junit.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.function.ThrowingRunnable;
import org.junit.internal.AssumptionViolatedException;
import org.junit.runners.model.MultipleFailureException;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/rules/ErrorCollector.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/rules/ErrorCollector.class */
public class ErrorCollector extends Verifier {
    private List<Throwable> errors = new ArrayList();

    @Override // org.junit.rules.Verifier
    protected void verify() throws Throwable {
        MultipleFailureException.assertEmpty(this.errors);
    }

    public void addError(Throwable error) {
        if (error == null) {
            throw new NullPointerException("Error cannot be null");
        }
        if (error instanceof AssumptionViolatedException) {
            AssertionError e = new AssertionError(error.getMessage());
            e.initCause(error);
            this.errors.add(e);
            return;
        }
        this.errors.add(error);
    }

    public <T> void checkThat(T value, Matcher<T> matcher) {
        checkThat("", value, matcher);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T> void checkThat(final String reason, final T value, final Matcher<T> matcher) {
        checkSucceeds(new Callable<Object>() { // from class: org.junit.rules.ErrorCollector.1
            @Override // java.util.concurrent.Callable
            public Object call() throws Exception {
                Assert.assertThat(reason, value, matcher);
                return value;
            }
        });
    }

    public <T> T checkSucceeds(Callable<T> callable) {
        try {
            return callable.call();
        } catch (AssumptionViolatedException e) {
            AssertionError error = new AssertionError("Callable threw AssumptionViolatedException");
            error.initCause(e);
            addError(error);
            return null;
        } catch (Throwable e2) {
            addError(e2);
            return null;
        }
    }

    public void checkThrows(Class<? extends Throwable> expectedThrowable, ThrowingRunnable runnable) {
        try {
            Assert.assertThrows(expectedThrowable, runnable);
        } catch (AssertionError e) {
            addError(e);
        }
    }
}
