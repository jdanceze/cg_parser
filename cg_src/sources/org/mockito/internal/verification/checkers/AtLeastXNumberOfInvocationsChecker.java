package org.mockito.internal.verification.checkers;

import java.util.List;
import org.mockito.internal.exceptions.Reporter;
import org.mockito.internal.invocation.InvocationMarker;
import org.mockito.internal.invocation.InvocationsFinder;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.invocation.MatchableInvocation;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/verification/checkers/AtLeastXNumberOfInvocationsChecker.class */
public class AtLeastXNumberOfInvocationsChecker {
    public static void checkAtLeastNumberOfInvocations(List<Invocation> invocations, MatchableInvocation wanted, int wantedCount) {
        List<Invocation> actualInvocations = InvocationsFinder.findInvocations(invocations, wanted);
        int actualCount = actualInvocations.size();
        if (wantedCount > actualCount) {
            List<Location> allLocations = InvocationsFinder.getAllLocations(actualInvocations);
            throw Reporter.tooFewActualInvocations(new AtLeastDiscrepancy(wantedCount, actualCount), wanted, allLocations);
        } else {
            InvocationMarker.markVerified(actualInvocations, wanted);
        }
    }

    public static void checkAtLeastNumberOfInvocations(List<Invocation> invocations, MatchableInvocation wanted, int wantedCount, InOrderContext orderingContext) {
        List<Invocation> chunk = InvocationsFinder.findAllMatchingUnverifiedChunks(invocations, wanted, orderingContext);
        int actualCount = chunk.size();
        if (wantedCount > actualCount) {
            List<Location> allLocations = InvocationsFinder.getAllLocations(chunk);
            throw Reporter.tooFewActualInvocationsInOrder(new AtLeastDiscrepancy(wantedCount, actualCount), wanted, allLocations);
        } else {
            InvocationMarker.markVerifiedInOrder(chunk, wanted, orderingContext);
        }
    }
}
