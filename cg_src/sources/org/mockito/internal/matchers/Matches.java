package org.mockito.internal.matchers;

import java.io.Serializable;
import java.util.regex.Pattern;
import org.mockito.ArgumentMatcher;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/matchers/Matches.class */
public class Matches implements ArgumentMatcher<Object>, Serializable {
    private final Pattern pattern;

    public Matches(String regex) {
        this(Pattern.compile(regex));
    }

    public Matches(Pattern pattern) {
        this.pattern = pattern;
    }

    @Override // org.mockito.ArgumentMatcher
    public boolean matches(Object actual) {
        return (actual instanceof String) && this.pattern.matcher((String) actual).find();
    }

    public String toString() {
        return "matches(\"" + this.pattern.pattern().replaceAll("\\\\", "\\\\\\\\") + "\")";
    }
}
