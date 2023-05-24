package org.mockito.internal.util.collections;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/collections/Iterables.class */
public class Iterables {
    public static <T> Iterable<T> toIterable(Enumeration<T> in) {
        List<T> out = new LinkedList<>();
        while (in.hasMoreElements()) {
            out.add(in.nextElement());
        }
        return out;
    }

    public static <T> T firstOf(Iterable<T> iterable) {
        Iterator<T> iterator = iterable.iterator();
        if (!iterator.hasNext()) {
            throw new IllegalArgumentException("Cannot provide 1st element from empty iterable: " + iterable);
        }
        return iterator.next();
    }
}
