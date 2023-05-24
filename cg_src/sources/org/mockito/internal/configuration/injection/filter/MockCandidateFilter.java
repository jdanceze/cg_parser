package org.mockito.internal.configuration.injection.filter;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/configuration/injection/filter/MockCandidateFilter.class */
public interface MockCandidateFilter {
    OngoingInjector filterCandidate(Collection<Object> collection, Field field, List<Field> list, Object obj);
}
