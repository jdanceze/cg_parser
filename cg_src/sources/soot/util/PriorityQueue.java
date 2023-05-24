package soot.util;

import java.util.AbstractMap;
import java.util.AbstractQueue;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/* loaded from: gencallgraphv3.jar:soot/util/PriorityQueue.class */
public abstract class PriorityQueue<E> extends AbstractQueue<E> {
    private static final Logger logger;
    private final List<? extends E> universe;
    private final Map<E, Integer> ordinalMap;
    final int N;
    int min = Integer.MAX_VALUE;
    static final /* synthetic */ boolean $assertionsDisabled;

    abstract void addAll();

    abstract int nextSetBit(int i);

    abstract boolean remove(int i);

    abstract boolean add(int i);

    abstract boolean contains(int i);

    static {
        $assertionsDisabled = !PriorityQueue.class.desiredAssertionStatus();
        logger = LoggerFactory.getLogger(PriorityQueue.class);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PriorityQueue(List<? extends E> universe, Map<E, Integer> ordinalMap) {
        if (!$assertionsDisabled && ordinalMap.size() != universe.size()) {
            throw new AssertionError();
        }
        this.universe = universe;
        this.ordinalMap = ordinalMap;
        this.N = universe.size();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:soot/util/PriorityQueue$Itr.class */
    public abstract class Itr implements Iterator<E> {
        int next;
        long expected = getExpected();
        int now = Integer.MAX_VALUE;

        abstract long getExpected();

        /* JADX INFO: Access modifiers changed from: package-private */
        public Itr() {
            this.next = PriorityQueue.this.min;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.next < PriorityQueue.this.N;
        }

        @Override // java.util.Iterator
        public E next() {
            if (this.expected != getExpected()) {
                throw new ConcurrentModificationException();
            }
            if (this.next >= PriorityQueue.this.N) {
                throw new NoSuchElementException();
            }
            this.now = this.next;
            this.next = PriorityQueue.this.nextSetBit(this.next + 1);
            return (E) PriorityQueue.this.universe.get(this.now);
        }

        @Override // java.util.Iterator
        public void remove() {
            if (this.now >= PriorityQueue.this.N) {
                throw new IllegalStateException();
            }
            if (this.expected != getExpected()) {
                throw new ConcurrentModificationException();
            }
            PriorityQueue.this.remove(this.now);
            this.expected = getExpected();
            this.now = Integer.MAX_VALUE;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getOrdinal(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
        Integer i = this.ordinalMap.get(o);
        if (i == null) {
            throw new NoSuchElementException();
        }
        return i.intValue();
    }

    @Override // java.util.Queue
    public final E peek() {
        if (isEmpty()) {
            return null;
        }
        return this.universe.get(this.min);
    }

    @Override // java.util.Queue
    public final E poll() {
        if (isEmpty()) {
            return null;
        }
        E e = this.universe.get(this.min);
        remove(this.min);
        return e;
    }

    @Override // java.util.AbstractQueue, java.util.AbstractCollection, java.util.Collection, java.util.Queue
    public final boolean add(E e) {
        return offer(e);
    }

    @Override // java.util.Queue
    public final boolean offer(E e) {
        return add(getOrdinal(e));
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public final boolean remove(Object o) {
        if (o == null || isEmpty()) {
            return false;
        }
        try {
            if (o.equals(peek())) {
                remove(this.min);
                return true;
            }
            return remove(getOrdinal(o));
        } catch (NoSuchElementException e) {
            logger.debug(e.getMessage());
            return false;
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public final boolean contains(Object o) {
        if (o == null) {
            return false;
        }
        try {
            if (o.equals(peek())) {
                return true;
            }
            return contains(getOrdinal(o));
        } catch (NoSuchElementException e) {
            logger.debug(e.getMessage());
            return false;
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean isEmpty() {
        return this.min >= this.N;
    }

    public static <E> PriorityQueue<E> of(E[] eArr) {
        return of(Arrays.asList(eArr));
    }

    public static <E> PriorityQueue<E> noneOf(E[] eArr) {
        return noneOf(Arrays.asList(eArr));
    }

    public static <E> PriorityQueue<E> of(List<? extends E> universe) {
        PriorityQueue<E> q = noneOf(universe);
        q.addAll();
        return q;
    }

    public static <E> PriorityQueue<E> noneOf(List<? extends E> universe) {
        Map<E, Integer> ordinalMap = new HashMap<>((2 * universe.size()) / 3);
        int i = 0;
        for (E e : universe) {
            if (e == null) {
                throw new NullPointerException("null is not allowed");
            }
            int i2 = i;
            i++;
            if (ordinalMap.put(e, Integer.valueOf(i2)) != null) {
                throw new IllegalArgumentException("duplicate key found");
            }
        }
        return newPriorityQueue(universe, ordinalMap);
    }

    public static <E extends Numberable> PriorityQueue<E> of(List<? extends E> universe, boolean useNumberInterface) {
        PriorityQueue<E> q = noneOf(universe, useNumberInterface);
        q.addAll();
        return q;
    }

    public static <E extends Numberable> PriorityQueue<E> noneOf(final List<? extends E> universe, boolean useNumberInterface) {
        if (!useNumberInterface) {
            return noneOf(universe);
        }
        int i = 0;
        Iterator<? extends E> it = universe.iterator();
        while (it.hasNext()) {
            int i2 = i;
            i++;
            ((Numberable) it.next()).setNumber(i2);
        }
        return newPriorityQueue(universe, new AbstractMap<E, Integer>() { // from class: soot.util.PriorityQueue.1
            @Override // java.util.AbstractMap, java.util.Map
            public Integer get(Object key) {
                return Integer.valueOf(((Numberable) key).getNumber());
            }

            @Override // java.util.AbstractMap, java.util.Map
            public int size() {
                return universe.size();
            }

            @Override // java.util.AbstractMap, java.util.Map
            public Set<Map.Entry<E, Integer>> entrySet() {
                throw new UnsupportedOperationException();
            }
        });
    }

    private static <E> PriorityQueue<E> newPriorityQueue(List<? extends E> universe, Map<E, Integer> ordinalMap) {
        int universeSize = universe.size();
        if (universeSize <= 64) {
            return new SmallPriorityQueue(universe, ordinalMap);
        }
        if (universeSize <= 4096) {
            return new MediumPriorityQueue(universe, ordinalMap);
        }
        return new LargePriorityQueue(universe, ordinalMap);
    }
}
