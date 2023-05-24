package org.hamcrest.core;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/core/IsAnything.class
  gencallgraphv3.jar:hamcrest-core-1.3.jar:org/hamcrest/core/IsAnything.class
 */
/* loaded from: gencallgraphv3.jar:org.hamcrest.core_1.3.0.v20180420-1519.jar:org/hamcrest/core/IsAnything.class */
public class IsAnything<T> extends BaseMatcher<T> {
    private final String message;

    public IsAnything() {
        this("ANYTHING");
    }

    public IsAnything(String message) {
        this.message = message;
    }

    @Override // org.hamcrest.Matcher
    public boolean matches(Object o) {
        return true;
    }

    @Override // org.hamcrest.SelfDescribing
    public void describeTo(Description description) {
        description.appendText(this.message);
    }

    @Factory
    public static Matcher<Object> anything() {
        return new IsAnything();
    }

    @Factory
    public static Matcher<Object> anything(String description) {
        return new IsAnything(description);
    }
}
