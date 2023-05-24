package org.hamcrest.text;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/text/IsEqualIgnoringWhiteSpace.class */
public class IsEqualIgnoringWhiteSpace extends TypeSafeMatcher<String> {
    private final String string;

    public IsEqualIgnoringWhiteSpace(String string) {
        if (string == null) {
            throw new IllegalArgumentException("Non-null value required by IsEqualIgnoringCase()");
        }
        this.string = string;
    }

    @Override // org.hamcrest.TypeSafeMatcher
    public boolean matchesSafely(String item) {
        return stripSpace(this.string).equalsIgnoreCase(stripSpace(item));
    }

    @Override // org.hamcrest.TypeSafeMatcher
    public void describeMismatchSafely(String item, Description mismatchDescription) {
        mismatchDescription.appendText("was  ").appendText(stripSpace(item));
    }

    @Override // org.hamcrest.SelfDescribing
    public void describeTo(Description description) {
        description.appendText("equalToIgnoringWhiteSpace(").appendValue(this.string).appendText(")");
    }

    public String stripSpace(String toBeStripped) {
        boolean z;
        StringBuilder result = new StringBuilder();
        boolean lastWasSpace = true;
        for (int i = 0; i < toBeStripped.length(); i++) {
            char c = toBeStripped.charAt(i);
            if (Character.isWhitespace(c)) {
                if (!lastWasSpace) {
                    result.append(' ');
                }
                z = true;
            } else {
                result.append(c);
                z = false;
            }
            lastWasSpace = z;
        }
        return result.toString().trim();
    }

    @Factory
    public static Matcher<String> equalToIgnoringWhiteSpace(String expectedString) {
        return new IsEqualIgnoringWhiteSpace(expectedString);
    }
}
