package org.mockito.internal.matchers.text;

import java.lang.reflect.Method;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.internal.util.StringUtil;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/matchers/text/MatcherToString.class */
class MatcherToString {
    MatcherToString() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String toString(ArgumentMatcher<?> matcher) {
        String matcherName;
        Class<?> cls = matcher.getClass();
        while (true) {
            Class<?> cls2 = cls;
            if (cls2 != Object.class) {
                Method[] methods = cls2.getDeclaredMethods();
                for (Method m : methods) {
                    if (ObjectMethodsGuru.isToStringMethod(m)) {
                        return matcher.toString();
                    }
                }
                cls = cls2.getSuperclass();
            } else {
                Class<?> matcherClass = matcher.getClass();
                if (matcherClass.isSynthetic()) {
                    matcherName = "";
                } else {
                    matcherName = matcherClass.getSimpleName();
                }
                return StringUtil.decamelizeMatcherName(matcherName);
            }
        }
    }
}
