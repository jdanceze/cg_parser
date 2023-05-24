package org.hamcrest;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/Matcher.class
  gencallgraphv3.jar:hamcrest-core-1.3.jar:org/hamcrest/Matcher.class
 */
/* loaded from: gencallgraphv3.jar:org.hamcrest.core_1.3.0.v20180420-1519.jar:org/hamcrest/Matcher.class */
public interface Matcher<T> extends SelfDescribing {
    boolean matches(Object obj);

    void describeMismatch(Object obj, Description description);

    @Deprecated
    void _dont_implement_Matcher___instead_extend_BaseMatcher_();
}
