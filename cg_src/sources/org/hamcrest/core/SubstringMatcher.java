package org.hamcrest.core;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import soot.coffi.Instruction;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/core/SubstringMatcher.class
  gencallgraphv3.jar:hamcrest-core-1.3.jar:org/hamcrest/core/SubstringMatcher.class
 */
/* loaded from: gencallgraphv3.jar:org.hamcrest.core_1.3.0.v20180420-1519.jar:org/hamcrest/core/SubstringMatcher.class */
public abstract class SubstringMatcher extends TypeSafeMatcher<String> {
    protected final String substring;

    protected abstract boolean evalSubstringOf(String str);

    protected abstract String relationship();

    /* JADX INFO: Access modifiers changed from: protected */
    public SubstringMatcher(String substring) {
        this.substring = substring;
    }

    @Override // org.hamcrest.TypeSafeMatcher
    public boolean matchesSafely(String item) {
        return evalSubstringOf(item);
    }

    @Override // org.hamcrest.TypeSafeMatcher
    public void describeMismatchSafely(String item, Description mismatchDescription) {
        mismatchDescription.appendText("was \"").appendText(item).appendText("\"");
    }

    @Override // org.hamcrest.SelfDescribing
    public void describeTo(Description description) {
        description.appendText("a string ").appendText(relationship()).appendText(Instruction.argsep).appendValue(this.substring);
    }
}
