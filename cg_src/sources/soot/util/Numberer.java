package soot.util;
/* loaded from: gencallgraphv3.jar:soot/util/Numberer.class */
public interface Numberer<E> {
    void add(E e);

    boolean remove(E e);

    long get(E e);

    E get(long j);

    int size();
}
