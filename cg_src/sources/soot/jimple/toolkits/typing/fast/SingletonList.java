package soot.jimple.toolkits.typing.fast;

import java.util.AbstractList;
@Deprecated
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/typing/fast/SingletonList.class */
public class SingletonList<E> extends AbstractList<E> {
    private final E e;

    public SingletonList(E e) {
        this.e = e;
    }

    @Override // java.util.AbstractList, java.util.List
    public E get(int index) {
        if (index != 0) {
            throw new IndexOutOfBoundsException();
        }
        return this.e;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public int size() {
        return 1;
    }
}
