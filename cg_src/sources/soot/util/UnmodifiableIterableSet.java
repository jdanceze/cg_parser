package soot.util;

import java.util.Iterator;
/* loaded from: gencallgraphv3.jar:soot/util/UnmodifiableIterableSet.class */
public class UnmodifiableIterableSet<E> extends IterableSet<E> {
    public UnmodifiableIterableSet() {
    }

    public UnmodifiableIterableSet(IterableSet<E> original) {
        Iterator<E> it = original.iterator();
        while (it.hasNext()) {
            E e = it.next();
            super.add(e);
        }
    }

    @Override // soot.util.IterableSet, soot.util.HashChain, java.util.AbstractCollection, java.util.Collection
    public boolean add(E o) {
        throw new RuntimeException("This set cannot be modified");
    }

    @Override // soot.util.IterableSet, soot.util.HashChain, java.util.AbstractCollection, java.util.Collection, soot.util.Chain
    public boolean remove(Object o) {
        throw new RuntimeException("This set cannot be modified");
    }

    public boolean forceRemove(Object o) {
        return super.remove(o);
    }
}
