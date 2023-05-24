package org.mockito.internal.progress;

import java.util.List;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.LocalizedMatcher;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/progress/ArgumentMatcherStorage.class */
public interface ArgumentMatcherStorage {
    void reportMatcher(ArgumentMatcher<?> argumentMatcher);

    List<LocalizedMatcher> pullLocalizedMatchers();

    void reportAnd();

    void reportNot();

    void reportOr();

    void validateState();

    void reset();
}
