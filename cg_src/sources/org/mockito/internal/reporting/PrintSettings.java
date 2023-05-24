package org.mockito.internal.reporting;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.text.MatchersPrinter;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MatchableInvocation;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/reporting/PrintSettings.class */
public class PrintSettings {
    public static final int MAX_LINE_LENGTH = 45;
    private boolean multiline;
    private List<Integer> withTypeInfo = new LinkedList();

    public void setMultiline(boolean multiline) {
        this.multiline = multiline;
    }

    public boolean isMultiline() {
        return this.multiline;
    }

    public static PrintSettings verboseMatchers(Integer... indexesOfMatchers) {
        PrintSettings settings = new PrintSettings();
        settings.setMatchersToBeDescribedWithExtraTypeInfo(indexesOfMatchers);
        return settings;
    }

    public boolean extraTypeInfoFor(int argumentIndex) {
        return this.withTypeInfo.contains(Integer.valueOf(argumentIndex));
    }

    public void setMatchersToBeDescribedWithExtraTypeInfo(Integer[] indexesOfMatchers) {
        this.withTypeInfo = Arrays.asList(indexesOfMatchers);
    }

    public String print(List<ArgumentMatcher> matchers, Invocation invocation) {
        MatchersPrinter matchersPrinter = new MatchersPrinter();
        String qualifiedName = MockUtil.getMockName(invocation.getMock()) + "." + invocation.getMethod().getName();
        String invocationString = qualifiedName + matchersPrinter.getArgumentsLine(matchers, this);
        if (isMultiline() || (!matchers.isEmpty() && invocationString.length() > 45)) {
            return qualifiedName + matchersPrinter.getArgumentsBlock(matchers, this);
        }
        return invocationString;
    }

    public String print(Invocation invocation) {
        return print(invocation.getArgumentsAsMatchers(), invocation);
    }

    public String print(MatchableInvocation invocation) {
        return print(invocation.getMatchers(), invocation.getInvocation());
    }
}
