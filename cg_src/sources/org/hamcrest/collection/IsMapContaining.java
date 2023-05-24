package org.hamcrest.collection;

import java.util.Map;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsAnything;
import org.hamcrest.core.IsEqual;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/collection/IsMapContaining.class */
public class IsMapContaining<K, V> extends TypeSafeMatcher<Map<? extends K, ? extends V>> {
    private final Matcher<? super K> keyMatcher;
    private final Matcher<? super V> valueMatcher;

    @Override // org.hamcrest.TypeSafeMatcher
    public /* bridge */ /* synthetic */ void describeMismatchSafely(Object x0, Description x1) {
        describeMismatchSafely((Map) ((Map) x0), x1);
    }

    @Override // org.hamcrest.TypeSafeMatcher
    public /* bridge */ /* synthetic */ boolean matchesSafely(Object x0) {
        return matchesSafely((Map) ((Map) x0));
    }

    public IsMapContaining(Matcher<? super K> keyMatcher, Matcher<? super V> valueMatcher) {
        this.keyMatcher = keyMatcher;
        this.valueMatcher = valueMatcher;
    }

    public boolean matchesSafely(Map<? extends K, ? extends V> map) {
        for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            if (this.keyMatcher.matches(entry.getKey()) && this.valueMatcher.matches(entry.getValue())) {
                return true;
            }
        }
        return false;
    }

    public void describeMismatchSafely(Map<? extends K, ? extends V> map, Description mismatchDescription) {
        mismatchDescription.appendText("map was ").appendValueList("[", ", ", "]", map.entrySet());
    }

    @Override // org.hamcrest.SelfDescribing
    public void describeTo(Description description) {
        description.appendText("map containing [").appendDescriptionOf(this.keyMatcher).appendText("->").appendDescriptionOf(this.valueMatcher).appendText("]");
    }

    @Factory
    public static <K, V> Matcher<Map<? extends K, ? extends V>> hasEntry(Matcher<? super K> keyMatcher, Matcher<? super V> valueMatcher) {
        return new IsMapContaining(keyMatcher, valueMatcher);
    }

    @Factory
    public static <K, V> Matcher<Map<? extends K, ? extends V>> hasEntry(K key, V value) {
        return new IsMapContaining(IsEqual.equalTo(key), IsEqual.equalTo(value));
    }

    @Factory
    public static <K> Matcher<Map<? extends K, ?>> hasKey(Matcher<? super K> keyMatcher) {
        return new IsMapContaining(keyMatcher, IsAnything.anything());
    }

    @Factory
    public static <K> Matcher<Map<? extends K, ?>> hasKey(K key) {
        return new IsMapContaining(IsEqual.equalTo(key), IsAnything.anything());
    }

    @Factory
    public static <V> Matcher<Map<?, ? extends V>> hasValue(Matcher<? super V> valueMatcher) {
        return new IsMapContaining(IsAnything.anything(), valueMatcher);
    }

    @Factory
    public static <V> Matcher<Map<?, ? extends V>> hasValue(V value) {
        return new IsMapContaining(IsAnything.anything(), IsEqual.equalTo(value));
    }
}
