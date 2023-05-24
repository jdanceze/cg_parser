package soot.util;

import java.util.AbstractList;
@Deprecated
/* loaded from: gencallgraphv3.jar:soot/util/SingletonList.class */
public class SingletonList<E> extends AbstractList<E> {
    private E o;

    public SingletonList(E o) {
        this.o = o;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public int size() {
        return 1;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean contains(Object other) {
        return other.equals(this.o);
    }

    @Override // java.util.AbstractList, java.util.List
    public E get(int index) {
        if (index != 0) {
            throw new IndexOutOfBoundsException("Singleton list; index = " + index);
        }
        return this.o;
    }
}
