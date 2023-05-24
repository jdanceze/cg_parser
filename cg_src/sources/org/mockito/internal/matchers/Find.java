package org.mockito.internal.matchers;

import java.io.Serializable;
import java.util.regex.Pattern;
import org.mockito.ArgumentMatcher;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/matchers/Find.class */
public class Find implements ArgumentMatcher<String>, Serializable {
    private final String regex;

    public Find(String regex) {
        this.regex = regex;
    }

    @Override // org.mockito.ArgumentMatcher
    public boolean matches(String actual) {
        return actual != null && Pattern.compile(this.regex).matcher(actual).find();
    }

    public String toString() {
        return "find(\"" + this.regex.replaceAll("\\\\", "\\\\\\\\") + "\")";
    }
}
