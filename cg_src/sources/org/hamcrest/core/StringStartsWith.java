package org.hamcrest.core;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/core/StringStartsWith.class
  gencallgraphv3.jar:hamcrest-core-1.3.jar:org/hamcrest/core/StringStartsWith.class
 */
/* loaded from: gencallgraphv3.jar:org.hamcrest.core_1.3.0.v20180420-1519.jar:org/hamcrest/core/StringStartsWith.class */
public class StringStartsWith extends SubstringMatcher {
    public StringStartsWith(String substring) {
        super(substring);
    }

    @Override // org.hamcrest.core.SubstringMatcher
    protected boolean evalSubstringOf(String s) {
        return s.startsWith(this.substring);
    }

    @Override // org.hamcrest.core.SubstringMatcher
    protected String relationship() {
        return "starting with";
    }

    @Factory
    public static Matcher<String> startsWith(String prefix) {
        return new StringStartsWith(prefix);
    }
}
