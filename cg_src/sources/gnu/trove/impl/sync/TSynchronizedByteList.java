package gnu.trove.impl.sync;

import gnu.trove.function.TByteFunction;
import gnu.trove.list.TByteList;
import gnu.trove.procedure.TByteProcedure;
import java.util.Random;
import java.util.RandomAccess;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/sync/TSynchronizedByteList.class */
public class TSynchronizedByteList extends TSynchronizedByteCollection implements TByteList {
    static final long serialVersionUID = -7754090372962971524L;
    final TByteList list;

    public TSynchronizedByteList(TByteList list) {
        super(list);
        this.list = list;
    }

    public TSynchronizedByteList(TByteList list, Object mutex) {
        super(list, mutex);
        this.list = list;
    }

    @Override // gnu.trove.TByteCollection
    public boolean equals(Object o) {
        boolean equals;
        synchronized (this.mutex) {
            equals = this.list.equals(o);
        }
        return equals;
    }

    @Override // gnu.trove.TByteCollection
    public int hashCode() {
        int hashCode;
        synchronized (this.mutex) {
            hashCode = this.list.hashCode();
        }
        return hashCode;
    }

    @Override // gnu.trove.list.TByteList
    public byte get(int index) {
        byte b;
        synchronized (this.mutex) {
            b = this.list.get(index);
        }
        return b;
    }

    @Override // gnu.trove.list.TByteList
    public byte set(int index, byte element) {
        byte b;
        synchronized (this.mutex) {
            b = this.list.set(index, element);
        }
        return b;
    }

    @Override // gnu.trove.list.TByteList
    public void set(int offset, byte[] values) {
        synchronized (this.mutex) {
            this.list.set(offset, values);
        }
    }

    @Override // gnu.trove.list.TByteList
    public void set(int offset, byte[] values, int valOffset, int length) {
        synchronized (this.mutex) {
            this.list.set(offset, values, valOffset, length);
        }
    }

    @Override // gnu.trove.list.TByteList
    public byte replace(int offset, byte val) {
        byte replace;
        synchronized (this.mutex) {
            replace = this.list.replace(offset, val);
        }
        return replace;
    }

    @Override // gnu.trove.list.TByteList
    public void remove(int offset, int length) {
        synchronized (this.mutex) {
            this.list.remove(offset, length);
        }
    }

    @Override // gnu.trove.list.TByteList
    public byte removeAt(int offset) {
        byte removeAt;
        synchronized (this.mutex) {
            removeAt = this.list.removeAt(offset);
        }
        return removeAt;
    }

    @Override // gnu.trove.list.TByteList
    public void add(byte[] vals) {
        synchronized (this.mutex) {
            this.list.add(vals);
        }
    }

    @Override // gnu.trove.list.TByteList
    public void add(byte[] vals, int offset, int length) {
        synchronized (this.mutex) {
            this.list.add(vals, offset, length);
        }
    }

    @Override // gnu.trove.list.TByteList
    public void insert(int offset, byte value) {
        synchronized (this.mutex) {
            this.list.insert(offset, value);
        }
    }

    @Override // gnu.trove.list.TByteList
    public void insert(int offset, byte[] values) {
        synchronized (this.mutex) {
            this.list.insert(offset, values);
        }
    }

    @Override // gnu.trove.list.TByteList
    public void insert(int offset, byte[] values, int valOffset, int len) {
        synchronized (this.mutex) {
            this.list.insert(offset, values, valOffset, len);
        }
    }

    @Override // gnu.trove.list.TByteList
    public int indexOf(byte o) {
        int indexOf;
        synchronized (this.mutex) {
            indexOf = this.list.indexOf(o);
        }
        return indexOf;
    }

    @Override // gnu.trove.list.TByteList
    public int lastIndexOf(byte o) {
        int lastIndexOf;
        synchronized (this.mutex) {
            lastIndexOf = this.list.lastIndexOf(o);
        }
        return lastIndexOf;
    }

    @Override // gnu.trove.list.TByteList
    public TByteList subList(int fromIndex, int toIndex) {
        TSynchronizedByteList tSynchronizedByteList;
        synchronized (this.mutex) {
            tSynchronizedByteList = new TSynchronizedByteList(this.list.subList(fromIndex, toIndex), this.mutex);
        }
        return tSynchronizedByteList;
    }

    @Override // gnu.trove.list.TByteList
    public byte[] toArray(int offset, int len) {
        byte[] array;
        synchronized (this.mutex) {
            array = this.list.toArray(offset, len);
        }
        return array;
    }

    @Override // gnu.trove.list.TByteList
    public byte[] toArray(byte[] dest, int offset, int len) {
        byte[] array;
        synchronized (this.mutex) {
            array = this.list.toArray(dest, offset, len);
        }
        return array;
    }

    @Override // gnu.trove.list.TByteList
    public byte[] toArray(byte[] dest, int source_pos, int dest_pos, int len) {
        byte[] array;
        synchronized (this.mutex) {
            array = this.list.toArray(dest, source_pos, dest_pos, len);
        }
        return array;
    }

    @Override // gnu.trove.list.TByteList
    public int indexOf(int offset, byte value) {
        int indexOf;
        synchronized (this.mutex) {
            indexOf = this.list.indexOf(offset, value);
        }
        return indexOf;
    }

    @Override // gnu.trove.list.TByteList
    public int lastIndexOf(int offset, byte value) {
        int lastIndexOf;
        synchronized (this.mutex) {
            lastIndexOf = this.list.lastIndexOf(offset, value);
        }
        return lastIndexOf;
    }

    @Override // gnu.trove.list.TByteList
    public void fill(byte val) {
        synchronized (this.mutex) {
            this.list.fill(val);
        }
    }

    @Override // gnu.trove.list.TByteList
    public void fill(int fromIndex, int toIndex, byte val) {
        synchronized (this.mutex) {
            this.list.fill(fromIndex, toIndex, val);
        }
    }

    @Override // gnu.trove.list.TByteList
    public void reverse() {
        synchronized (this.mutex) {
            this.list.reverse();
        }
    }

    @Override // gnu.trove.list.TByteList
    public void reverse(int from, int to) {
        synchronized (this.mutex) {
            this.list.reverse(from, to);
        }
    }

    @Override // gnu.trove.list.TByteList
    public void shuffle(Random rand) {
        synchronized (this.mutex) {
            this.list.shuffle(rand);
        }
    }

    @Override // gnu.trove.list.TByteList
    public void sort() {
        synchronized (this.mutex) {
            this.list.sort();
        }
    }

    @Override // gnu.trove.list.TByteList
    public void sort(int fromIndex, int toIndex) {
        synchronized (this.mutex) {
            this.list.sort(fromIndex, toIndex);
        }
    }

    @Override // gnu.trove.list.TByteList
    public int binarySearch(byte value) {
        int binarySearch;
        synchronized (this.mutex) {
            binarySearch = this.list.binarySearch(value);
        }
        return binarySearch;
    }

    @Override // gnu.trove.list.TByteList
    public int binarySearch(byte value, int fromIndex, int toIndex) {
        int binarySearch;
        synchronized (this.mutex) {
            binarySearch = this.list.binarySearch(value, fromIndex, toIndex);
        }
        return binarySearch;
    }

    @Override // gnu.trove.list.TByteList
    public TByteList grep(TByteProcedure condition) {
        TByteList grep;
        synchronized (this.mutex) {
            grep = this.list.grep(condition);
        }
        return grep;
    }

    @Override // gnu.trove.list.TByteList
    public TByteList inverseGrep(TByteProcedure condition) {
        TByteList inverseGrep;
        synchronized (this.mutex) {
            inverseGrep = this.list.inverseGrep(condition);
        }
        return inverseGrep;
    }

    @Override // gnu.trove.list.TByteList
    public byte max() {
        byte max;
        synchronized (this.mutex) {
            max = this.list.max();
        }
        return max;
    }

    @Override // gnu.trove.list.TByteList
    public byte min() {
        byte min;
        synchronized (this.mutex) {
            min = this.list.min();
        }
        return min;
    }

    @Override // gnu.trove.list.TByteList
    public byte sum() {
        byte sum;
        synchronized (this.mutex) {
            sum = this.list.sum();
        }
        return sum;
    }

    @Override // gnu.trove.list.TByteList
    public boolean forEachDescending(TByteProcedure procedure) {
        boolean forEachDescending;
        synchronized (this.mutex) {
            forEachDescending = this.list.forEachDescending(procedure);
        }
        return forEachDescending;
    }

    @Override // gnu.trove.list.TByteList
    public void transformValues(TByteFunction function) {
        synchronized (this.mutex) {
            this.list.transformValues(function);
        }
    }

    private Object readResolve() {
        return this.list instanceof RandomAccess ? new TSynchronizedRandomAccessByteList(this.list) : this;
    }
}
