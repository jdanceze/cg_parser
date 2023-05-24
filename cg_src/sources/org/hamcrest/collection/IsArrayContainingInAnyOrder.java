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
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/collection/IsArrayContainingInAnyOrder.class */
public class IsArrayContainingInAnyOrder<E> extends TypeSafeMatcher<E[]> {
    private final IsIterableContainingInAnyOrder<E> iterableMatcher;
    private final Collection<Matcher<? super E>> matchers;

    public IsArrayContainingInAnyOrder(Collection<Matcher<? super E>> matchers) {
        this.iterableMatcher = new IsIterableContainingInAnyOrder<>(matchers);
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
        description.appendList("[", ", ", "]", this.matchers).appendText(" in any order");
    }

    @Factory
    public static <E> Matcher<E[]> arrayContainingInAnyOrder(Matcher<? super E>... itemMatchers) {
        return arrayContainingInAnyOrder(Arrays.asList(itemMatchers));
    }

    @Factory
    public static <E> Matcher<E[]> arrayContainingInAnyOrder(Collection<Matcher<? super E>> itemMatchers) {
        return new IsArrayContainingInAnyOrder(itemMatchers);
    }

    @Factory
    public static <E> Matcher<E[]> arrayContainingInAnyOrder(E... items) {
        List<Matcher<? super E>> matchers = new ArrayList<>();
        for (E item : items) {
            matchers.add(IsEqual.equalTo(item));
        }
        return new IsArrayContainingInAnyOrder(matchers);
    }
}
