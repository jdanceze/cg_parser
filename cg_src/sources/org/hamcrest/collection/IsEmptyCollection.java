package org.hamcrest.collection;

import java.util.Collection;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/collection/IsEmptyCollection.class */
public class IsEmptyCollection<E> extends TypeSafeMatcher<Collection<? extends E>> {
    @Override // org.hamcrest.TypeSafeMatcher
    public /* bridge */ /* synthetic */ void describeMismatchSafely(Object x0, Description x1) {
        describeMismatchSafely((Collection) ((Collection) x0), x1);
    }

    @Override // org.hamcrest.TypeSafeMatcher
    public /* bridge */ /* synthetic */ boolean matchesSafely(Object x0) {
        return matchesSafely((Collection) ((Collection) x0));
    }

    public boolean matchesSafely(Collection<? extends E> item) {
        return item.isEmpty();
    }

    public void describeMismatchSafely(Collection<? extends E> item, Description mismatchDescription) {
        mismatchDescription.appendValue(item);
    }

    @Override // org.hamcrest.SelfDescribing
    public void describeTo(Description description) {
        description.appendText("an empty collection");
    }

    @Factory
    public static <E> Matcher<Collection<? extends E>> empty() {
        return new IsEmptyCollection();
    }

    @Factory
    public static <E> Matcher<Collection<E>> emptyCollectionOf(Class<E> type) {
        Matcher<Collection<E>> result = empty();
        return result;
    }
}
