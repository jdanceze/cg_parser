package org.mockito.internal.invocation;

import java.util.LinkedList;
import java.util.List;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.invocation.MatchableInvocation;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/invocation/InvocationsFinder.class */
public class InvocationsFinder {
    private InvocationsFinder() {
    }

    public static List<Invocation> findInvocations(List<Invocation> invocations, MatchableInvocation wanted) {
        return ListUtil.filter(invocations, new RemoveNotMatching(wanted));
    }

    public static List<Invocation> findAllMatchingUnverifiedChunks(List<Invocation> invocations, MatchableInvocation wanted, InOrderContext orderingContext) {
        List<Invocation> unverified = removeVerifiedInOrder(invocations, orderingContext);
        return ListUtil.filter(unverified, new RemoveNotMatching(wanted));
    }

    public static List<Invocation> findMatchingChunk(List<Invocation> invocations, MatchableInvocation wanted, int wantedCount, InOrderContext context) {
        List<Invocation> unverified = removeVerifiedInOrder(invocations, context);
        List<Invocation> firstChunk = getFirstMatchingChunk(wanted, unverified);
        if (wantedCount != firstChunk.size()) {
            return findAllMatchingUnverifiedChunks(invocations, wanted, context);
        }
        return firstChunk;
    }

    private static List<Invocation> getFirstMatchingChunk(MatchableInvocation wanted, List<Invocation> unverified) {
        List<Invocation> firstChunk = new LinkedList<>();
        for (Invocation invocation : unverified) {
            if (wanted.matches(invocation)) {
                firstChunk.add(invocation);
            } else if (!firstChunk.isEmpty()) {
                break;
            }
        }
        return firstChunk;
    }

    public static Invocation findFirstMatchingUnverifiedInvocation(List<Invocation> invocations, MatchableInvocation wanted, InOrderContext context) {
        for (Invocation invocation : removeVerifiedInOrder(invocations, context)) {
            if (wanted.matches(invocation)) {
                return invocation;
            }
        }
        return null;
    }

    public static Invocation findSimilarInvocation(List<Invocation> invocations, MatchableInvocation wanted) {
        Invocation firstSimilar = null;
        for (Invocation invocation : invocations) {
            if (wanted.hasSimilarMethod(invocation)) {
                if (firstSimilar == null) {
                    firstSimilar = invocation;
                }
                if (wanted.hasSameMethod(invocation)) {
                    return invocation;
                }
            }
        }
        return firstSimilar;
    }

    public static Invocation findFirstUnverified(List<Invocation> invocations) {
        return findFirstUnverified(invocations, null);
    }

    static Invocation findFirstUnverified(List<Invocation> invocations, Object mock) {
        for (Invocation i : invocations) {
            boolean mockIsValid = mock == null || mock == i.getMock();
            if (!i.isVerified() && mockIsValid) {
                return i;
            }
        }
        return null;
    }

    public static Location getLastLocation(List<Invocation> invocations) {
        if (invocations.isEmpty()) {
            return null;
        }
        Invocation last = invocations.get(invocations.size() - 1);
        return last.getLocation();
    }

    public static Invocation findPreviousVerifiedInOrder(List<Invocation> invocations, InOrderContext context) {
        LinkedList<Invocation> verifiedOnly = ListUtil.filter(invocations, new RemoveUnverifiedInOrder(context));
        if (verifiedOnly.isEmpty()) {
            return null;
        }
        return verifiedOnly.getLast();
    }

    private static List<Invocation> removeVerifiedInOrder(List<Invocation> invocations, InOrderContext orderingContext) {
        List<Invocation> unverified = new LinkedList<>();
        for (Invocation i : invocations) {
            if (orderingContext.isVerified(i)) {
                unverified.clear();
            } else {
                unverified.add(i);
            }
        }
        return unverified;
    }

    public static List<Location> getAllLocations(List<Invocation> invocations) {
        List<Location> locations = new LinkedList<>();
        for (Invocation invocation : invocations) {
            locations.add(invocation.getLocation());
        }
        return locations;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/invocation/InvocationsFinder$RemoveNotMatching.class */
    public static class RemoveNotMatching implements ListUtil.Filter<Invocation> {
        private final MatchableInvocation wanted;

        private RemoveNotMatching(MatchableInvocation wanted) {
            this.wanted = wanted;
        }

        @Override // org.mockito.internal.util.collections.ListUtil.Filter
        public boolean isOut(Invocation invocation) {
            return !this.wanted.matches(invocation);
        }
    }

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/invocation/InvocationsFinder$RemoveUnverifiedInOrder.class */
    private static class RemoveUnverifiedInOrder implements ListUtil.Filter<Invocation> {
        private final InOrderContext orderingContext;

        public RemoveUnverifiedInOrder(InOrderContext orderingContext) {
            this.orderingContext = orderingContext;
        }

        @Override // org.mockito.internal.util.collections.ListUtil.Filter
        public boolean isOut(Invocation invocation) {
            return !this.orderingContext.isVerified(invocation);
        }
    }

    public static Invocation findFirstUnverifiedInOrder(InOrderContext context, List<Invocation> orderedInvocations) {
        Invocation candidate = null;
        for (Invocation i : orderedInvocations) {
            if (!context.isVerified(i)) {
                candidate = candidate != null ? candidate : i;
            } else {
                candidate = null;
            }
        }
        return candidate;
    }
}
