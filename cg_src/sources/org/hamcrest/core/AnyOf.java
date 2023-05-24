package org.hamcrest.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/core/AnyOf.class
  gencallgraphv3.jar:hamcrest-core-1.3.jar:org/hamcrest/core/AnyOf.class
 */
/* loaded from: gencallgraphv3.jar:org.hamcrest.core_1.3.0.v20180420-1519.jar:org/hamcrest/core/AnyOf.class */
public class AnyOf<T> extends ShortcutCombination<T> {
    @Override // org.hamcrest.core.ShortcutCombination
    public /* bridge */ /* synthetic */ void describeTo(Description x0, String x1) {
        super.describeTo(x0, x1);
    }

    public AnyOf(Iterable<Matcher<? super T>> matchers) {
        super(matchers);
    }

    @Override // org.hamcrest.core.ShortcutCombination, org.hamcrest.Matcher
    public boolean matches(Object o) {
        return matches(o, true);
    }

    @Override // org.hamcrest.core.ShortcutCombination, org.hamcrest.SelfDescribing
    public void describeTo(Description description) {
        describeTo(description, "or");
    }

    @Factory
    public static <T> AnyOf<T> anyOf(Iterable<Matcher<? super T>> matchers) {
        return new AnyOf<>(matchers);
    }

    @Factory
    public static <T> AnyOf<T> anyOf(Matcher<? super T>... matchers) {
        return anyOf(Arrays.asList(matchers));
    }

    @Factory
    public static <T> AnyOf<T> anyOf(Matcher<T> first, Matcher<? super T> second) {
        List<Matcher<? super T>> matchers = new ArrayList<>();
        matchers.add(first);
        matchers.add(second);
        return anyOf(matchers);
    }

    @Factory
    public static <T> AnyOf<T> anyOf(Matcher<T> first, Matcher<? super T> second, Matcher<? super T> third) {
        List<Matcher<? super T>> matchers = new ArrayList<>();
        matchers.add(first);
        matchers.add(second);
        matchers.add(third);
        return anyOf(matchers);
    }

    @Factory
    public static <T> AnyOf<T> anyOf(Matcher<T> first, Matcher<? super T> second, Matcher<? super T> third, Matcher<? super T> fourth) {
        List<Matcher<? super T>> matchers = new ArrayList<>();
        matchers.add(first);
        matchers.add(second);
        matchers.add(third);
        matchers.add(fourth);
        return anyOf(matchers);
    }

    @Factory
    public static <T> AnyOf<T> anyOf(Matcher<T> first, Matcher<? super T> second, Matcher<? super T> third, Matcher<? super T> fourth, Matcher<? super T> fifth) {
        List<Matcher<? super T>> matchers = new ArrayList<>();
        matchers.add(first);
        matchers.add(second);
        matchers.add(third);
        matchers.add(fourth);
        matchers.add(fifth);
        return anyOf(matchers);
    }

    @Factory
    public static <T> AnyOf<T> anyOf(Matcher<T> first, Matcher<? super T> second, Matcher<? super T> third, Matcher<? super T> fourth, Matcher<? super T> fifth, Matcher<? super T> sixth) {
        List<Matcher<? super T>> matchers = new ArrayList<>();
        matchers.add(first);
        matchers.add(second);
        matchers.add(third);
        matchers.add(fourth);
        matchers.add(fifth);
        matchers.add(sixth);
        return anyOf(matchers);
    }
}
