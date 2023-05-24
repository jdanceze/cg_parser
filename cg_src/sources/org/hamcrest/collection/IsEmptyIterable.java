package org.hamcrest.collection;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/collection/IsEmptyIterable.class */
public class IsEmptyIterable<E> extends TypeSafeMatcher<Iterable<? extends E>> {
    @Override // org.hamcrest.TypeSafeMatcher
    public /* bridge */ /* synthetic */ void describeMismatchSafely(Object x0, Description x1) {
        describeMismatchSafely((Iterable) ((Iterable) x0), x1);
    }

    @Override // org.hamcrest.TypeSafeMatcher
    public /* bridge */ /* synthetic */ boolean matchesSafely(Object x0) {
        return matchesSafely((Iterable) ((Iterable) x0));
    }

    public boolean matchesSafely(Iterable<? extends E> iterable) {
        return !iterable.iterator().hasNext();
    }

    public void describeMismatchSafely(Iterable<? extends E> iter, Description mismatchDescription) {
        mismatchDescription.appendValueList("[", ",", "]", iter);
    }

    @Override // org.hamcrest.SelfDescribing
    public void describeTo(Description description) {
        description.appendText("an empty iterable");
    }

    @Factory
    public static <E> Matcher<Iterable<? extends E>> emptyIterable() {
        return new IsEmptyIterable();
    }

    @Factory
    public static <E> Matcher<Iterable<E>> emptyIterableOf(Class<E> type) {
        Matcher<Iterable<E>> result = emptyIterable();
        return result;
    }
}
