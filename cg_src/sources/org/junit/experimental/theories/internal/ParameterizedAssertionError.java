package org.junit.experimental.theories.internal;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/experimental/theories/internal/ParameterizedAssertionError.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/experimental/theories/internal/ParameterizedAssertionError.class */
public class ParameterizedAssertionError extends AssertionError {
    private static final long serialVersionUID = 1;

    public ParameterizedAssertionError(Throwable targetException, String methodName, Object... params) {
        super(String.format("%s(%s)", methodName, join(", ", params)));
        initCause(targetException);
    }

    public boolean equals(Object obj) {
        return (obj instanceof ParameterizedAssertionError) && toString().equals(obj.toString());
    }

    public int hashCode() {
        return toString().hashCode();
    }

    public static String join(String delimiter, Object... params) {
        return join(delimiter, Arrays.asList(params));
    }

    public static String join(String delimiter, Collection<Object> values) {
        StringBuilder sb = new StringBuilder();
        Iterator<Object> iter = values.iterator();
        while (iter.hasNext()) {
            Object next = iter.next();
            sb.append(stringValueOf(next));
            if (iter.hasNext()) {
                sb.append(delimiter);
            }
        }
        return sb.toString();
    }

    private static String stringValueOf(Object next) {
        try {
            return String.valueOf(next);
        } catch (Throwable th) {
            return "[toString failed]";
        }
    }
}
