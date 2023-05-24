package soot.jimple.toolkits.typing.fast;

import java.util.AbstractList;
@Deprecated
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/typing/fast/EmptyList.class */
public class EmptyList<E> extends AbstractList<E> {
    @Override // java.util.AbstractList, java.util.List
    public E get(int index) {
        throw new IndexOutOfBoundsException();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public int size() {
        return 0;
    }
}
