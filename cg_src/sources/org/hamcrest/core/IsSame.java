package org.hamcrest.core;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/core/IsSame.class
  gencallgraphv3.jar:hamcrest-core-1.3.jar:org/hamcrest/core/IsSame.class
 */
/* loaded from: gencallgraphv3.jar:org.hamcrest.core_1.3.0.v20180420-1519.jar:org/hamcrest/core/IsSame.class */
public class IsSame<T> extends BaseMatcher<T> {
    private final T object;

    public IsSame(T object) {
        this.object = object;
    }

    @Override // org.hamcrest.Matcher
    public boolean matches(Object arg) {
        return arg == this.object;
    }

    @Override // org.hamcrest.SelfDescribing
    public void describeTo(Description description) {
        description.appendText("sameInstance(").appendValue(this.object).appendText(")");
    }

    @Factory
    public static <T> Matcher<T> sameInstance(T target) {
        return new IsSame(target);
    }

    @Factory
    public static <T> Matcher<T> theInstance(T target) {
        return new IsSame(target);
    }
}
