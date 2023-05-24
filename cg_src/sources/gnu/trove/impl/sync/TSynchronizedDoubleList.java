package gnu.trove.impl.sync;

import gnu.trove.function.TDoubleFunction;
import gnu.trove.list.TDoubleList;
import gnu.trove.procedure.TDoubleProcedure;
import java.util.Random;
import java.util.RandomAccess;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/sync/TSynchronizedDoubleList.class */
public class TSynchronizedDoubleList extends TSynchronizedDoubleCollection implements TDoubleList {
    static final long serialVersionUID = -7754090372962971524L;
    final TDoubleList list;

    public TSynchronizedDoubleList(TDoubleList list) {
        super(list);
        this.list = list;
    }

    public TSynchronizedDoubleList(TDoubleList list, Object mutex) {
        super(list, mutex);
        this.list = list;
    }

    @Override // gnu.trove.TDoubleCollection
    public boolean equals(Object o) {
        boolean equals;
        synchronized (this.mutex) {
            equals = this.list.equals(o);
        }
        return equals;
    }

    @Override // gnu.trove.TDoubleCollection
    public int hashCode() {
        int hashCode;
        synchronized (this.mutex) {
            hashCode = this.list.hashCode();
        }
        return hashCode;
    }

    @Override // gnu.trove.list.TDoubleList
    public double get(int index) {
        double d;
        synchronized (this.mutex) {
            d = this.list.get(index);
        }
        return d;
    }

    @Override // gnu.trove.list.TDoubleList
    public double set(int index, double element) {
        double d;
        synchronized (this.mutex) {
            d = this.list.set(index, element);
        }
        return d;
    }

    @Override // gnu.trove.list.TDoubleList
    public void set(int offset, double[] values) {
        synchronized (this.mutex) {
            this.list.set(offset, values);
        }
    }

    @Override // gnu.trove.list.TDoubleList
    public void set(int offset, double[] values, int valOffset, int length) {
        synchronized (this.mutex) {
            this.list.set(offset, values, valOffset, length);
        }
    }

    @Override // gnu.trove.list.TDoubleList
    public double replace(int offset, double val) {
        double replace;
        synchronized (this.mutex) {
            replace = this.list.replace(offset, val);
        }
        return replace;
    }

    @Override // gnu.trove.list.TDoubleList
    public void remove(int offset, int length) {
        synchronized (this.mutex) {
            this.list.remove(offset, length);
        }
    }

    @Override // gnu.trove.list.TDoubleList
    public double removeAt(int offset) {
        double removeAt;
        synchronized (this.mutex) {
            removeAt = this.list.removeAt(offset);
        }
        return removeAt;
    }

    @Override // gnu.trove.list.TDoubleList
    public void add(double[] vals) {
        synchronized (this.mutex) {
            this.list.add(vals);
        }
    }

    @Override // gnu.trove.list.TDoubleList
    public void add(double[] vals, int offset, int length) {
        synchronized (this.mutex) {
            this.list.add(vals, offset, length);
        }
    }

    @Override // gnu.trove.list.TDoubleList
    public void insert(int offset, double value) {
        synchronized (this.mutex) {
            this.list.insert(offset, value);
        }
    }

    @Override // gnu.trove.list.TDoubleList
    public void insert(int offset, double[] values) {
        synchronized (this.mutex) {
            this.list.insert(offset, values);
        }
    }

    @Override // gnu.trove.list.TDoubleList
    public void insert(int offset, double[] values, int valOffset, int len) {
        synchronized (this.mutex) {
            this.list.insert(offset, values, valOffset, len);
        }
    }

    @Override // gnu.trove.list.TDoubleList
    public int indexOf(double o) {
        int indexOf;
        synchronized (this.mutex) {
            indexOf = this.list.indexOf(o);
        }
        return indexOf;
    }

    @Override // gnu.trove.list.TDoubleList
    public int lastIndexOf(double o) {
        int lastIndexOf;
        synchronized (this.mutex) {
            lastIndexOf = this.list.lastIndexOf(o);
        }
        return lastIndexOf;
    }

    @Override // gnu.trove.list.TDoubleList
    public TDoubleList subList(int fromIndex, int toIndex) {
        TSynchronizedDoubleList tSynchronizedDoubleList;
        synchronized (this.mutex) {
            tSynchronizedDoubleList = new TSynchronizedDoubleList(this.list.subList(fromIndex, toIndex), this.mutex);
        }
        return tSynchronizedDoubleList;
    }

    @Override // gnu.trove.list.TDoubleList
    public double[] toArray(int offset, int len) {
        double[] array;
        synchronized (this.mutex) {
            array = this.list.toArray(offset, len);
        }
        return array;
    }

    @Override // gnu.trove.list.TDoubleList
    public double[] toArray(double[] dest, int offset, int len) {
        double[] array;
        synchronized (this.mutex) {
            array = this.list.toArray(dest, offset, len);
        }
        return array;
    }

    @Override // gnu.trove.list.TDoubleList
    public double[] toArray(double[] dest, int source_pos, int dest_pos, int len) {
        double[] array;
        synchronized (this.mutex) {
            array = this.list.toArray(dest, source_pos, dest_pos, len);
        }
        return array;
    }

    @Override // gnu.trove.list.TDoubleList
    public int indexOf(int offset, double value) {
        int indexOf;
        synchronized (this.mutex) {
            indexOf = this.list.indexOf(offset, value);
        }
        return indexOf;
    }

    @Override // gnu.trove.list.TDoubleList
    public int lastIndexOf(int offset, double value) {
        int lastIndexOf;
        synchronized (this.mutex) {
            lastIndexOf = this.list.lastIndexOf(offset, value);
        }
        return lastIndexOf;
    }

    @Override // gnu.trove.list.TDoubleList
    public void fill(double val) {
        synchronized (this.mutex) {
            this.list.fill(val);
        }
    }

    @Override // gnu.trove.list.TDoubleList
    public void fill(int fromIndex, int toIndex, double val) {
        synchronized (this.mutex) {
            this.list.fill(fromIndex, toIndex, val);
        }
    }

    @Override // gnu.trove.list.TDoubleList
    public void reverse() {
        synchronized (this.mutex) {
            this.list.reverse();
        }
    }

    @Override // gnu.trove.list.TDoubleList
    public void reverse(int from, int to) {
        synchronized (this.mutex) {
            this.list.reverse(from, to);
        }
    }

    @Override // gnu.trove.list.TDoubleList
    public void shuffle(Random rand) {
        synchronized (this.mutex) {
            this.list.shuffle(rand);
        }
    }

    @Override // gnu.trove.list.TDoubleList
    public void sort() {
        synchronized (this.mutex) {
            this.list.sort();
        }
    }

    @Override // gnu.trove.list.TDoubleList
    public void sort(int fromIndex, int toIndex) {
        synchronized (this.mutex) {
            this.list.sort(fromIndex, toIndex);
        }
    }

    @Override // gnu.trove.list.TDoubleList
    public int binarySearch(double value) {
        int binarySearch;
        synchronized (this.mutex) {
            binarySearch = this.list.binarySearch(value);
        }
        return binarySearch;
    }

    @Override // gnu.trove.list.TDoubleList
    public int binarySearch(double value, int fromIndex, int toIndex) {
        int binarySearch;
        synchronized (this.mutex) {
            binarySearch = this.list.binarySearch(value, fromIndex, toIndex);
        }
        return binarySearch;
    }

    @Override // gnu.trove.list.TDoubleList
    public TDoubleList grep(TDoubleProcedure condition) {
        TDoubleList grep;
        synchronized (this.mutex) {
            grep = this.list.grep(condition);
        }
        return grep;
    }

    @Override // gnu.trove.list.TDoubleList
    public TDoubleList inverseGrep(TDoubleProcedure condition) {
        TDoubleList inverseGrep;
        synchronized (this.mutex) {
            inverseGrep = this.list.inverseGrep(condition);
        }
        return inverseGrep;
    }

    @Override // gnu.trove.list.TDoubleList
    public double max() {
        double max;
        synchronized (this.mutex) {
            max = this.list.max();
        }
        return max;
    }

    @Override // gnu.trove.list.TDoubleList
    public double min() {
        double min;
        synchronized (this.mutex) {
            min = this.list.min();
        }
        return min;
    }

    @Override // gnu.trove.list.TDoubleList
    public double sum() {
        double sum;
        synchronized (this.mutex) {
            sum = this.list.sum();
        }
        return sum;
    }

    @Override // gnu.trove.list.TDoubleList
    public boolean forEachDescending(TDoubleProcedure procedure) {
        boolean forEachDescending;
        synchronized (this.mutex) {
            forEachDescending = this.list.forEachDescending(procedure);
        }
        return forEachDescending;
    }

    @Override // gnu.trove.list.TDoubleList
    public void transformValues(TDoubleFunction function) {
        synchronized (this.mutex) {
            this.list.transformValues(function);
        }
    }

    private Object readResolve() {
        return this.list instanceof RandomAccess ? new TSynchronizedRandomAccessDoubleList(this.list) : this;
    }
}
