package org.mockito.internal.matchers.text;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.ContainsExtraTypeInfo;
import org.mockito.internal.reporting.PrintSettings;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/matchers/text/MatchersPrinter.class */
public class MatchersPrinter {
    public String getArgumentsLine(List<ArgumentMatcher> matchers, PrintSettings printSettings) {
        Iterator args = applyPrintSettings(matchers, printSettings);
        return ValuePrinter.printValues("(", ", ", ");", args);
    }

    public String getArgumentsBlock(List<ArgumentMatcher> matchers, PrintSettings printSettings) {
        Iterator args = applyPrintSettings(matchers, printSettings);
        return ValuePrinter.printValues("(\n    ", ",\n    ", "\n);", args);
    }

    private Iterator<FormattedText> applyPrintSettings(List<ArgumentMatcher> matchers, PrintSettings printSettings) {
        List<FormattedText> out = new LinkedList<>();
        int i = 0;
        for (ArgumentMatcher matcher : matchers) {
            if ((matcher instanceof ContainsExtraTypeInfo) && printSettings.extraTypeInfoFor(i)) {
                out.add(new FormattedText(((ContainsExtraTypeInfo) matcher).toStringWithType()));
            } else {
                out.add(new FormattedText(MatcherToString.toString(matcher)));
            }
            i++;
        }
        return out.iterator();
    }
}
