package org.hamcrest;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/JavaLangMatcherAssert.class */
public class JavaLangMatcherAssert {
    private JavaLangMatcherAssert() {
    }

    public static <T> boolean that(T argument, Matcher<? super T> matcher) {
        return matcher.matches(argument);
    }
}
