package org.hamcrest.text;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/text/IsEqualIgnoringCase.class */
public class IsEqualIgnoringCase extends TypeSafeMatcher<String> {
    private final String string;

    public IsEqualIgnoringCase(String string) {
        if (string == null) {
            throw new IllegalArgumentException("Non-null value required by IsEqualIgnoringCase()");
        }
        this.string = string;
    }

    @Override // org.hamcrest.TypeSafeMatcher
    public boolean matchesSafely(String item) {
        return this.string.equalsIgnoreCase(item);
    }

    @Override // org.hamcrest.TypeSafeMatcher
    public void describeMismatchSafely(String item, Description mismatchDescription) {
        mismatchDescription.appendText("was ").appendText(item);
    }

    @Override // org.hamcrest.SelfDescribing
    public void describeTo(Description description) {
        description.appendText("equalToIgnoringCase(").appendValue(this.string).appendText(")");
    }

    @Factory
    public static Matcher<String> equalToIgnoringCase(String expectedString) {
        return new IsEqualIgnoringCase(expectedString);
    }
}
