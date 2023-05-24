package org.hamcrest.core;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import soot.coffi.Instruction;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/core/ShortcutCombination.class
  gencallgraphv3.jar:hamcrest-core-1.3.jar:org/hamcrest/core/ShortcutCombination.class
 */
/* loaded from: gencallgraphv3.jar:org.hamcrest.core_1.3.0.v20180420-1519.jar:org/hamcrest/core/ShortcutCombination.class */
abstract class ShortcutCombination<T> extends BaseMatcher<T> {
    private final Iterable<Matcher<? super T>> matchers;

    @Override // org.hamcrest.Matcher
    public abstract boolean matches(Object obj);

    @Override // org.hamcrest.SelfDescribing
    public abstract void describeTo(Description description);

    public ShortcutCombination(Iterable<Matcher<? super T>> matchers) {
        this.matchers = matchers;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean matches(Object o, boolean shortcut) {
        for (Matcher<? super T> matcher : this.matchers) {
            if (matcher.matches(o) == shortcut) {
                return shortcut;
            }
        }
        return !shortcut;
    }

    public void describeTo(Description description, String operator) {
        description.appendList("(", Instruction.argsep + operator + Instruction.argsep, ")", this.matchers);
    }
}
