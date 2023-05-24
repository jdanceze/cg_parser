package org.hamcrest.core;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import soot.jimple.Jimple;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/core/IsNull.class
  gencallgraphv3.jar:hamcrest-core-1.3.jar:org/hamcrest/core/IsNull.class
 */
/* loaded from: gencallgraphv3.jar:org.hamcrest.core_1.3.0.v20180420-1519.jar:org/hamcrest/core/IsNull.class */
public class IsNull<T> extends BaseMatcher<T> {
    @Override // org.hamcrest.Matcher
    public boolean matches(Object o) {
        return o == null;
    }

    @Override // org.hamcrest.SelfDescribing
    public void describeTo(Description description) {
        description.appendText(Jimple.NULL);
    }

    @Factory
    public static Matcher<Object> nullValue() {
        return new IsNull();
    }

    @Factory
    public static Matcher<Object> notNullValue() {
        return IsNot.not((Matcher) nullValue());
    }

    @Factory
    public static <T> Matcher<T> nullValue(Class<T> type) {
        return new IsNull();
    }

    @Factory
    public static <T> Matcher<T> notNullValue(Class<T> type) {
        return IsNot.not(nullValue(type));
    }
}
