package org.hamcrest.core;

import java.util.ArrayList;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/core/CombinableMatcher.class
  gencallgraphv3.jar:hamcrest-core-1.3.jar:org/hamcrest/core/CombinableMatcher.class
 */
/* loaded from: gencallgraphv3.jar:org.hamcrest.core_1.3.0.v20180420-1519.jar:org/hamcrest/core/CombinableMatcher.class */
public class CombinableMatcher<T> extends TypeSafeDiagnosingMatcher<T> {
    private final Matcher<? super T> matcher;

    public CombinableMatcher(Matcher<? super T> matcher) {
        this.matcher = matcher;
    }

    @Override // org.hamcrest.TypeSafeDiagnosingMatcher
    protected boolean matchesSafely(T item, Description mismatch) {
        if (!this.matcher.matches(item)) {
            this.matcher.describeMismatch(item, mismatch);
            return false;
        }
        return true;
    }

    @Override // org.hamcrest.SelfDescribing
    public void describeTo(Description description) {
        description.appendDescriptionOf(this.matcher);
    }

    public CombinableMatcher<T> and(Matcher<? super T> other) {
        return new CombinableMatcher<>(new AllOf(templatedListWith(other)));
    }

    public CombinableMatcher<T> or(Matcher<? super T> other) {
        return new CombinableMatcher<>(new AnyOf(templatedListWith(other)));
    }

    private ArrayList<Matcher<? super T>> templatedListWith(Matcher<? super T> other) {
        ArrayList<Matcher<? super T>> matchers = new ArrayList<>();
        matchers.add(this.matcher);
        matchers.add(other);
        return matchers;
    }

    @Factory
    public static <LHS> CombinableBothMatcher<LHS> both(Matcher<? super LHS> matcher) {
        return new CombinableBothMatcher<>(matcher);
    }

    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/core/CombinableMatcher$CombinableBothMatcher.class
      gencallgraphv3.jar:hamcrest-core-1.3.jar:org/hamcrest/core/CombinableMatcher$CombinableBothMatcher.class
     */
    /* loaded from: gencallgraphv3.jar:org.hamcrest.core_1.3.0.v20180420-1519.jar:org/hamcrest/core/CombinableMatcher$CombinableBothMatcher.class */
    public static final class CombinableBothMatcher<X> {
        private final Matcher<? super X> first;

        public CombinableBothMatcher(Matcher<? super X> matcher) {
            this.first = matcher;
        }

        public CombinableMatcher<X> and(Matcher<? super X> other) {
            return new CombinableMatcher(this.first).and(other);
        }
    }

    @Factory
    public static <LHS> CombinableEitherMatcher<LHS> either(Matcher<? super LHS> matcher) {
        return new CombinableEitherMatcher<>(matcher);
    }

    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/core/CombinableMatcher$CombinableEitherMatcher.class
      gencallgraphv3.jar:hamcrest-core-1.3.jar:org/hamcrest/core/CombinableMatcher$CombinableEitherMatcher.class
     */
    /* loaded from: gencallgraphv3.jar:org.hamcrest.core_1.3.0.v20180420-1519.jar:org/hamcrest/core/CombinableMatcher$CombinableEitherMatcher.class */
    public static final class CombinableEitherMatcher<X> {
        private final Matcher<? super X> first;

        public CombinableEitherMatcher(Matcher<? super X> matcher) {
            this.first = matcher;
        }

        public CombinableMatcher<X> or(Matcher<? super X> other) {
            return new CombinableMatcher(this.first).or(other);
        }
    }
}
