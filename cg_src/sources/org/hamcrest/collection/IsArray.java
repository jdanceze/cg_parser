package org.hamcrest.collection;

import java.util.Arrays;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/collection/IsArray.class */
public class IsArray<T> extends TypeSafeMatcher<T[]> {
    private final Matcher<? super T>[] elementMatchers;

    public IsArray(Matcher<? super T>[] elementMatchers) {
        this.elementMatchers = (Matcher[]) elementMatchers.clone();
    }

    @Override // org.hamcrest.TypeSafeMatcher
    public boolean matchesSafely(T[] array) {
        if (array.length != this.elementMatchers.length) {
            return false;
        }
        for (int i = 0; i < array.length; i++) {
            if (!this.elementMatchers[i].matches(array[i])) {
                return false;
            }
        }
        return true;
    }

    @Override // org.hamcrest.TypeSafeMatcher
    public void describeMismatchSafely(T[] actual, Description mismatchDescription) {
        if (actual.length != this.elementMatchers.length) {
            mismatchDescription.appendText("array length was " + actual.length);
            return;
        }
        for (int i = 0; i < actual.length; i++) {
            if (!this.elementMatchers[i].matches(actual[i])) {
                mismatchDescription.appendText("element " + i + " was ").appendValue(actual[i]);
                return;
            }
        }
    }

    @Override // org.hamcrest.SelfDescribing
    public void describeTo(Description description) {
        description.appendList(descriptionStart(), descriptionSeparator(), descriptionEnd(), Arrays.asList(this.elementMatchers));
    }

    protected String descriptionStart() {
        return "[";
    }

    protected String descriptionSeparator() {
        return ", ";
    }

    protected String descriptionEnd() {
        return "]";
    }

    @Factory
    public static <T> IsArray<T> array(Matcher<? super T>... elementMatchers) {
        return new IsArray<>(elementMatchers);
    }
}
