package soot.util;

import java.util.ArrayList;
/* loaded from: gencallgraphv3.jar:soot/util/StationaryArrayList.class */
public class StationaryArrayList<T> extends ArrayList<T> {
    @Override // java.util.AbstractList, java.util.Collection, java.util.List
    public int hashCode() {
        return System.identityHashCode(this);
    }

    @Override // java.util.AbstractList, java.util.Collection, java.util.List
    public boolean equals(Object other) {
        return this == other;
    }
}
