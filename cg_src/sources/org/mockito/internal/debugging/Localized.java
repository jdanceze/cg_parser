package org.mockito.internal.debugging;

import org.mockito.invocation.Location;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/debugging/Localized.class */
public class Localized<T> {
    private final T object;
    private final Location location = new LocationImpl();

    public Localized(T object) {
        this.object = object;
    }

    public T getObject() {
        return this.object;
    }

    public Location getLocation() {
        return this.location;
    }
}
