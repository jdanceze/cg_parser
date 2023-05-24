package org.jvnet.staxex.util;

import javax.xml.stream.Location;
/* loaded from: gencallgraphv3.jar:stax-ex-1.8.jar:org/jvnet/staxex/util/DummyLocation.class */
public final class DummyLocation implements Location {
    public static final Location INSTANCE = new DummyLocation();

    private DummyLocation() {
    }

    public int getCharacterOffset() {
        return -1;
    }

    public int getColumnNumber() {
        return -1;
    }

    public int getLineNumber() {
        return -1;
    }

    public String getPublicId() {
        return null;
    }

    public String getSystemId() {
        return null;
    }
}
