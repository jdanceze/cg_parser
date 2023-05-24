package org.hamcrest.core;

import java.util.ArrayList;
import java.util.List;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/core/IsCollectionContaining.class
  gencallgraphv3.jar:hamcrest-core-1.3.jar:org/hamcrest/core/IsCollectionContaining.class
 */
/* loaded from: gencallgraphv3.jar:org.hamcrest.core_1.3.0.v20180420-1519.jar:org/hamcrest/core/IsCollectionContaining.class */
public class IsCollectionContaining<T> extends TypeSafeDiagnosingMatcher<Iterable<? super T>> {
    private final Matcher<? super T> elementMatcher;

    @Override // org.hamcrest.TypeSafeDiagnosingMatcher
    protected /* bridge */ /* synthetic */ boolean matchesSafely(Object x0, Description x1) {
        return matchesSafely((Iterable) ((Iterable) x0), x1);
    }

    public IsCollectionContaining(Matcher<? super T> elementMatcher) {
        this.elementMatcher = elementMatcher;
    }

    protected boolean matchesSafely(Iterable<? super T> collection, Description mismatchDescription) {
        boolean isPastFirst = false;
        for (Object item : collection) {
            if (this.elementMatcher.matches(item)) {
                return true;
            }
            if (isPastFirst) {
                mismatchDescription.appendText(", ");
            }
            this.elementMatcher.describeMismatch(item, mismatchDescription);
            isPastFirst = true;
        }
        return false;
    }

    @Override // org.hamcrest.SelfDescribing
    public void describeTo(Description description) {
        description.appendText("a collection containing ").appendDescriptionOf(this.elementMatcher);
    }

    @Factory
    public static <T> Matcher<Iterable<? super T>> hasItem(Matcher<? super T> itemMatcher) {
        return new IsCollectionContaining(itemMatcher);
    }

    @Factory
    public static <T> Matcher<Iterable<? super T>> hasItem(T item) {
        return new IsCollectionContaining(IsEqual.equalTo(item));
    }

    @Factory
    public static <T> Matcher<Iterable<T>> hasItems(Matcher<? super T>... itemMatchers) {
        List<Matcher<? super Iterable<T>>> all = new ArrayList<>(itemMatchers.length);
        for (Matcher<? super T> elementMatcher : itemMatchers) {
            all.add(new IsCollectionContaining<>(elementMatcher));
        }
        return AllOf.allOf(all);
    }

    @Factory
    public static <T> Matcher<Iterable<T>> hasItems(T... items) {
        List<Matcher<? super Iterable<T>>> all = new ArrayList<>(items.length);
        for (T element : items) {
            all.add(hasItem(element));
        }
        return AllOf.allOf(all);
    }
}
