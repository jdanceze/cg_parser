package org.hamcrest.core;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/core/StringContains.class
  gencallgraphv3.jar:hamcrest-core-1.3.jar:org/hamcrest/core/StringContains.class
 */
/* loaded from: gencallgraphv3.jar:org.hamcrest.core_1.3.0.v20180420-1519.jar:org/hamcrest/core/StringContains.class */
public class StringContains extends SubstringMatcher {
    public StringContains(String substring) {
        super(substring);
    }

    @Override // org.hamcrest.core.SubstringMatcher
    protected boolean evalSubstringOf(String s) {
        return s.indexOf(this.substring) >= 0;
    }

    @Override // org.hamcrest.core.SubstringMatcher
    protected String relationship() {
        return "containing";
    }

    @Factory
    public static Matcher<String> containsString(String substring) {
        return new StringContains(substring);
    }
}
