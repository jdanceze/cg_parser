package gnu.trove.impl.sync;

import gnu.trove.function.TIntFunction;
import gnu.trove.list.TIntList;
import gnu.trove.procedure.TIntProcedure;
import java.util.Random;
import java.util.RandomAccess;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/sync/TSynchronizedIntList.class */
public class TSynchronizedIntList extends TSynchronizedIntCollection implements TIntList {
    static final long serialVersionUID = -7754090372962971524L;
    final TIntList list;

    public TSynchronizedIntList(TIntList list) {
        super(list);
        this.list = list;
    }

    public TSynchronizedIntList(TIntList list, Object mutex) {
        super(list, mutex);
        this.list = list;
    }

    @Override // gnu.trove.TIntCollection
    public boolean equals(Object o) {
        boolean equals;
        synchronized (this.mutex) {
            equals = this.list.equals(o);
        }
        return equals;
    }

    @Override // gnu.trove.TIntCollection
    public int hashCode() {
        int hashCode;
        synchronized (this.mutex) {
            hashCode = this.list.hashCode();
        }
        return hashCode;
    }

    @Override // gnu.trove.list.TIntList
    public int get(int index) {
        int i;
        synchronized (this.mutex) {
            i = this.list.get(index);
        }
        return i;
    }

    @Override // gnu.trove.list.TIntList
    public int set(int index, int element) {
        int i;
        synchronized (this.mutex) {
            i = this.list.set(index, element);
        }
        return i;
    }

    @Override // gnu.trove.list.TIntList
    public void set(int offset, int[] values) {
        synchronized (this.mutex) {
            this.list.set(offset, values);
        }
    }

    @Override // gnu.trove.list.TIntList
    public void set(int offset, int[] values, int valOffset, int length) {
        synchronized (this.mutex) {
            this.list.set(offset, values, valOffset, length);
        }
    }

    @Override // gnu.trove.list.TIntList
    public int replace(int offset, int val) {
        int replace;
        synchronized (this.mutex) {
            replace = this.list.replace(offset, val);
        }
        return replace;
    }

    @Override // gnu.trove.list.TIntList
    public void remove(int offset, int length) {
        synchronized (this.mutex) {
            this.list.remove(offset, length);
        }
    }

    @Override // gnu.trove.list.TIntList
    public int removeAt(int offset) {
        int removeAt;
        synchronized (this.mutex) {
            removeAt = this.list.removeAt(offset);
        }
        return removeAt;
    }

    @Override // gnu.trove.list.TIntList
    public void add(int[] vals) {
        synchronized (this.mutex) {
            this.list.add(vals);
        }
    }

    @Override // gnu.trove.list.TIntList
    public void add(int[] vals, int offset, int length) {
        synchronized (this.mutex) {
            this.list.add(vals, offset, length);
        }
    }

    @Override // gnu.trove.list.TIntList
    public void insert(int offset, int value) {
        synchronized (this.mutex) {
            this.list.insert(offset, value);
        }
    }

    @Override // gnu.trove.list.TIntList
    public void insert(int offset, int[] values) {
        synchronized (this.mutex) {
            this.list.insert(offset, values);
        }
    }

    @Override // gnu.trove.list.TIntList
    public void insert(int offset, int[] values, int valOffset, int len) {
        synchronized (this.mutex) {
            this.list.insert(offset, values, valOffset, len);
        }
    }

    @Override // gnu.trove.list.TIntList
    public int indexOf(int o) {
        int indexOf;
        synchronized (this.mutex) {
            indexOf = this.list.indexOf(o);
        }
        return indexOf;
    }

    @Override // gnu.trove.list.TIntList
    public int lastIndexOf(int o) {
        int lastIndexOf;
        synchronized (this.mutex) {
            lastIndexOf = this.list.lastIndexOf(o);
        }
        return lastIndexOf;
    }

    @Override // gnu.trove.list.TIntList
    public TIntList subList(int fromIndex, int toIndex) {
        TSynchronizedIntList tSynchronizedIntList;
        synchronized (this.mutex) {
            tSynchronizedIntList = new TSynchronizedIntList(this.list.subList(fromIndex, toIndex), this.mutex);
        }
        return tSynchronizedIntList;
    }

    @Override // gnu.trove.list.TIntList
    public int[] toArray(int offset, int len) {
        int[] array;
        synchronized (this.mutex) {
            array = this.list.toArray(offset, len);
        }
        return array;
    }

    @Override // gnu.trove.list.TIntList
    public int[] toArray(int[] dest, int offset, int len) {
        int[] array;
        synchronized (this.mutex) {
            array = this.list.toArray(dest, offset, len);
        }
        return array;
    }

    @Override // gnu.trove.list.TIntList
    public int[] toArray(int[] dest, int source_pos, int dest_pos, int len) {
        int[] array;
        synchronized (this.mutex) {
            array = this.list.toArray(dest, source_pos, dest_pos, len);
        }
        return array;
    }

    @Override // gnu.trove.list.TIntList
    public int indexOf(int offset, int value) {
        int indexOf;
        synchronized (this.mutex) {
            indexOf = this.list.indexOf(offset, value);
        }
        return indexOf;
    }

    @Override // gnu.trove.list.TIntList
    public int lastIndexOf(int offset, int value) {
        int lastIndexOf;
        synchronized (this.mutex) {
            lastIndexOf = this.list.lastIndexOf(offset, value);
        }
        return lastIndexOf;
    }

    @Override // gnu.trove.list.TIntList
    public void fill(int val) {
        synchronized (this.mutex) {
            this.list.fill(val);
        }
    }

    @Override // gnu.trove.list.TIntList
    public void fill(int fromIndex, int toIndex, int val) {
        synchronized (this.mutex) {
            this.list.fill(fromIndex, toIndex, val);
        }
    }

    @Override // gnu.trove.list.TIntList
    public void reverse() {
        synchronized (this.mutex) {
            this.list.reverse();
        }
    }

    @Override // gnu.trove.list.TIntList
    public void reverse(int from, int to) {
        synchronized (this.mutex) {
            this.list.reverse(from, to);
        }
    }

    @Override // gnu.trove.list.TIntList
    public void shuffle(Random rand) {
        synchronized (this.mutex) {
            this.list.shuffle(rand);
        }
    }

    @Override // gnu.trove.list.TIntList
    public void sort() {
        synchronized (this.mutex) {
            this.list.sort();
        }
    }

    @Override // gnu.trove.list.TIntList
    public void sort(int fromIndex, int toIndex) {
        synchronized (this.mutex) {
            this.list.sort(fromIndex, toIndex);
        }
    }

    @Override // gnu.trove.list.TIntList
    public int binarySearch(int value) {
        int binarySearch;
        synchronized (this.mutex) {
            binarySearch = this.list.binarySearch(value);
        }
        return binarySearch;
    }

    @Override // gnu.trove.list.TIntList
    public int binarySearch(int value, int fromIndex, int toIndex) {
        int binarySearch;
        synchronized (this.mutex) {
            binarySearch = this.list.binarySearch(value, fromIndex, toIndex);
        }
        return binarySearch;
    }

    @Override // gnu.trove.list.TIntList
    public TIntList grep(TIntProcedure condition) {
        TIntList grep;
        synchronized (this.mutex) {
            grep = this.list.grep(condition);
        }
        return grep;
    }

    @Override // gnu.trove.list.TIntList
    public TIntList inverseGrep(TIntProcedure condition) {
        TIntList inverseGrep;
        synchronized (this.mutex) {
            inverseGrep = this.list.inverseGrep(condition);
        }
        return inverseGrep;
    }

    @Override // gnu.trove.list.TIntList
    public int max() {
        int max;
        synchronized (this.mutex) {
            max = this.list.max();
        }
        return max;
    }

    @Override // gnu.trove.list.TIntList
    public int min() {
        int min;
        synchronized (this.mutex) {
            min = this.list.min();
        }
        return min;
    }

    @Override // gnu.trove.list.TIntList
    public int sum() {
        int sum;
        synchronized (this.mutex) {
            sum = this.list.sum();
        }
        return sum;
    }

    @Override // gnu.trove.list.TIntList
    public boolean forEachDescending(TIntProcedure procedure) {
        boolean forEachDescending;
        synchronized (this.mutex) {
            forEachDescending = this.list.forEachDescending(procedure);
        }
        return forEachDescending;
    }

    @Override // gnu.trove.list.TIntList
    public void transformValues(TIntFunction function) {
        synchronized (this.mutex) {
            this.list.transformValues(function);
        }
    }

    private Object readResolve() {
        return this.list instanceof RandomAccess ? new TSynchronizedRandomAccessIntList(this.list) : this;
    }
}
