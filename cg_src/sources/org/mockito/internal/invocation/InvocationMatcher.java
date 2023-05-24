package org.mockito.internal.invocation;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.invocation.MatchableInvocation;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/invocation/InvocationMatcher.class */
public class InvocationMatcher implements MatchableInvocation, DescribedInvocation, Serializable {
    private final Invocation invocation;
    private final List<ArgumentMatcher<?>> matchers;

    public InvocationMatcher(Invocation invocation, List<ArgumentMatcher> matchers) {
        this.invocation = invocation;
        if (matchers.isEmpty()) {
            this.matchers = invocation.getArgumentsAsMatchers();
        } else {
            this.matchers = matchers;
        }
    }

    public InvocationMatcher(Invocation invocation) {
        this(invocation, Collections.emptyList());
    }

    public static List<InvocationMatcher> createFrom(List<Invocation> invocations) {
        LinkedList<InvocationMatcher> out = new LinkedList<>();
        for (Invocation i : invocations) {
            out.add(new InvocationMatcher(i));
        }
        return out;
    }

    public Method getMethod() {
        return this.invocation.getMethod();
    }

    @Override // org.mockito.invocation.MatchableInvocation
    public Invocation getInvocation() {
        return this.invocation;
    }

    @Override // org.mockito.invocation.MatchableInvocation
    public List<ArgumentMatcher> getMatchers() {
        return this.matchers;
    }

    @Override // org.mockito.invocation.DescribedInvocation
    public String toString() {
        return new PrintSettings().print(this.matchers, this.invocation);
    }

    @Override // org.mockito.invocation.MatchableInvocation
    public boolean matches(Invocation candidate) {
        return this.invocation.getMock().equals(candidate.getMock()) && hasSameMethod(candidate) && argumentsMatch(candidate);
    }

    @Override // org.mockito.invocation.MatchableInvocation
    public boolean hasSimilarMethod(Invocation candidate) {
        String wantedMethodName = getMethod().getName();
        String candidateMethodName = candidate.getMethod().getName();
        if (wantedMethodName.equals(candidateMethodName) && !candidate.isVerified() && getInvocation().getMock() == candidate.getMock()) {
            return hasSameMethod(candidate) || !argumentsMatch(candidate);
        }
        return false;
    }

    @Override // org.mockito.invocation.MatchableInvocation
    public boolean hasSameMethod(Invocation candidate) {
        Method m1 = this.invocation.getMethod();
        Method m2 = candidate.getMethod();
        if (m1.getName() != null && m1.getName().equals(m2.getName())) {
            Class<?>[] params1 = m1.getParameterTypes();
            Class<?>[] params2 = m2.getParameterTypes();
            return Arrays.equals(params1, params2);
        }
        return false;
    }

    @Override // org.mockito.invocation.DescribedInvocation
    public Location getLocation() {
        return this.invocation.getLocation();
    }

    @Override // org.mockito.invocation.MatchableInvocation
    public void captureArgumentsFrom(Invocation invocation) {
        MatcherApplicationStrategy strategy = MatcherApplicationStrategy.getMatcherApplicationStrategyFor(invocation, this.matchers);
        strategy.forEachMatcherAndArgument(captureArgument());
    }

    private ArgumentMatcherAction captureArgument() {
        return new ArgumentMatcherAction() { // from class: org.mockito.internal.invocation.InvocationMatcher.1
            @Override // org.mockito.internal.invocation.ArgumentMatcherAction
            public boolean apply(ArgumentMatcher<?> matcher, Object argument) {
                if (matcher instanceof CapturesArguments) {
                    ((CapturesArguments) matcher).captureFrom(argument);
                    return true;
                }
                return true;
            }
        };
    }

    private boolean argumentsMatch(Invocation actual) {
        List matchers = getMatchers();
        return MatcherApplicationStrategy.getMatcherApplicationStrategyFor(actual, matchers).forEachMatcherAndArgument(TypeSafeMatching.matchesTypeSafe());
    }
}
