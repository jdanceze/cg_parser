package org.hamcrest;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/Condition.class
  gencallgraphv3.jar:hamcrest-core-1.3.jar:org/hamcrest/Condition.class
 */
/* loaded from: gencallgraphv3.jar:org.hamcrest.core_1.3.0.v20180420-1519.jar:org/hamcrest/Condition.class */
public abstract class Condition<T> {
    public static final NotMatched<Object> NOT_MATCHED = new NotMatched<>();

    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/Condition$Step.class
      gencallgraphv3.jar:hamcrest-core-1.3.jar:org/hamcrest/Condition$Step.class
     */
    /* loaded from: gencallgraphv3.jar:org.hamcrest.core_1.3.0.v20180420-1519.jar:org/hamcrest/Condition$Step.class */
    public interface Step<I, O> {
        Condition<O> apply(I i, Description description);
    }

    public abstract boolean matching(Matcher<T> matcher, String str);

    public abstract <U> Condition<U> and(Step<? super T, U> step);

    private Condition() {
    }

    public final boolean matching(Matcher<T> match) {
        return matching(match, "");
    }

    public final <U> Condition<U> then(Step<? super T, U> mapping) {
        return and(mapping);
    }

    public static <T> Condition<T> notMatched() {
        return NOT_MATCHED;
    }

    public static <T> Condition<T> matched(T theValue, Description mismatch) {
        return new Matched(theValue, mismatch);
    }

    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/Condition$Matched.class
      gencallgraphv3.jar:hamcrest-core-1.3.jar:org/hamcrest/Condition$Matched.class
     */
    /* loaded from: gencallgraphv3.jar:org.hamcrest.core_1.3.0.v20180420-1519.jar:org/hamcrest/Condition$Matched.class */
    private static final class Matched<T> extends Condition<T> {
        private final T theValue;
        private final Description mismatch;

        private Matched(T theValue, Description mismatch) {
            super();
            this.theValue = theValue;
            this.mismatch = mismatch;
        }

        @Override // org.hamcrest.Condition
        public boolean matching(Matcher<T> matcher, String message) {
            if (matcher.matches(this.theValue)) {
                return true;
            }
            this.mismatch.appendText(message);
            matcher.describeMismatch(this.theValue, this.mismatch);
            return false;
        }

        @Override // org.hamcrest.Condition
        public <U> Condition<U> and(Step<? super T, U> next) {
            return next.apply((T) this.theValue, this.mismatch);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/Condition$NotMatched.class
      gencallgraphv3.jar:hamcrest-core-1.3.jar:org/hamcrest/Condition$NotMatched.class
     */
    /* loaded from: gencallgraphv3.jar:org.hamcrest.core_1.3.0.v20180420-1519.jar:org/hamcrest/Condition$NotMatched.class */
    public static final class NotMatched<T> extends Condition<T> {
        private NotMatched() {
            super();
        }

        @Override // org.hamcrest.Condition
        public boolean matching(Matcher<T> match, String message) {
            return false;
        }

        @Override // org.hamcrest.Condition
        public <U> Condition<U> and(Step<? super T, U> mapping) {
            return notMatched();
        }
    }
}
