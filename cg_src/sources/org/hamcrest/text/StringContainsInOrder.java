package org.hamcrest.text;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/text/StringContainsInOrder.class */
public class StringContainsInOrder extends TypeSafeMatcher<String> {
    private final Iterable<String> substrings;

    public StringContainsInOrder(Iterable<String> substrings) {
        this.substrings = substrings;
    }

    @Override // org.hamcrest.TypeSafeMatcher
    public boolean matchesSafely(String s) {
        int fromIndex = 0;
        for (String substring : this.substrings) {
            fromIndex = s.indexOf(substring, fromIndex);
            if (fromIndex == -1) {
                return false;
            }
        }
        return true;
    }

    @Override // org.hamcrest.TypeSafeMatcher
    public void describeMismatchSafely(String item, Description mismatchDescription) {
        mismatchDescription.appendText("was \"").appendText(item).appendText("\"");
    }

    @Override // org.hamcrest.SelfDescribing
    public void describeTo(Description description) {
        description.appendText("a string containing ").appendValueList("", ", ", "", this.substrings).appendText(" in order");
    }

    @Factory
    public static Matcher<String> stringContainsInOrder(Iterable<String> substrings) {
        return new StringContainsInOrder(substrings);
    }
}
