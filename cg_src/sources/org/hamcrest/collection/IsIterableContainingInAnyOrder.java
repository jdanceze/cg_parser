package org.hamcrest.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.hamcrest.core.IsEqual;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/collection/IsIterableContainingInAnyOrder.class */
public class IsIterableContainingInAnyOrder<T> extends TypeSafeDiagnosingMatcher<Iterable<? extends T>> {
    private final Collection<Matcher<? super T>> matchers;

    @Override // org.hamcrest.TypeSafeDiagnosingMatcher
    protected /* bridge */ /* synthetic */ boolean matchesSafely(Object x0, Description x1) {
        return matchesSafely((Iterable) ((Iterable) x0), x1);
    }

    public IsIterableContainingInAnyOrder(Collection<Matcher<? super T>> matchers) {
        this.matchers = matchers;
    }

    protected boolean matchesSafely(Iterable<? extends T> items, Description mismatchDescription) {
        Matching<T> matching = new Matching<>(this.matchers, mismatchDescription);
        for (T item : items) {
            if (!matching.matches(item)) {
                return false;
            }
        }
        return matching.isFinished(items);
    }

    @Override // org.hamcrest.SelfDescribing
    public void describeTo(Description description) {
        description.appendText("iterable over ").appendList("[", ", ", "]", this.matchers).appendText(" in any order");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/collection/IsIterableContainingInAnyOrder$Matching.class */
    public static class Matching<S> {
        private final Collection<Matcher<? super S>> matchers;
        private final Description mismatchDescription;

        public Matching(Collection<Matcher<? super S>> matchers, Description mismatchDescription) {
            this.matchers = new ArrayList(matchers);
            this.mismatchDescription = mismatchDescription;
        }

        public boolean matches(S item) {
            return isNotSurplus(item) && isMatched(item);
        }

        public boolean isFinished(Iterable<? extends S> items) {
            if (this.matchers.isEmpty()) {
                return true;
            }
            this.mismatchDescription.appendText("No item matches: ").appendList("", ", ", "", this.matchers).appendText(" in ").appendValueList("[", ", ", "]", items);
            return false;
        }

        private boolean isNotSurplus(S item) {
            if (this.matchers.isEmpty()) {
                this.mismatchDescription.appendText("Not matched: ").appendValue(item);
                return false;
            }
            return true;
        }

        private boolean isMatched(S item) {
            for (Matcher<? super S> matcher : this.matchers) {
                if (matcher.matches(item)) {
                    this.matchers.remove(matcher);
                    return true;
                }
            }
            this.mismatchDescription.appendText("Not matched: ").appendValue(item);
            return false;
        }
    }

    @Factory
    @Deprecated
    public static <E> Matcher<Iterable<? extends E>> containsInAnyOrder(Matcher<? super E> itemMatcher) {
        return containsInAnyOrder(new ArrayList(Arrays.asList(itemMatcher)));
    }

    @Factory
    public static <T> Matcher<Iterable<? extends T>> containsInAnyOrder(Matcher<? super T>... itemMatchers) {
        return containsInAnyOrder(Arrays.asList(itemMatchers));
    }

    @Factory
    public static <T> Matcher<Iterable<? extends T>> containsInAnyOrder(T... items) {
        List<Matcher<? super T>> matchers = new ArrayList<>();
        for (T item : items) {
            matchers.add(IsEqual.equalTo(item));
        }
        return new IsIterableContainingInAnyOrder(matchers);
    }

    @Factory
    public static <T> Matcher<Iterable<? extends T>> containsInAnyOrder(Collection<Matcher<? super T>> itemMatchers) {
        return new IsIterableContainingInAnyOrder(itemMatchers);
    }
}
