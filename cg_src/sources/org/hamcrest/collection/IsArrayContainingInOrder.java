package org.hamcrest.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsEqual;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/collection/IsArrayContainingInOrder.class */
public class IsArrayContainingInOrder<E> extends TypeSafeMatcher<E[]> {
    private final Collection<Matcher<? super E>> matchers;
    private final IsIterableContainingInOrder<E> iterableMatcher;

    public IsArrayContainingInOrder(List<Matcher<? super E>> matchers) {
        this.iterableMatcher = new IsIterableContainingInOrder<>(matchers);
        this.matchers = matchers;
    }

    @Override // org.hamcrest.TypeSafeMatcher
    public boolean matchesSafely(E[] item) {
        return this.iterableMatcher.matches(Arrays.asList(item));
    }

    @Override // org.hamcrest.TypeSafeMatcher
    public void describeMismatchSafely(E[] item, Description mismatchDescription) {
        this.iterableMatcher.describeMismatch(Arrays.asList(item), mismatchDescription);
    }

    @Override // org.hamcrest.SelfDescribing
    public void describeTo(Description description) {
        description.appendList("[", ", ", "]", this.matchers);
    }

    @Factory
    public static <E> Matcher<E[]> arrayContaining(E... items) {
        List<Matcher<? super E>> matchers = new ArrayList<>();
        for (E item : items) {
            matchers.add(IsEqual.equalTo(item));
        }
        return arrayContaining(matchers);
    }

    @Factory
    public static <E> Matcher<E[]> arrayContaining(Matcher<? super E>... itemMatchers) {
        return arrayContaining(Arrays.asList(itemMatchers));
    }

    @Factory
    public static <E> Matcher<E[]> arrayContaining(List<Matcher<? super E>> itemMatchers) {
        return new IsArrayContainingInOrder(itemMatchers);
    }
}
