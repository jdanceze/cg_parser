package org.mockito.internal.util.collections;

import java.util.Iterator;
import java.util.LinkedList;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/collections/IdentitySet.class */
public class IdentitySet {
    private final LinkedList list = new LinkedList();

    public boolean contains(Object o) {
        Iterator it = this.list.iterator();
        while (it.hasNext()) {
            Object existing = it.next();
            if (existing == o) {
                return true;
            }
        }
        return false;
    }

    public void add(Object o) {
        this.list.add(o);
    }
}
