package net.bytebuddy.matcher;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.matcher.ElementMatcher;
@HashCodeAndEqualsPlugin.Enhance
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/matcher/StringMatcher.class */
public class StringMatcher extends ElementMatcher.Junction.AbstractBase<String> {
    private final String value;
    private final Mode mode;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj != null && getClass() == obj.getClass() && this.mode.equals(((StringMatcher) obj).mode) && this.value.equals(((StringMatcher) obj).value);
    }

    public int hashCode() {
        return (((17 * 31) + this.value.hashCode()) * 31) + this.mode.hashCode();
    }

    public StringMatcher(String value, Mode mode) {
        this.value = value;
        this.mode = mode;
    }

    @Override // net.bytebuddy.matcher.ElementMatcher
    public boolean matches(String target) {
        return this.mode.matches(this.value, target);
    }

    public String toString() {
        return this.mode.getDescription() + '(' + this.value + ')';
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/matcher/StringMatcher$Mode.class */
    public enum Mode {
        EQUALS_FULLY("equals") { // from class: net.bytebuddy.matcher.StringMatcher.Mode.1
            @Override // net.bytebuddy.matcher.StringMatcher.Mode
            protected boolean matches(String expected, String actual) {
                return actual.equals(expected);
            }
        },
        EQUALS_FULLY_IGNORE_CASE("equalsIgnoreCase") { // from class: net.bytebuddy.matcher.StringMatcher.Mode.2
            @Override // net.bytebuddy.matcher.StringMatcher.Mode
            protected boolean matches(String expected, String actual) {
                return actual.equalsIgnoreCase(expected);
            }
        },
        STARTS_WITH("startsWith") { // from class: net.bytebuddy.matcher.StringMatcher.Mode.3
            @Override // net.bytebuddy.matcher.StringMatcher.Mode
            protected boolean matches(String expected, String actual) {
                return actual.startsWith(expected);
            }
        },
        STARTS_WITH_IGNORE_CASE("startsWithIgnoreCase") { // from class: net.bytebuddy.matcher.StringMatcher.Mode.4
            @Override // net.bytebuddy.matcher.StringMatcher.Mode
            @SuppressFBWarnings(value = {"DM_CONVERT_CASE"}, justification = "Both strings are transformed by the default locale")
            protected boolean matches(String expected, String actual) {
                return actual.toLowerCase().startsWith(expected.toLowerCase());
            }
        },
        ENDS_WITH("endsWith") { // from class: net.bytebuddy.matcher.StringMatcher.Mode.5
            @Override // net.bytebuddy.matcher.StringMatcher.Mode
            protected boolean matches(String expected, String actual) {
                return actual.endsWith(expected);
            }
        },
        ENDS_WITH_IGNORE_CASE("endsWithIgnoreCase") { // from class: net.bytebuddy.matcher.StringMatcher.Mode.6
            @Override // net.bytebuddy.matcher.StringMatcher.Mode
            @SuppressFBWarnings(value = {"DM_CONVERT_CASE"}, justification = "Both strings are transformed by the default locale")
            protected boolean matches(String expected, String actual) {
                return actual.toLowerCase().endsWith(expected.toLowerCase());
            }
        },
        CONTAINS("contains") { // from class: net.bytebuddy.matcher.StringMatcher.Mode.7
            @Override // net.bytebuddy.matcher.StringMatcher.Mode
            protected boolean matches(String expected, String actual) {
                return actual.contains(expected);
            }
        },
        CONTAINS_IGNORE_CASE("containsIgnoreCase") { // from class: net.bytebuddy.matcher.StringMatcher.Mode.8
            @Override // net.bytebuddy.matcher.StringMatcher.Mode
            @SuppressFBWarnings(value = {"DM_CONVERT_CASE"}, justification = "Both strings are transformed by the default locale")
            protected boolean matches(String expected, String actual) {
                return actual.toLowerCase().contains(expected.toLowerCase());
            }
        },
        MATCHES("matches") { // from class: net.bytebuddy.matcher.StringMatcher.Mode.9
            @Override // net.bytebuddy.matcher.StringMatcher.Mode
            protected boolean matches(String expected, String actual) {
                return actual.matches(expected);
            }
        };
        
        private final String description;

        protected abstract boolean matches(String str, String str2);

        Mode(String description) {
            this.description = description;
        }

        protected String getDescription() {
            return this.description;
        }
    }
}
