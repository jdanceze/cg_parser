package org.mockito.internal.configuration.injection.filter;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.plugins.MemberAccessor;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/configuration/injection/filter/TerminalMockCandidateFilter.class */
public class TerminalMockCandidateFilter implements MockCandidateFilter {
    @Override // org.mockito.internal.configuration.injection.filter.MockCandidateFilter
    public OngoingInjector filterCandidate(Collection<Object> mocks, Field candidateFieldToBeInjected, List<Field> allRemainingCandidateFields, Object injectee) {
        if (mocks.size() == 1) {
            Object matchingMock = mocks.iterator().next();
            MemberAccessor accessor = Plugins.getMemberAccessor();
            return () -> {
                try {
                    if (!new BeanPropertySetter(injectee, candidateFieldToBeInjected).set(matchingMock)) {
                        accessor.set(candidateFieldToBeInjected, injectee, matchingMock);
                    }
                    return matchingMock;
                } catch (IllegalAccessException | RuntimeException e) {
                    throw Reporter.cannotInjectDependency(candidateFieldToBeInjected, matchingMock, e);
                }
            };
        }
        return OngoingInjector.nop;
    }
}
