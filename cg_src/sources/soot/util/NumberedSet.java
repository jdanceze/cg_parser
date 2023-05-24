package soot.util;

import java.util.Iterator;
import soot.util.Numberable;
/* loaded from: gencallgraphv3.jar:soot/util/NumberedSet.class */
public final class NumberedSet<N extends Numberable> {
    private final IterableNumberer<N> universe;
    private BitVector bits;
    private Numberable[] array = new Numberable[8];
    private int size = 0;

    public NumberedSet(IterableNumberer<N> universe) {
        this.universe = universe;
    }

    public boolean add(Numberable o) {
        if (this.array != null) {
            int pos = findPosition(o);
            if (this.array[pos] == o) {
                return false;
            }
            this.size++;
            if (this.size * 3 > this.array.length * 2) {
                doubleSize();
                if (this.array != null) {
                    pos = findPosition(o);
                } else {
                    int number = o.getNumber();
                    if (number == 0) {
                        throw new RuntimeException("unnumbered");
                    }
                    return this.bits.set(number);
                }
            }
            this.array[pos] = o;
            return true;
        }
        int number2 = o.getNumber();
        if (number2 == 0) {
            throw new RuntimeException("unnumbered");
        }
        if (this.bits.set(number2)) {
            this.size++;
            return true;
        }
        return false;
    }

    public boolean contains(Numberable o) {
        if (this.array != null) {
            return this.array[findPosition(o)] != null;
        }
        int number = o.getNumber();
        if (number == 0) {
            throw new RuntimeException("unnumbered");
        }
        return this.bits.get(number);
    }

    private int findPosition(Numberable o) {
        int number;
        int number2 = o.getNumber();
        if (number2 == 0) {
            throw new RuntimeException("unnumbered");
        }
        int i = number2;
        int length = this.array.length;
        while (true) {
            number = i & (length - 1);
            Numberable temp = this.array[number];
            if (temp == o || temp == null) {
                break;
            }
            i = number + 1;
            length = this.array.length;
        }
        return number;
    }

    private void doubleSize() {
        int uniSize = this.universe.size();
        if (this.array.length * 128 > uniSize) {
            this.bits = new BitVector(uniSize);
            Numberable[] oldArray = this.array;
            this.array = null;
            for (Numberable element : oldArray) {
                if (element != null) {
                    this.bits.set(element.getNumber());
                }
            }
            return;
        }
        Numberable[] oldArray2 = this.array;
        this.array = new Numberable[this.array.length * 2];
        for (Numberable element2 : oldArray2) {
            if (element2 != null) {
                this.array[findPosition(element2)] = element2;
            }
        }
    }

    public final int size() {
        return this.size;
    }

    public Iterator<N> iterator() {
        if (this.array == null) {
            return new BitSetIterator();
        }
        return new NumberedSetIterator();
    }

    /* loaded from: gencallgraphv3.jar:soot/util/NumberedSet$BitSetIterator.class */
    private class BitSetIterator implements Iterator<N> {
        private final soot.util.BitSetIterator iter;

        BitSetIterator() {
            this.iter = NumberedSet.this.bits.iterator();
        }

        @Override // java.util.Iterator
        public final boolean hasNext() {
            return this.iter.hasNext();
        }

        @Override // java.util.Iterator
        public final N next() {
            return (N) NumberedSet.this.universe.get(this.iter.next());
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/util/NumberedSet$NumberedSetIterator.class */
    private class NumberedSetIterator implements Iterator<N> {
        private int cur = 0;

        NumberedSetIterator() {
            seekNext();
        }

        protected final void seekNext() {
            Numberable[] temp = NumberedSet.this.array;
            while (temp[this.cur] == null) {
                try {
                    this.cur++;
                } catch (ArrayIndexOutOfBoundsException e) {
                    this.cur = -1;
                    return;
                }
            }
        }

        @Override // java.util.Iterator
        public final boolean hasNext() {
            return this.cur != -1;
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Iterator
        public final N next() {
            N ret = (N) NumberedSet.this.array[this.cur];
            this.cur++;
            seekNext();
            return ret;
        }
    }
}
