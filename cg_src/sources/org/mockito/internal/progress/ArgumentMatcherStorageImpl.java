package org.mockito.internal.progress;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.exceptions.Reporter;
import org.mockito.internal.matchers.And;
import org.mockito.internal.matchers.LocalizedMatcher;
import org.mockito.internal.matchers.Not;
import org.mockito.internal.matchers.Or;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/progress/ArgumentMatcherStorageImpl.class */
public class ArgumentMatcherStorageImpl implements ArgumentMatcherStorage {
    private static final int TWO_SUB_MATCHERS = 2;
    private static final int ONE_SUB_MATCHER = 1;
    private final Stack<LocalizedMatcher> matcherStack = new Stack<>();

    @Override // org.mockito.internal.progress.ArgumentMatcherStorage
    public void reportMatcher(ArgumentMatcher<?> matcher) {
        this.matcherStack.push(new LocalizedMatcher(matcher));
    }

    @Override // org.mockito.internal.progress.ArgumentMatcherStorage
    public List<LocalizedMatcher> pullLocalizedMatchers() {
        if (this.matcherStack.isEmpty()) {
            return Collections.emptyList();
        }
        List<LocalizedMatcher> lastMatchers = resetStack();
        return lastMatchers;
    }

    @Override // org.mockito.internal.progress.ArgumentMatcherStorage
    public void reportAnd() {
        assertStateFor("And(?)", 2);
        ArgumentMatcher<?> m1 = popMatcher();
        ArgumentMatcher<?> m2 = popMatcher();
        reportMatcher(new And(m1, m2));
    }

    @Override // org.mockito.internal.progress.ArgumentMatcherStorage
    public void reportOr() {
        assertStateFor("Or(?)", 2);
        ArgumentMatcher<?> m1 = popMatcher();
        ArgumentMatcher<?> m2 = popMatcher();
        reportMatcher(new Or(m1, m2));
    }

    @Override // org.mockito.internal.progress.ArgumentMatcherStorage
    public void reportNot() {
        assertStateFor("Not(?)", 1);
        ArgumentMatcher<?> m = popMatcher();
        reportMatcher(new Not(m));
    }

    @Override // org.mockito.internal.progress.ArgumentMatcherStorage
    public void validateState() {
        if (!this.matcherStack.isEmpty()) {
            List<LocalizedMatcher> lastMatchers = resetStack();
            throw Reporter.misplacedArgumentMatcher(lastMatchers);
        }
    }

    @Override // org.mockito.internal.progress.ArgumentMatcherStorage
    public void reset() {
        this.matcherStack.clear();
    }

    private void assertStateFor(String additionalMatcherName, int subMatchersCount) {
        if (this.matcherStack.isEmpty()) {
            throw Reporter.reportNoSubMatchersFound(additionalMatcherName);
        }
        if (this.matcherStack.size() < subMatchersCount) {
            List<LocalizedMatcher> lastMatchers = resetStack();
            throw Reporter.incorrectUseOfAdditionalMatchers(additionalMatcherName, subMatchersCount, lastMatchers);
        }
    }

    private ArgumentMatcher<?> popMatcher() {
        return this.matcherStack.pop().getMatcher();
    }

    private List<LocalizedMatcher> resetStack() {
        ArrayList<LocalizedMatcher> lastMatchers = new ArrayList<>(this.matcherStack);
        reset();
        return lastMatchers;
    }
}
