package gnu.trove.impl.sync;

import gnu.trove.function.TShortFunction;
import gnu.trove.list.TShortList;
import gnu.trove.procedure.TShortProcedure;
import java.util.Random;
import java.util.RandomAccess;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/sync/TSynchronizedShortList.class */
public class TSynchronizedShortList extends TSynchronizedShortCollection implements TShortList {
    static final long serialVersionUID = -7754090372962971524L;
    final TShortList list;

    public TSynchronizedShortList(TShortList list) {
        super(list);
        this.list = list;
    }

    public TSynchronizedShortList(TShortList list, Object mutex) {
        super(list, mutex);
        this.list = list;
    }

    @Override // gnu.trove.TShortCollection
    public boolean equals(Object o) {
        boolean equals;
        synchronized (this.mutex) {
            equals = this.list.equals(o);
        }
        return equals;
    }

    @Override // gnu.trove.TShortCollection
    public int hashCode() {
        int hashCode;
        synchronized (this.mutex) {
            hashCode = this.list.hashCode();
        }
        return hashCode;
    }

    @Override // gnu.trove.list.TShortList
    public short get(int index) {
        short s;
        synchronized (this.mutex) {
            s = this.list.get(index);
        }
        return s;
    }

    @Override // gnu.trove.list.TShortList
    public short set(int index, short element) {
        short s;
        synchronized (this.mutex) {
            s = this.list.set(index, element);
        }
        return s;
    }

    @Override // gnu.trove.list.TShortList
    public void set(int offset, short[] values) {
        synchronized (this.mutex) {
            this.list.set(offset, values);
        }
    }

    @Override // gnu.trove.list.TShortList
    public void set(int offset, short[] values, int valOffset, int length) {
        synchronized (this.mutex) {
            this.list.set(offset, values, valOffset, length);
        }
    }

    @Override // gnu.trove.list.TShortList
    public short replace(int offset, short val) {
        short replace;
        synchronized (this.mutex) {
            replace = this.list.replace(offset, val);
        }
        return replace;
    }

    @Override // gnu.trove.list.TShortList
    public void remove(int offset, int length) {
        synchronized (this.mutex) {
            this.list.remove(offset, length);
        }
    }

    @Override // gnu.trove.list.TShortList
    public short removeAt(int offset) {
        short removeAt;
        synchronized (this.mutex) {
            removeAt = this.list.removeAt(offset);
        }
        return removeAt;
    }

    @Override // gnu.trove.list.TShortList
    public void add(short[] vals) {
        synchronized (this.mutex) {
            this.list.add(vals);
        }
    }

    @Override // gnu.trove.list.TShortList
    public void add(short[] vals, int offset, int length) {
        synchronized (this.mutex) {
            this.list.add(vals, offset, length);
        }
    }

    @Override // gnu.trove.list.TShortList
    public void insert(int offset, short value) {
        synchronized (this.mutex) {
            this.list.insert(offset, value);
        }
    }

    @Override // gnu.trove.list.TShortList
    public void insert(int offset, short[] values) {
        synchronized (this.mutex) {
            this.list.insert(offset, values);
        }
    }

    @Override // gnu.trove.list.TShortList
    public void insert(int offset, short[] values, int valOffset, int len) {
        synchronized (this.mutex) {
            this.list.insert(offset, values, valOffset, len);
        }
    }

    @Override // gnu.trove.list.TShortList
    public int indexOf(short o) {
        int indexOf;
        synchronized (this.mutex) {
            indexOf = this.list.indexOf(o);
        }
        return indexOf;
    }

    @Override // gnu.trove.list.TShortList
    public int lastIndexOf(short o) {
        int lastIndexOf;
        synchronized (this.mutex) {
            lastIndexOf = this.list.lastIndexOf(o);
        }
        return lastIndexOf;
    }

    public TShortList subList(int fromIndex, int toIndex) {
        TSynchronizedShortList tSynchronizedShortList;
        synchronized (this.mutex) {
            tSynchronizedShortList = new TSynchronizedShortList(this.list.subList(fromIndex, toIndex), this.mutex);
        }
        return tSynchronizedShortList;
    }

    @Override // gnu.trove.list.TShortList
    public short[] toArray(int offset, int len) {
        short[] array;
        synchronized (this.mutex) {
            array = this.list.toArray(offset, len);
        }
        return array;
    }

    @Override // gnu.trove.list.TShortList
    public short[] toArray(short[] dest, int offset, int len) {
        short[] array;
        synchronized (this.mutex) {
            array = this.list.toArray(dest, offset, len);
        }
        return array;
    }

    @Override // gnu.trove.list.TShortList
    public short[] toArray(short[] dest, int source_pos, int dest_pos, int len) {
        short[] array;
        synchronized (this.mutex) {
            array = this.list.toArray(dest, source_pos, dest_pos, len);
        }
        return array;
    }

    @Override // gnu.trove.list.TShortList
    public int indexOf(int offset, short value) {
        int indexOf;
        synchronized (this.mutex) {
            indexOf = this.list.indexOf(offset, value);
        }
        return indexOf;
    }

    @Override // gnu.trove.list.TShortList
    public int lastIndexOf(int offset, short value) {
        int lastIndexOf;
        synchronized (this.mutex) {
            lastIndexOf = this.list.lastIndexOf(offset, value);
        }
        return lastIndexOf;
    }

    @Override // gnu.trove.list.TShortList
    public void fill(short val) {
        synchronized (this.mutex) {
            this.list.fill(val);
        }
    }

    @Override // gnu.trove.list.TShortList
    public void fill(int fromIndex, int toIndex, short val) {
        synchronized (this.mutex) {
            this.list.fill(fromIndex, toIndex, val);
        }
    }

    @Override // gnu.trove.list.TShortList
    public void reverse() {
        synchronized (this.mutex) {
            this.list.reverse();
        }
    }

    @Override // gnu.trove.list.TShortList
    public void reverse(int from, int to) {
        synchronized (this.mutex) {
            this.list.reverse(from, to);
        }
    }

    @Override // gnu.trove.list.TShortList
    public void shuffle(Random rand) {
        synchronized (this.mutex) {
            this.list.shuffle(rand);
        }
    }

    @Override // gnu.trove.list.TShortList
    public void sort() {
        synchronized (this.mutex) {
            this.list.sort();
        }
    }

    @Override // gnu.trove.list.TShortList
    public void sort(int fromIndex, int toIndex) {
        synchronized (this.mutex) {
            this.list.sort(fromIndex, toIndex);
        }
    }

    @Override // gnu.trove.list.TShortList
    public int binarySearch(short value) {
        int binarySearch;
        synchronized (this.mutex) {
            binarySearch = this.list.binarySearch(value);
        }
        return binarySearch;
    }

    @Override // gnu.trove.list.TShortList
    public int binarySearch(short value, int fromIndex, int toIndex) {
        int binarySearch;
        synchronized (this.mutex) {
            binarySearch = this.list.binarySearch(value, fromIndex, toIndex);
        }
        return binarySearch;
    }

    @Override // gnu.trove.list.TShortList
    public TShortList grep(TShortProcedure condition) {
        TShortList grep;
        synchronized (this.mutex) {
            grep = this.list.grep(condition);
        }
        return grep;
    }

    @Override // gnu.trove.list.TShortList
    public TShortList inverseGrep(TShortProcedure condition) {
        TShortList inverseGrep;
        synchronized (this.mutex) {
            inverseGrep = this.list.inverseGrep(condition);
        }
        return inverseGrep;
    }

    @Override // gnu.trove.list.TShortList
    public short max() {
        short max;
        synchronized (this.mutex) {
            max = this.list.max();
        }
        return max;
    }

    @Override // gnu.trove.list.TShortList
    public short min() {
        short min;
        synchronized (this.mutex) {
            min = this.list.min();
        }
        return min;
    }

    @Override // gnu.trove.list.TShortList
    public short sum() {
        short sum;
        synchronized (this.mutex) {
            sum = this.list.sum();
        }
        return sum;
    }

    @Override // gnu.trove.list.TShortList
    public boolean forEachDescending(TShortProcedure procedure) {
        boolean forEachDescending;
        synchronized (this.mutex) {
            forEachDescending = this.list.forEachDescending(procedure);
        }
        return forEachDescending;
    }

    @Override // gnu.trove.list.TShortList
    public void transformValues(TShortFunction function) {
        synchronized (this.mutex) {
            this.list.transformValues(function);
        }
    }

    private Object readResolve() {
        return this.list instanceof RandomAccess ? new TSynchronizedRandomAccessShortList(this.list) : this;
    }
}
