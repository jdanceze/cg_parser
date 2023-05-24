package org.mockito.internal.configuration.injection.filter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.mockito.internal.util.MockUtil;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/configuration/injection/filter/NameBasedCandidateFilter.class */
public class NameBasedCandidateFilter implements MockCandidateFilter {
    private final MockCandidateFilter next;

    public NameBasedCandidateFilter(MockCandidateFilter next) {
        this.next = next;
    }

    @Override // org.mockito.internal.configuration.injection.filter.MockCandidateFilter
    public OngoingInjector filterCandidate(Collection<Object> mocks, Field candidateFieldToBeInjected, List<Field> allRemainingCandidateFields, Object injectee) {
        if (mocks.size() == 1 && anotherCandidateMatchesMockName(mocks, candidateFieldToBeInjected, allRemainingCandidateFields)) {
            return OngoingInjector.nop;
        }
        return this.next.filterCandidate(tooMany(mocks) ? selectMatchingName(mocks, candidateFieldToBeInjected) : mocks, candidateFieldToBeInjected, allRemainingCandidateFields, injectee);
    }

    private boolean tooMany(Collection<Object> mocks) {
        return mocks.size() > 1;
    }

    private List<Object> selectMatchingName(Collection<Object> mocks, Field candidateFieldToBeInjected) {
        List<Object> mockNameMatches = new ArrayList<>();
        for (Object mock : mocks) {
            if (candidateFieldToBeInjected.getName().equals(MockUtil.getMockName(mock).toString())) {
                mockNameMatches.add(mock);
            }
        }
        return mockNameMatches;
    }

    private boolean anotherCandidateMatchesMockName(Collection<Object> mocks, Field candidateFieldToBeInjected, List<Field> allRemainingCandidateFields) {
        String mockName = MockUtil.getMockName(mocks.iterator().next()).toString();
        for (Field otherCandidateField : allRemainingCandidateFields) {
            if (!otherCandidateField.equals(candidateFieldToBeInjected) && otherCandidateField.getType().equals(candidateFieldToBeInjected.getType()) && otherCandidateField.getName().equals(mockName)) {
                return true;
            }
        }
        return false;
    }
}
