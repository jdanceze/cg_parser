package org.junit.internal;

import java.io.Serializable;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/internal/SerializableMatcherDescription.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/internal/SerializableMatcherDescription.class */
class SerializableMatcherDescription<T> extends BaseMatcher<T> implements Serializable {
    private final String matcherDescription;

    private SerializableMatcherDescription(Matcher<T> matcher) {
        this.matcherDescription = StringDescription.asString(matcher);
    }

    @Override // org.hamcrest.Matcher
    public boolean matches(Object o) {
        throw new UnsupportedOperationException("This Matcher implementation only captures the description");
    }

    @Override // org.hamcrest.SelfDescribing
    public void describeTo(Description description) {
        description.appendText(this.matcherDescription);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> Matcher<T> asSerializableMatcher(Matcher<T> matcher) {
        if (matcher == null || (matcher instanceof Serializable)) {
            return matcher;
        }
        return new SerializableMatcherDescription(matcher);
    }
}
