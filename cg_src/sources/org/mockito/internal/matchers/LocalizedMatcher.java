package org.mockito.internal.matchers;

import org.mockito.ArgumentMatcher;
import org.mockito.internal.debugging.LocationImpl;
import org.mockito.invocation.Location;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/matchers/LocalizedMatcher.class */
public class LocalizedMatcher {
    private final ArgumentMatcher<?> matcher;
    private final Location location = new LocationImpl();

    public LocalizedMatcher(ArgumentMatcher<?> matcher) {
        this.matcher = matcher;
    }

    public Location getLocation() {
        return this.location;
    }

    public ArgumentMatcher<?> getMatcher() {
        return this.matcher;
    }
}
