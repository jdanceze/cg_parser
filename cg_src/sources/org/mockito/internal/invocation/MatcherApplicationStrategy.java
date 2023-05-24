package org.mockito.internal.invocation;

import java.util.ArrayList;
import java.util.List;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.hamcrest.HamcrestArgumentMatcher;
import org.mockito.internal.matchers.VarargMatcher;
import org.mockito.invocation.Invocation;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/invocation/MatcherApplicationStrategy.class */
public class MatcherApplicationStrategy {
    private final Invocation invocation;
    private final List<ArgumentMatcher<?>> matchers;
    private final MatcherApplicationType matchingType;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/invocation/MatcherApplicationStrategy$MatcherApplicationType.class */
    public enum MatcherApplicationType {
        ONE_MATCHER_PER_ARGUMENT,
        MATCH_EACH_VARARGS_WITH_LAST_MATCHER,
        ERROR_UNSUPPORTED_NUMBER_OF_MATCHERS
    }

    private MatcherApplicationStrategy(Invocation invocation, List<ArgumentMatcher<?>> matchers, MatcherApplicationType matchingType) {
        this.invocation = invocation;
        if (matchingType == MatcherApplicationType.MATCH_EACH_VARARGS_WITH_LAST_MATCHER) {
            int times = varargLength(invocation);
            this.matchers = appendLastMatcherNTimes(matchers, times);
        } else {
            this.matchers = matchers;
        }
        this.matchingType = matchingType;
    }

    public static MatcherApplicationStrategy getMatcherApplicationStrategyFor(Invocation invocation, List<ArgumentMatcher<?>> matchers) {
        MatcherApplicationType type = getMatcherApplicationType(invocation, matchers);
        return new MatcherApplicationStrategy(invocation, matchers, type);
    }

    public boolean forEachMatcherAndArgument(ArgumentMatcherAction action) {
        if (this.matchingType == MatcherApplicationType.ERROR_UNSUPPORTED_NUMBER_OF_MATCHERS) {
            return false;
        }
        Object[] arguments = this.invocation.getArguments();
        for (int i = 0; i < arguments.length; i++) {
            ArgumentMatcher<?> matcher = this.matchers.get(i);
            Object argument = arguments[i];
            if (!action.apply(matcher, argument)) {
                return false;
            }
        }
        return true;
    }

    private static MatcherApplicationType getMatcherApplicationType(Invocation invocation, List<ArgumentMatcher<?>> matchers) {
        int rawArguments = invocation.getRawArguments().length;
        int expandedArguments = invocation.getArguments().length;
        int matcherCount = matchers.size();
        if (expandedArguments == matcherCount) {
            return MatcherApplicationType.ONE_MATCHER_PER_ARGUMENT;
        }
        if (rawArguments == matcherCount && isLastMatcherVarargMatcher(matchers)) {
            return MatcherApplicationType.MATCH_EACH_VARARGS_WITH_LAST_MATCHER;
        }
        return MatcherApplicationType.ERROR_UNSUPPORTED_NUMBER_OF_MATCHERS;
    }

    private static boolean isLastMatcherVarargMatcher(List<ArgumentMatcher<?>> matchers) {
        ArgumentMatcher<?> argumentMatcher = lastMatcher(matchers);
        if (argumentMatcher instanceof HamcrestArgumentMatcher) {
            return ((HamcrestArgumentMatcher) argumentMatcher).isVarargMatcher();
        }
        return argumentMatcher instanceof VarargMatcher;
    }

    private static List<ArgumentMatcher<?>> appendLastMatcherNTimes(List<ArgumentMatcher<?>> matchers, int timesToAppendLastMatcher) {
        ArgumentMatcher<?> lastMatcher = lastMatcher(matchers);
        List<ArgumentMatcher<?>> expandedMatchers = new ArrayList<>(matchers);
        for (int i = 0; i < timesToAppendLastMatcher; i++) {
            expandedMatchers.add(lastMatcher);
        }
        return expandedMatchers;
    }

    private static int varargLength(Invocation invocation) {
        int rawArgumentCount = invocation.getRawArguments().length;
        int expandedArgumentCount = invocation.getArguments().length;
        return expandedArgumentCount - rawArgumentCount;
    }

    private static ArgumentMatcher<?> lastMatcher(List<ArgumentMatcher<?>> matchers) {
        return matchers.get(matchers.size() - 1);
    }
}
