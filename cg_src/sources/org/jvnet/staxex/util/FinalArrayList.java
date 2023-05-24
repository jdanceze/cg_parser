package org.jvnet.staxex.util;

import java.util.ArrayList;
import java.util.Collection;
/* loaded from: gencallgraphv3.jar:stax-ex-1.8.jar:org/jvnet/staxex/util/FinalArrayList.class */
public final class FinalArrayList<T> extends ArrayList<T> {
    public FinalArrayList(int initialCapacity) {
        super(initialCapacity);
    }

    public FinalArrayList() {
    }

    public FinalArrayList(Collection collection) {
        super(collection);
    }
}
