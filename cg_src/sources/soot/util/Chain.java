package soot.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
/* loaded from: gencallgraphv3.jar:soot/util/Chain.class */
public interface Chain<E> extends Collection<E>, Serializable {
    void insertBefore(E e, E e2);

    void insertAfter(E e, E e2);

    void insertBefore(Chain<E> chain, E e);

    void insertAfter(Chain<E> chain, E e);

    void insertBefore(List<E> list, E e);

    void insertAfter(List<E> list, E e);

    void insertBefore(Collection<? extends E> collection, E e);

    void insertAfter(Collection<? extends E> collection, E e);

    void swapWith(E e, E e2);

    @Override // java.util.Collection, soot.util.Chain
    boolean remove(Object obj);

    void addFirst(E e);

    void addLast(E e);

    void removeFirst();

    void removeLast();

    boolean follows(E e, E e2);

    E getFirst();

    E getLast();

    E getSuccOf(E e);

    E getPredOf(E e);

    Iterator<E> snapshotIterator();

    @Override // java.util.Collection, java.lang.Iterable, soot.util.Chain
    Iterator<E> iterator();

    Iterator<E> iterator(E e);

    Iterator<E> iterator(E e, E e2);

    @Override // java.util.Collection, soot.util.Chain
    int size();

    long getModificationCount();

    Collection<E> getElementsUnsorted();
}
