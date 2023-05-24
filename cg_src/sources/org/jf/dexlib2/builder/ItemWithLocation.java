package org.jf.dexlib2.builder;

import javax.annotation.Nullable;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/builder/ItemWithLocation.class */
public abstract class ItemWithLocation {
    @Nullable
    MethodLocation location;

    public boolean isPlaced() {
        return this.location != null;
    }

    public void setLocation(MethodLocation methodLocation) {
        this.location = methodLocation;
    }
}
