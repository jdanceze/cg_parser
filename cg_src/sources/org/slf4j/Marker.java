package org.slf4j;

import java.io.Serializable;
import java.util.Iterator;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:slf4j-api-1.7.5.jar:org/slf4j/Marker.class
 */
/* loaded from: gencallgraphv3.jar:slf4j-api-2.0.3.jar:org/slf4j/Marker.class */
public interface Marker extends Serializable {
    public static final String ANY_MARKER = "*";
    public static final String ANY_NON_NULL_MARKER = "+";

    String getName();

    void add(Marker marker);

    boolean remove(Marker marker);

    @Deprecated
    boolean hasChildren();

    boolean hasReferences();

    Iterator<Marker> iterator();

    boolean contains(Marker marker);

    boolean contains(String str);

    boolean equals(Object obj);

    int hashCode();
}
