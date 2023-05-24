package org.hamcrest.object;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/object/IsCompatibleType.class */
public class IsCompatibleType<T> extends TypeSafeMatcher<Class<?>> {
    private final Class<T> type;

    public IsCompatibleType(Class<T> type) {
        this.type = type;
    }

    @Override // org.hamcrest.TypeSafeMatcher
    public boolean matchesSafely(Class<?> cls) {
        return this.type.isAssignableFrom(cls);
    }

    @Override // org.hamcrest.TypeSafeMatcher
    public void describeMismatchSafely(Class<?> cls, Description mismatchDescription) {
        mismatchDescription.appendValue(cls.getName());
    }

    @Override // org.hamcrest.SelfDescribing
    public void describeTo(Description description) {
        description.appendText("type < ").appendText(this.type.getName());
    }

    @Factory
    public static <T> Matcher<Class<?>> typeCompatibleWith(Class<T> baseType) {
        return new IsCompatibleType(baseType);
    }
}
