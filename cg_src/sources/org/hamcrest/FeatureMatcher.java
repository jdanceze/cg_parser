package org.hamcrest;

import org.hamcrest.internal.ReflectiveTypeFinder;
import soot.coffi.Instruction;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/FeatureMatcher.class
  gencallgraphv3.jar:hamcrest-core-1.3.jar:org/hamcrest/FeatureMatcher.class
 */
/* loaded from: gencallgraphv3.jar:org.hamcrest.core_1.3.0.v20180420-1519.jar:org/hamcrest/FeatureMatcher.class */
public abstract class FeatureMatcher<T, U> extends TypeSafeDiagnosingMatcher<T> {
    private static final ReflectiveTypeFinder TYPE_FINDER = new ReflectiveTypeFinder("featureValueOf", 1, 0);
    private final Matcher<? super U> subMatcher;
    private final String featureDescription;
    private final String featureName;

    protected abstract U featureValueOf(T t);

    public FeatureMatcher(Matcher<? super U> subMatcher, String featureDescription, String featureName) {
        super(TYPE_FINDER);
        this.subMatcher = subMatcher;
        this.featureDescription = featureDescription;
        this.featureName = featureName;
    }

    @Override // org.hamcrest.TypeSafeDiagnosingMatcher
    protected boolean matchesSafely(T actual, Description mismatch) {
        U featureValue = featureValueOf(actual);
        if (!this.subMatcher.matches(featureValue)) {
            mismatch.appendText(this.featureName).appendText(Instruction.argsep);
            this.subMatcher.describeMismatch(featureValue, mismatch);
            return false;
        }
        return true;
    }

    @Override // org.hamcrest.SelfDescribing
    public final void describeTo(Description description) {
        description.appendText(this.featureDescription).appendText(Instruction.argsep).appendDescriptionOf(this.subMatcher);
    }
}
