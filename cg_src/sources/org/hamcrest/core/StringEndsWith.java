package org.hamcrest.core;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/core/StringEndsWith.class
  gencallgraphv3.jar:hamcrest-core-1.3.jar:org/hamcrest/core/StringEndsWith.class
 */
/* loaded from: gencallgraphv3.jar:org.hamcrest.core_1.3.0.v20180420-1519.jar:org/hamcrest/core/StringEndsWith.class */
public class StringEndsWith extends SubstringMatcher {
    public StringEndsWith(String substring) {
        super(substring);
    }

    @Override // org.hamcrest.core.SubstringMatcher
    protected boolean evalSubstringOf(String s) {
        return s.endsWith(this.substring);
    }

    @Override // org.hamcrest.core.SubstringMatcher
    protected String relationship() {
        return "ending with";
    }

    @Factory
    public static Matcher<String> endsWith(String suffix) {
        return new StringEndsWith(suffix);
    }
}
