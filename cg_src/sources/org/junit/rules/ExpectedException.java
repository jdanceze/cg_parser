package org.junit.rules;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Assert;
import org.junit.internal.matchers.ThrowableCauseMatcher;
import org.junit.internal.matchers.ThrowableMessageMatcher;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/rules/ExpectedException.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/rules/ExpectedException.class */
public class ExpectedException implements TestRule {
    private final ExpectedExceptionMatcherBuilder matcherBuilder = new ExpectedExceptionMatcherBuilder();
    private String missingExceptionMessage = "Expected test to throw %s";

    @Deprecated
    public static ExpectedException none() {
        return new ExpectedException();
    }

    private ExpectedException() {
    }

    @Deprecated
    public ExpectedException handleAssertionErrors() {
        return this;
    }

    @Deprecated
    public ExpectedException handleAssumptionViolatedExceptions() {
        return this;
    }

    public ExpectedException reportMissingExceptionWithMessage(String message) {
        this.missingExceptionMessage = message;
        return this;
    }

    @Override // org.junit.rules.TestRule
    public Statement apply(Statement base, Description description) {
        return new ExpectedExceptionStatement(base);
    }

    public void expect(Matcher<?> matcher) {
        this.matcherBuilder.add(matcher);
    }

    public void expect(Class<? extends Throwable> type) {
        expect(CoreMatchers.instanceOf(type));
    }

    public void expectMessage(String substring) {
        expectMessage(CoreMatchers.containsString(substring));
    }

    public void expectMessage(Matcher<String> matcher) {
        expect(ThrowableMessageMatcher.hasMessage(matcher));
    }

    public void expectCause(Matcher<?> expectedCause) {
        expect(ThrowableCauseMatcher.hasCause(expectedCause));
    }

    public final boolean isAnyExceptionExpected() {
        return this.matcherBuilder.expectsThrowable();
    }

    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:junit-4.13.2.jar:org/junit/rules/ExpectedException$ExpectedExceptionStatement.class
     */
    /* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/rules/ExpectedException$ExpectedExceptionStatement.class */
    private class ExpectedExceptionStatement extends Statement {
        private final Statement next;

        public ExpectedExceptionStatement(Statement base) {
            this.next = base;
        }

        @Override // org.junit.runners.model.Statement
        public void evaluate() throws Throwable {
            try {
                this.next.evaluate();
                if (ExpectedException.this.isAnyExceptionExpected()) {
                    ExpectedException.this.failDueToMissingException();
                }
            } catch (Throwable e) {
                ExpectedException.this.handleException(e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleException(Throwable e) throws Throwable {
        if (isAnyExceptionExpected()) {
            Assert.assertThat(e, this.matcherBuilder.build());
            return;
        }
        throw e;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void failDueToMissingException() throws AssertionError {
        Assert.fail(missingExceptionMessage());
    }

    private String missingExceptionMessage() {
        String expectation = StringDescription.toString(this.matcherBuilder.build());
        return String.format(this.missingExceptionMessage, expectation);
    }
}
