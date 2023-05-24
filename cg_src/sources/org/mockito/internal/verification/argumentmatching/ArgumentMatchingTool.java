package org.mockito.internal.verification.argumentmatching;

import java.util.LinkedList;
import java.util.List;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.ContainsExtraTypeInfo;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/verification/argumentmatching/ArgumentMatchingTool.class */
public class ArgumentMatchingTool {
    private ArgumentMatchingTool() {
    }

    public static Integer[] getSuspiciouslyNotMatchingArgsIndexes(List<ArgumentMatcher> matchers, Object[] arguments) {
        if (matchers.size() != arguments.length) {
            return new Integer[0];
        }
        List<Integer> suspicious = new LinkedList<>();
        int i = 0;
        for (ArgumentMatcher m : matchers) {
            if ((m instanceof ContainsExtraTypeInfo) && !safelyMatches(m, arguments[i]) && toStringEquals(m, arguments[i]) && !((ContainsExtraTypeInfo) m).typeMatches(arguments[i])) {
                suspicious.add(Integer.valueOf(i));
            }
            i++;
        }
        return (Integer[]) suspicious.toArray(new Integer[0]);
    }

    private static boolean safelyMatches(ArgumentMatcher m, Object arg) {
        try {
            return m.matches(arg);
        } catch (Throwable th) {
            return false;
        }
    }

    private static boolean toStringEquals(ArgumentMatcher m, Object arg) {
        return m.toString().equals(arg == null ? Jimple.NULL : arg.toString());
    }
}
