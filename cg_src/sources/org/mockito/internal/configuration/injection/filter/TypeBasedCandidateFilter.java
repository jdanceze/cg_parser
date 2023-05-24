package org.mockito.internal.configuration.injection.filter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/configuration/injection/filter/TypeBasedCandidateFilter.class */
public class TypeBasedCandidateFilter implements MockCandidateFilter {
    private final MockCandidateFilter next;

    public TypeBasedCandidateFilter(MockCandidateFilter next) {
        this.next = next;
    }

    @Override // org.mockito.internal.configuration.injection.filter.MockCandidateFilter
    public OngoingInjector filterCandidate(Collection<Object> mocks, Field candidateFieldToBeInjected, List<Field> allRemainingCandidateFields, Object injectee) {
        List<Object> mockTypeMatches = new ArrayList<>();
        for (Object mock : mocks) {
            if (candidateFieldToBeInjected.getType().isAssignableFrom(mock.getClass())) {
                mockTypeMatches.add(mock);
            }
        }
        return this.next.filterCandidate(mockTypeMatches, candidateFieldToBeInjected, allRemainingCandidateFields, injectee);
    }
}
