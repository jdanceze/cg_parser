package soot.util;

import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
/* loaded from: gencallgraphv3.jar:soot/util/HashChain.class */
public class HashChain<E> extends AbstractCollection<E> implements Chain<E> {
    protected final Map<E, HashChain<E>.Link<E>> map;
    protected E firstItem;
    protected E lastItem;
    protected int stateCount;

    public HashChain() {
        this.stateCount = 0;
        this.map = new ConcurrentHashMap();
        this.firstItem = null;
        this.lastItem = null;
    }

    public HashChain(int initialCapacity) {
        this.stateCount = 0;
        this.map = new ConcurrentHashMap(initialCapacity);
        this.firstItem = null;
        this.lastItem = null;
    }

    public HashChain(Chain<E> src) {
        this(src.size());
        addAll(src);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/util/HashChain$EmptyIteratorSingleton.class */
    public static class EmptyIteratorSingleton {
        static final Iterator<Object> INSTANCE = new Iterator<Object>() { // from class: soot.util.HashChain.EmptyIteratorSingleton.1
            @Override // java.util.Iterator
            public boolean hasNext() {
                return false;
            }

            @Override // java.util.Iterator
            public Object next() {
                return null;
            }

            @Override // java.util.Iterator
            public void remove() {
            }
        };

        private EmptyIteratorSingleton() {
        }
    }

    protected static <X> Iterator<X> emptyIterator() {
        Iterator<X> retVal = (Iterator<X>) EmptyIteratorSingleton.INSTANCE;
        return retVal;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public synchronized void clear() {
        this.stateCount++;
        this.lastItem = null;
        this.firstItem = null;
        this.map.clear();
    }

    @Override // soot.util.Chain
    public synchronized void swapWith(E out, E in) {
        insertBefore(in, out);
        remove(out);
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public synchronized boolean add(E item) {
        addLast(item);
        return true;
    }

    @Override // soot.util.Chain
    public synchronized Collection<E> getElementsUnsorted() {
        return this.map.keySet();
    }

    @Deprecated
    public static <E> List<E> toList(Chain<E> c) {
        return new ArrayList(c);
    }

    @Override // soot.util.Chain
    public synchronized boolean follows(E someObject, E someReferenceObject) {
        try {
            Iterator<E> it = iterator(someReferenceObject);
            while (it.hasNext()) {
                if (it.next() == someObject) {
                    return true;
                }
            }
            return false;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public synchronized boolean contains(Object o) {
        return this.map.containsKey(o);
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public synchronized boolean containsAll(Collection<?> c) {
        for (Object next : c) {
            if (!this.map.containsKey(next)) {
                return false;
            }
        }
        return true;
    }

    @Override // soot.util.Chain
    public synchronized void insertAfter(E toInsert, E point) {
        if (toInsert == null) {
            throw new RuntimeException("Cannot insert a null object into a Chain!");
        }
        if (point == null) {
            throw new RuntimeException("Insertion point cannot be null!");
        }
        if (this.map.containsKey(toInsert)) {
            throw new RuntimeException("Chain already contains object.");
        }
        HashChain<E>.Link<E> temp = this.map.get(point);
        if (temp == null) {
            throw new RuntimeException("Insertion point not found in chain!");
        }
        this.stateCount++;
        HashChain<E>.Link<E> newLink = temp.insertAfter(toInsert);
        this.map.put(toInsert, newLink);
    }

    @Override // soot.util.Chain
    public synchronized void insertAfter(Collection<? extends E> toInsert, E point) {
        if (toInsert == null) {
            throw new RuntimeException("Cannot insert a null Collection into a Chain!");
        }
        if (point == null) {
            throw new RuntimeException("Insertion point cannot be null!");
        }
        E previousPoint = point;
        for (E o : toInsert) {
            insertAfter(o, previousPoint);
            previousPoint = o;
        }
    }

    @Override // soot.util.Chain
    public synchronized void insertAfter(List<E> toInsert, E point) {
        insertAfter((Collection<? extends List<E>>) toInsert, (List<E>) point);
    }

    @Override // soot.util.Chain
    public synchronized void insertAfter(Chain<E> toInsert, E point) {
        insertAfter((Collection<? extends Chain<E>>) toInsert, (Chain<E>) point);
    }

    @Override // soot.util.Chain
    public synchronized void insertBefore(E toInsert, E point) {
        if (toInsert == null) {
            throw new RuntimeException("Cannot insert a null object into a Chain!");
        }
        if (point == null) {
            throw new RuntimeException("Insertion point cannot be null!");
        }
        if (this.map.containsKey(toInsert)) {
            throw new RuntimeException("Chain already contains object.");
        }
        HashChain<E>.Link<E> temp = this.map.get(point);
        if (temp == null) {
            throw new RuntimeException("Insertion point not found in chain!");
        }
        this.stateCount++;
        HashChain<E>.Link<E> newLink = temp.insertBefore(toInsert);
        this.map.put(toInsert, newLink);
    }

    @Override // soot.util.Chain
    public synchronized void insertBefore(Collection<? extends E> toInsert, E point) {
        if (toInsert == null) {
            throw new RuntimeException("Cannot insert a null Collection into a Chain!");
        }
        if (point == null) {
            throw new RuntimeException("Insertion point cannot be null!");
        }
        for (E o : toInsert) {
            insertBefore(o, point);
        }
    }

    @Override // soot.util.Chain
    public synchronized void insertBefore(List<E> toInsert, E point) {
        insertBefore((Collection<? extends List<E>>) toInsert, (List<E>) point);
    }

    @Override // soot.util.Chain
    public synchronized void insertBefore(Chain<E> toInsert, E point) {
        insertBefore((Collection<? extends Chain<E>>) toInsert, (Chain<E>) point);
    }

    public static <T> HashChain<T> listToHashChain(List<T> list) {
        HashChain<T> c = new HashChain<>();
        for (T next : list) {
            c.addLast(next);
        }
        return c;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, soot.util.Chain
    public synchronized boolean remove(Object item) {
        if (item == null) {
            throw new RuntimeException("Cannot remove a null object from a Chain!");
        }
        this.stateCount++;
        HashChain<E>.Link<E> link = this.map.get(item);
        if (link != null) {
            link.unlinkSelf();
            this.map.remove(item);
            return true;
        }
        return false;
    }

    @Override // soot.util.Chain
    public synchronized void addFirst(E item) {
        HashChain<E>.Link<E> newLink;
        if (item == null) {
            throw new RuntimeException("Cannot insert a null object into a Chain!");
        }
        this.stateCount++;
        if (this.map.containsKey(item)) {
            throw new RuntimeException("Chain already contains object.");
        }
        if (this.firstItem != null) {
            HashChain<E>.Link<E> temp = this.map.get(this.firstItem);
            newLink = temp.insertBefore(item);
        } else {
            newLink = new Link<>(item);
            this.lastItem = item;
            this.firstItem = item;
        }
        this.map.put(item, newLink);
    }

    @Override // soot.util.Chain
    public synchronized void addLast(E item) {
        HashChain<E>.Link<E> newLink;
        if (item == null) {
            throw new RuntimeException("Cannot insert a null object into a Chain!");
        }
        this.stateCount++;
        if (this.map.containsKey(item)) {
            throw new RuntimeException("Chain already contains object: " + item);
        }
        if (this.lastItem != null) {
            HashChain<E>.Link<E> temp = this.map.get(this.lastItem);
            newLink = temp.insertAfter(item);
        } else {
            newLink = new Link<>(item);
            this.lastItem = item;
            this.firstItem = item;
        }
        this.map.put(item, newLink);
    }

    @Override // soot.util.Chain
    public synchronized void removeFirst() {
        this.stateCount++;
        E item = this.firstItem;
        this.map.get(item).unlinkSelf();
        this.map.remove(item);
    }

    @Override // soot.util.Chain
    public synchronized void removeLast() {
        this.stateCount++;
        E item = this.lastItem;
        this.map.get(item).unlinkSelf();
        this.map.remove(item);
    }

    @Override // soot.util.Chain
    public synchronized E getFirst() {
        if (this.firstItem == null) {
            throw new NoSuchElementException();
        }
        return this.firstItem;
    }

    @Override // soot.util.Chain
    public synchronized E getLast() {
        if (this.lastItem == null) {
            throw new NoSuchElementException();
        }
        return this.lastItem;
    }

    @Override // soot.util.Chain
    public synchronized E getSuccOf(E point) throws NoSuchElementException {
        HashChain<E>.Link<E> link = this.map.get(point);
        if (link == null) {
            throw new NoSuchElementException();
        }
        HashChain<E>.Link<E> link2 = link.getNext();
        if (link2 == null) {
            return null;
        }
        return link2.getItem();
    }

    @Override // soot.util.Chain
    public synchronized E getPredOf(E point) throws NoSuchElementException {
        if (point == null) {
            throw new RuntimeException("Chain cannot contain null objects!");
        }
        HashChain<E>.Link<E> link = this.map.get(point);
        if (link == null) {
            throw new NoSuchElementException();
        }
        HashChain<E>.Link<E> link2 = link.getPrevious();
        if (link2 == null) {
            return null;
        }
        return link2.getItem();
    }

    @Override // soot.util.Chain
    public Iterator<E> snapshotIterator() {
        if (this.firstItem == null || isEmpty()) {
            return emptyIterator();
        }
        return new ArrayList(this).iterator();
    }

    public Iterator<E> snapshotIterator(E from) {
        if (from == null || this.firstItem == null || isEmpty()) {
            return emptyIterator();
        }
        ArrayList<E> l = new ArrayList<>(this.map.size());
        Iterator<E> it = new LinkIterator<>(this, from);
        while (it.hasNext()) {
            E next = it.next();
            l.add(next);
        }
        return l.iterator();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, soot.util.Chain
    public synchronized Iterator<E> iterator() {
        if (this.firstItem == null || isEmpty()) {
            return emptyIterator();
        }
        return new LinkIterator(this, this.firstItem);
    }

    @Override // soot.util.Chain
    public synchronized Iterator<E> iterator(E from) {
        if (from == null || this.firstItem == null || isEmpty()) {
            return emptyIterator();
        }
        return new LinkIterator(this, from);
    }

    @Override // soot.util.Chain
    public synchronized Iterator<E> iterator(E head, E tail) {
        if (head == null || this.firstItem == null || isEmpty()) {
            return emptyIterator();
        }
        if (getPredOf(head) == tail) {
            return emptyIterator();
        }
        return new LinkIterator(head, tail);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, soot.util.Chain
    public synchronized int size() {
        return this.map.size();
    }

    @Override // java.util.AbstractCollection
    public synchronized String toString() {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append('[');
        boolean b = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            E next = it.next();
            if (!b) {
                b = true;
            } else {
                strBuf.append(", ");
            }
            strBuf.append(next.toString());
        }
        strBuf.append(']');
        return strBuf.toString();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:soot/util/HashChain$Link.class */
    public class Link<X extends E> implements Serializable {
        private HashChain<E>.Link<X> nextLink = null;
        private HashChain<E>.Link<X> previousLink = null;
        private X item;

        public Link(X item) {
            this.item = item;
        }

        public HashChain<E>.Link<X> getNext() {
            return this.nextLink;
        }

        public HashChain<E>.Link<X> getPrevious() {
            return this.previousLink;
        }

        public void setNext(HashChain<E>.Link<X> link) {
            this.nextLink = link;
        }

        public void setPrevious(HashChain<E>.Link<X> link) {
            this.previousLink = link;
        }

        public void unlinkSelf() {
            bind(this.previousLink, this.nextLink);
        }

        public HashChain<E>.Link<X> insertAfter(X item) {
            HashChain<E>.Link<X> newLink = new Link<>(item);
            bind(newLink, this.nextLink);
            bind(this, newLink);
            return newLink;
        }

        public HashChain<E>.Link<X> insertBefore(X item) {
            HashChain<E>.Link<X> newLink = new Link<>(item);
            bind(this.previousLink, newLink);
            bind(newLink, this);
            return newLink;
        }

        private void bind(HashChain<E>.Link<X> a, HashChain<E>.Link<X> b) {
            if (a == null) {
                HashChain.this.firstItem = (E) (b == null ? (E) null : b.item);
            } else {
                a.nextLink = b;
            }
            if (b == null) {
                HashChain.this.lastItem = (E) (a == null ? (E) null : a.item);
            } else {
                b.previousLink = a;
            }
        }

        public X getItem() {
            return this.item;
        }

        public String toString() {
            if (this.item != null) {
                return this.item.toString();
            }
            return "Link item is null: " + super.toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:soot/util/HashChain$LinkIterator.class */
    public class LinkIterator<X extends E> implements Iterator<E> {
        private final X destination;
        private HashChain<E>.Link<E> currentLink;
        private int iteratorStateCount;
        private boolean state;

        public LinkIterator(HashChain hashChain, X from) {
            this(from, null);
        }

        public LinkIterator(X from, X to) {
            if (from == null) {
                throw new RuntimeException("Chain cannot contain null objects!");
            }
            HashChain<E>.Link<E> nextLink = HashChain.this.map.get(from);
            if (nextLink == null) {
                throw new NoSuchElementException("HashChain.LinkIterator(obj) with obj that is not in the chain: " + from.toString());
            }
            this.destination = to;
            this.currentLink = new Link<>(null);
            this.currentLink.setNext(nextLink);
            this.iteratorStateCount = HashChain.this.stateCount;
            this.state = false;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (HashChain.this.stateCount != this.iteratorStateCount) {
                throw new ConcurrentModificationException();
            }
            return this.destination == null ? this.currentLink.getNext() != null : this.destination != this.currentLink.getItem();
        }

        @Override // java.util.Iterator
        public E next() throws NoSuchElementException {
            String exceptionMsg;
            if (HashChain.this.stateCount != this.iteratorStateCount) {
                throw new ConcurrentModificationException();
            }
            HashChain<E>.Link<E> temp = this.currentLink.getNext();
            if (temp == null) {
                if (this.destination != null && this.destination != this.currentLink.getItem()) {
                    exceptionMsg = "HashChain.LinkIterator.next() reached end of chain without reaching specified tail unit";
                } else {
                    exceptionMsg = "HashChain.LinkIterator.next() called past the end of the Chain";
                }
                throw new NoSuchElementException(exceptionMsg);
            }
            this.currentLink = temp;
            this.state = true;
            return this.currentLink.getItem();
        }

        @Override // java.util.Iterator
        public void remove() throws IllegalStateException {
            if (HashChain.this.stateCount != this.iteratorStateCount) {
                throw new ConcurrentModificationException();
            }
            HashChain.this.stateCount++;
            this.iteratorStateCount++;
            if (!this.state) {
                throw new IllegalStateException();
            }
            this.currentLink.unlinkSelf();
            HashChain.this.map.remove(this.currentLink.getItem());
            this.state = false;
        }

        public String toString() {
            if (this.currentLink == null) {
                return "Current object under iterator is null" + super.toString();
            }
            return this.currentLink.toString();
        }
    }

    @Override // soot.util.Chain
    public long getModificationCount() {
        return this.stateCount;
    }
}
