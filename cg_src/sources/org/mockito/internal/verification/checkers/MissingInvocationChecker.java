package org.mockito.internal.verification.checkers;

import java.util.List;
import org.mockito.internal.exceptions.Reporter;
import org.mockito.internal.invocation.InvocationsFinder;
import org.mockito.internal.reporting.SmartPrinter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.invocation.MatchableInvocation;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/verification/checkers/MissingInvocationChecker.class */
public class MissingInvocationChecker {
    private MissingInvocationChecker() {
    }

    public static void checkMissingInvocation(List<Invocation> invocations, MatchableInvocation wanted) {
        List<Invocation> actualInvocations = InvocationsFinder.findInvocations(invocations, wanted);
        if (!actualInvocations.isEmpty()) {
            return;
        }
        Invocation similar = InvocationsFinder.findSimilarInvocation(invocations, wanted);
        if (similar == null) {
            throw Reporter.wantedButNotInvoked(wanted, invocations);
        }
        Integer[] indexesOfSuspiciousArgs = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(wanted.getMatchers(), similar.getArguments());
        SmartPrinter smartPrinter = new SmartPrinter(wanted, invocations, indexesOfSuspiciousArgs);
        List<Location> actualLocations = ListUtil.convert(invocations, new ListUtil.Converter<Invocation, Location>() { // from class: org.mockito.internal.verification.checkers.MissingInvocationChecker.1
            @Override // org.mockito.internal.util.collections.ListUtil.Converter
            public Location convert(Invocation invocation) {
                return invocation.getLocation();
            }
        });
        throw Reporter.argumentsAreDifferent(smartPrinter.getWanted(), smartPrinter.getActuals(), actualLocations);
    }

    public static void checkMissingInvocation(List<Invocation> invocations, MatchableInvocation wanted, InOrderContext context) {
        List<Invocation> chunk = InvocationsFinder.findAllMatchingUnverifiedChunks(invocations, wanted, context);
        if (!chunk.isEmpty()) {
            return;
        }
        Invocation previousInOrder = InvocationsFinder.findPreviousVerifiedInOrder(invocations, context);
        if (previousInOrder != null) {
            throw Reporter.wantedButNotInvokedInOrder(wanted, previousInOrder);
        }
        checkMissingInvocation(invocations, wanted);
    }
}
