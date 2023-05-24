package org.hamcrest.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.hamcrest.core.IsEqual;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/collection/IsIterableContainingInOrder.class */
public class IsIterableContainingInOrder<E> extends TypeSafeDiagnosingMatcher<Iterable<? extends E>> {
    private final List<Matcher<? super E>> matchers;

    @Override // org.hamcrest.TypeSafeDiagnosingMatcher
    protected /* bridge */ /* synthetic */ boolean matchesSafely(Object x0, Description x1) {
        return matchesSafely((Iterable) ((Iterable) x0), x1);
    }

    public IsIterableContainingInOrder(List<Matcher<? super E>> matchers) {
        this.matchers = matchers;
    }

    protected boolean matchesSafely(Iterable<? extends E> iterable, Description mismatchDescription) {
        MatchSeries<E> matchSeries = new MatchSeries<>(this.matchers, mismatchDescription);
        for (E item : iterable) {
            if (!matchSeries.matches(item)) {
                return false;
            }
        }
        return matchSeries.isFinished();
    }

    @Override // org.hamcrest.SelfDescribing
    public void describeTo(Description description) {
        description.appendText("iterable containing ").appendList("[", ", ", "]", this.matchers);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/collection/IsIterableContainingInOrder$MatchSeries.class */
    public static class MatchSeries<F> {
        public final List<Matcher<? super F>> matchers;
        private final Description mismatchDescription;
        public int nextMatchIx = 0;

        public MatchSeries(List<Matcher<? super F>> matchers, Description mismatchDescription) {
            this.mismatchDescription = mismatchDescription;
            if (matchers.isEmpty()) {
                throw new IllegalArgumentException("Should specify at least one expected element");
            }
            this.matchers = matchers;
        }

        public boolean matches(F item) {
            return isNotSurplus(item) && isMatched(item);
        }

        public boolean isFinished() {
            if (this.nextMatchIx < this.matchers.size()) {
                this.mismatchDescription.appendText("No item matched: ").appendDescriptionOf(this.matchers.get(this.nextMatchIx));
                return false;
            }
            return true;
        }

        private boolean isMatched(F item) {
            Matcher<? super F> matcher = this.matchers.get(this.nextMatchIx);
            if (!matcher.matches(item)) {
                describeMismatch(matcher, item);
                return false;
            }
            this.nextMatchIx++;
            return true;
        }

        private boolean isNotSurplus(F item) {
            if (this.matchers.size() <= this.nextMatchIx) {
                this.mismatchDescription.appendText("Not matched: ").appendValue(item);
                return false;
            }
            return true;
        }

        private void describeMismatch(Matcher<? super F> matcher, F item) {
            this.mismatchDescription.appendText("item " + this.nextMatchIx + ": ");
            matcher.describeMismatch(item, this.mismatchDescription);
        }
    }

    @Factory
    public static <E> Matcher<Iterable<? extends E>> contains(E... items) {
        List<Matcher<? super E>> matchers = new ArrayList<>();
        for (E item : items) {
            matchers.add(IsEqual.equalTo(item));
        }
        return contains(matchers);
    }

    @Factory
    public static <E> Matcher<Iterable<? extends E>> contains(Matcher<? super E> itemMatcher) {
        return contains(new ArrayList(Arrays.asList(itemMatcher)));
    }

    @Factory
    public static <E> Matcher<Iterable<? extends E>> contains(Matcher<? super E>... itemMatchers) {
        return contains(Arrays.asList(itemMatchers));
    }

    @Factory
    public static <E> Matcher<Iterable<? extends E>> contains(List<Matcher<? super E>> itemMatchers) {
        return new IsIterableContainingInOrder(itemMatchers);
    }
}
