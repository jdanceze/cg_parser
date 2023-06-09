package org.hamcrest.text;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.core.AnyOf;
import org.hamcrest.core.IsNull;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/text/IsEmptyString.class */
public final class IsEmptyString extends BaseMatcher<String> {
    private static final IsEmptyString INSTANCE = new IsEmptyString();
    private static final Matcher<String> NULL_OR_EMPTY_INSTANCE = AnyOf.anyOf(IsNull.nullValue(), INSTANCE);

    @Override // org.hamcrest.Matcher
    public boolean matches(Object item) {
        return item != null && (item instanceof String) && ((String) item).equals("");
    }

    @Override // org.hamcrest.SelfDescribing
    public void describeTo(Description description) {
        description.appendText("an empty string");
    }

    @Factory
    public static Matcher<String> isEmptyString() {
        return INSTANCE;
    }

    @Factory
    public static Matcher<String> isEmptyOrNullString() {
        return NULL_OR_EMPTY_INSTANCE;
    }
}
