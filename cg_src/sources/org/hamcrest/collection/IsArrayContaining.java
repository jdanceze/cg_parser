package org.hamcrest.collection;

import java.util.Arrays;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsEqual;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/collection/IsArrayContaining.class */
public class IsArrayContaining<T> extends TypeSafeMatcher<T[]> {
    private final Matcher<? super T> elementMatcher;

    public IsArrayContaining(Matcher<? super T> elementMatcher) {
        this.elementMatcher = elementMatcher;
    }

    @Override // org.hamcrest.TypeSafeMatcher
    public boolean matchesSafely(T[] array) {
        for (T item : array) {
            if (this.elementMatcher.matches(item)) {
                return true;
            }
        }
        return false;
    }

    @Override // org.hamcrest.TypeSafeMatcher
    public void describeMismatchSafely(T[] item, Description mismatchDescription) {
        super.describeMismatch(Arrays.asList(item), mismatchDescription);
    }

    @Override // org.hamcrest.SelfDescribing
    public void describeTo(Description description) {
        description.appendText("an array containing ").appendDescriptionOf(this.elementMatcher);
    }

    @Factory
    public static <T> Matcher<T[]> hasItemInArray(Matcher<? super T> elementMatcher) {
        return new IsArrayContaining(elementMatcher);
    }

    @Factory
    public static <T> Matcher<T[]> hasItemInArray(T element) {
        Matcher<? super T> matcher = IsEqual.equalTo(element);
        return hasItemInArray((Matcher) matcher);
    }
}
