package org.hamcrest.core;

import java.util.regex.Pattern;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/core/DescribedAs.class
  gencallgraphv3.jar:hamcrest-core-1.3.jar:org/hamcrest/core/DescribedAs.class
 */
/* loaded from: gencallgraphv3.jar:org.hamcrest.core_1.3.0.v20180420-1519.jar:org/hamcrest/core/DescribedAs.class */
public class DescribedAs<T> extends BaseMatcher<T> {
    private final String descriptionTemplate;
    private final Matcher<T> matcher;
    private final Object[] values;
    private static final Pattern ARG_PATTERN = Pattern.compile("%([0-9]+)");

    public DescribedAs(String descriptionTemplate, Matcher<T> matcher, Object[] values) {
        this.descriptionTemplate = descriptionTemplate;
        this.matcher = matcher;
        this.values = (Object[]) values.clone();
    }

    @Override // org.hamcrest.Matcher
    public boolean matches(Object o) {
        return this.matcher.matches(o);
    }

    @Override // org.hamcrest.SelfDescribing
    public void describeTo(Description description) {
        int textStart;
        java.util.regex.Matcher arg = ARG_PATTERN.matcher(this.descriptionTemplate);
        int i = 0;
        while (true) {
            textStart = i;
            if (!arg.find()) {
                break;
            }
            description.appendText(this.descriptionTemplate.substring(textStart, arg.start()));
            description.appendValue(this.values[Integer.parseInt(arg.group(1))]);
            i = arg.end();
        }
        if (textStart < this.descriptionTemplate.length()) {
            description.appendText(this.descriptionTemplate.substring(textStart));
        }
    }

    @Override // org.hamcrest.BaseMatcher, org.hamcrest.Matcher
    public void describeMismatch(Object item, Description description) {
        this.matcher.describeMismatch(item, description);
    }

    @Factory
    public static <T> Matcher<T> describedAs(String description, Matcher<T> matcher, Object... values) {
        return new DescribedAs(description, matcher, values);
    }
}
