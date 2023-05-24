package org.hamcrest.object;

import org.hamcrest.Factory;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsEqual;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/object/HasToString.class */
public class HasToString<T> extends FeatureMatcher<T, String> {
    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.hamcrest.FeatureMatcher
    protected /* bridge */ /* synthetic */ String featureValueOf(Object x0) {
        return featureValueOf((HasToString<T>) x0);
    }

    public HasToString(Matcher<? super String> toStringMatcher) {
        super(toStringMatcher, "with toString()", "toString()");
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.hamcrest.FeatureMatcher
    protected String featureValueOf(T actual) {
        return String.valueOf(actual);
    }

    @Factory
    public static <T> Matcher<T> hasToString(Matcher<? super String> toStringMatcher) {
        return new HasToString(toStringMatcher);
    }

    @Factory
    public static <T> Matcher<T> hasToString(String expectedToString) {
        return new HasToString(IsEqual.equalTo(expectedToString));
    }
}
