package org.mockito.internal.verification.checkers;

import java.util.Arrays;
import java.util.List;
import org.mockito.internal.exceptions.Reporter;
import org.mockito.internal.invocation.InvocationMarker;
import org.mockito.internal.invocation.InvocationsFinder;
import org.mockito.internal.reporting.Discrepancy;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.invocation.MatchableInvocation;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/verification/checkers/NumberOfInvocationsChecker.class */
public class NumberOfInvocationsChecker {
    private NumberOfInvocationsChecker() {
    }

    public static void checkNumberOfInvocations(List<Invocation> invocations, MatchableInvocation wanted, int wantedCount) {
        List<Invocation> actualInvocations = InvocationsFinder.findInvocations(invocations, wanted);
        int actualCount = actualInvocations.size();
        if (wantedCount > actualCount) {
            List<Location> allLocations = InvocationsFinder.getAllLocations(actualInvocations);
            throw Reporter.tooFewActualInvocations(new Discrepancy(wantedCount, actualCount), wanted, allLocations);
        } else if (wantedCount == 0 && actualCount > 0) {
            throw Reporter.neverWantedButInvoked(wanted, InvocationsFinder.getAllLocations(actualInvocations));
        } else {
            if (wantedCount < actualCount) {
                throw Reporter.tooManyActualInvocations(wantedCount, actualCount, wanted, InvocationsFinder.getAllLocations(actualInvocations));
            }
            InvocationMarker.markVerified(actualInvocations, wanted);
        }
    }

    public static void checkNumberOfInvocations(List<Invocation> invocations, MatchableInvocation wanted, int wantedCount, InOrderContext context) {
        List<Invocation> chunk = InvocationsFinder.findMatchingChunk(invocations, wanted, wantedCount, context);
        int actualCount = chunk.size();
        if (wantedCount > actualCount) {
            List<Location> allLocations = InvocationsFinder.getAllLocations(chunk);
            throw Reporter.tooFewActualInvocationsInOrder(new Discrepancy(wantedCount, actualCount), wanted, allLocations);
        } else if (wantedCount < actualCount) {
            throw Reporter.tooManyActualInvocationsInOrder(wantedCount, actualCount, wanted, InvocationsFinder.getAllLocations(chunk));
        } else {
            InvocationMarker.markVerifiedInOrder(chunk, wanted, context);
        }
    }

    public static void checkNumberOfInvocationsNonGreedy(List<Invocation> invocations, MatchableInvocation wanted, int wantedCount, InOrderContext context) {
        Location lastLocation = null;
        for (int actualCount = 0; actualCount < wantedCount; actualCount++) {
            Invocation next = InvocationsFinder.findFirstMatchingUnverifiedInvocation(invocations, wanted, context);
            if (next == null) {
                throw Reporter.tooFewActualInvocationsInOrder(new Discrepancy(wantedCount, actualCount), wanted, Arrays.asList(lastLocation));
            }
            InvocationMarker.markVerified(next, wanted);
            context.markVerified(next);
            lastLocation = next.getLocation();
        }
    }
}
