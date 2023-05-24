package gnu.trove.impl.sync;

import gnu.trove.function.TFloatFunction;
import gnu.trove.list.TFloatList;
import gnu.trove.procedure.TFloatProcedure;
import java.util.Random;
import java.util.RandomAccess;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/sync/TSynchronizedFloatList.class */
public class TSynchronizedFloatList extends TSynchronizedFloatCollection implements TFloatList {
    static final long serialVersionUID = -7754090372962971524L;
    final TFloatList list;

    public TSynchronizedFloatList(TFloatList list) {
        super(list);
        this.list = list;
    }

    public TSynchronizedFloatList(TFloatList list, Object mutex) {
        super(list, mutex);
        this.list = list;
    }

    @Override // gnu.trove.TFloatCollection
    public boolean equals(Object o) {
        boolean equals;
        synchronized (this.mutex) {
            equals = this.list.equals(o);
        }
        return equals;
    }

    @Override // gnu.trove.TFloatCollection
    public int hashCode() {
        int hashCode;
        synchronized (this.mutex) {
            hashCode = this.list.hashCode();
        }
        return hashCode;
    }

    @Override // gnu.trove.list.TFloatList
    public float get(int index) {
        float f;
        synchronized (this.mutex) {
            f = this.list.get(index);
        }
        return f;
    }

    @Override // gnu.trove.list.TFloatList
    public float set(int index, float element) {
        float f;
        synchronized (this.mutex) {
            f = this.list.set(index, element);
        }
        return f;
    }

    @Override // gnu.trove.list.TFloatList
    public void set(int offset, float[] values) {
        synchronized (this.mutex) {
            this.list.set(offset, values);
        }
    }

    @Override // gnu.trove.list.TFloatList
    public void set(int offset, float[] values, int valOffset, int length) {
        synchronized (this.mutex) {
            this.list.set(offset, values, valOffset, length);
        }
    }

    @Override // gnu.trove.list.TFloatList
    public float replace(int offset, float val) {
        float replace;
        synchronized (this.mutex) {
            replace = this.list.replace(offset, val);
        }
        return replace;
    }

    @Override // gnu.trove.list.TFloatList
    public void remove(int offset, int length) {
        synchronized (this.mutex) {
            this.list.remove(offset, length);
        }
    }

    @Override // gnu.trove.list.TFloatList
    public float removeAt(int offset) {
        float removeAt;
        synchronized (this.mutex) {
            removeAt = this.list.removeAt(offset);
        }
        return removeAt;
    }

    @Override // gnu.trove.list.TFloatList
    public void add(float[] vals) {
        synchronized (this.mutex) {
            this.list.add(vals);
        }
    }

    @Override // gnu.trove.list.TFloatList
    public void add(float[] vals, int offset, int length) {
        synchronized (this.mutex) {
            this.list.add(vals, offset, length);
        }
    }

    @Override // gnu.trove.list.TFloatList
    public void insert(int offset, float value) {
        synchronized (this.mutex) {
            this.list.insert(offset, value);
        }
    }

    @Override // gnu.trove.list.TFloatList
    public void insert(int offset, float[] values) {
        synchronized (this.mutex) {
            this.list.insert(offset, values);
        }
    }

    @Override // gnu.trove.list.TFloatList
    public void insert(int offset, float[] values, int valOffset, int len) {
        synchronized (this.mutex) {
            this.list.insert(offset, values, valOffset, len);
        }
    }

    @Override // gnu.trove.list.TFloatList
    public int indexOf(float o) {
        int indexOf;
        synchronized (this.mutex) {
            indexOf = this.list.indexOf(o);
        }
        return indexOf;
    }

    @Override // gnu.trove.list.TFloatList
    public int lastIndexOf(float o) {
        int lastIndexOf;
        synchronized (this.mutex) {
            lastIndexOf = this.list.lastIndexOf(o);
        }
        return lastIndexOf;
    }

    @Override // gnu.trove.list.TFloatList
    public TFloatList subList(int fromIndex, int toIndex) {
        TSynchronizedFloatList tSynchronizedFloatList;
        synchronized (this.mutex) {
            tSynchronizedFloatList = new TSynchronizedFloatList(this.list.subList(fromIndex, toIndex), this.mutex);
        }
        return tSynchronizedFloatList;
    }

    @Override // gnu.trove.list.TFloatList
    public float[] toArray(int offset, int len) {
        float[] array;
        synchronized (this.mutex) {
            array = this.list.toArray(offset, len);
        }
        return array;
    }

    @Override // gnu.trove.list.TFloatList
    public float[] toArray(float[] dest, int offset, int len) {
        float[] array;
        synchronized (this.mutex) {
            array = this.list.toArray(dest, offset, len);
        }
        return array;
    }

    @Override // gnu.trove.list.TFloatList
    public float[] toArray(float[] dest, int source_pos, int dest_pos, int len) {
        float[] array;
        synchronized (this.mutex) {
            array = this.list.toArray(dest, source_pos, dest_pos, len);
        }
        return array;
    }

    @Override // gnu.trove.list.TFloatList
    public int indexOf(int offset, float value) {
        int indexOf;
        synchronized (this.mutex) {
            indexOf = this.list.indexOf(offset, value);
        }
        return indexOf;
    }

    @Override // gnu.trove.list.TFloatList
    public int lastIndexOf(int offset, float value) {
        int lastIndexOf;
        synchronized (this.mutex) {
            lastIndexOf = this.list.lastIndexOf(offset, value);
        }
        return lastIndexOf;
    }

    @Override // gnu.trove.list.TFloatList
    public void fill(float val) {
        synchronized (this.mutex) {
            this.list.fill(val);
        }
    }

    @Override // gnu.trove.list.TFloatList
    public void fill(int fromIndex, int toIndex, float val) {
        synchronized (this.mutex) {
            this.list.fill(fromIndex, toIndex, val);
        }
    }

    @Override // gnu.trove.list.TFloatList
    public void reverse() {
        synchronized (this.mutex) {
            this.list.reverse();
        }
    }

    @Override // gnu.trove.list.TFloatList
    public void reverse(int from, int to) {
        synchronized (this.mutex) {
            this.list.reverse(from, to);
        }
    }

    @Override // gnu.trove.list.TFloatList
    public void shuffle(Random rand) {
        synchronized (this.mutex) {
            this.list.shuffle(rand);
        }
    }

    @Override // gnu.trove.list.TFloatList
    public void sort() {
        synchronized (this.mutex) {
            this.list.sort();
        }
    }

    @Override // gnu.trove.list.TFloatList
    public void sort(int fromIndex, int toIndex) {
        synchronized (this.mutex) {
            this.list.sort(fromIndex, toIndex);
        }
    }

    @Override // gnu.trove.list.TFloatList
    public int binarySearch(float value) {
        int binarySearch;
        synchronized (this.mutex) {
            binarySearch = this.list.binarySearch(value);
        }
        return binarySearch;
    }

    @Override // gnu.trove.list.TFloatList
    public int binarySearch(float value, int fromIndex, int toIndex) {
        int binarySearch;
        synchronized (this.mutex) {
            binarySearch = this.list.binarySearch(value, fromIndex, toIndex);
        }
        return binarySearch;
    }

    @Override // gnu.trove.list.TFloatList
    public TFloatList grep(TFloatProcedure condition) {
        TFloatList grep;
        synchronized (this.mutex) {
            grep = this.list.grep(condition);
        }
        return grep;
    }

    @Override // gnu.trove.list.TFloatList
    public TFloatList inverseGrep(TFloatProcedure condition) {
        TFloatList inverseGrep;
        synchronized (this.mutex) {
            inverseGrep = this.list.inverseGrep(condition);
        }
        return inverseGrep;
    }

    @Override // gnu.trove.list.TFloatList
    public float max() {
        float max;
        synchronized (this.mutex) {
            max = this.list.max();
        }
        return max;
    }

    @Override // gnu.trove.list.TFloatList
    public float min() {
        float min;
        synchronized (this.mutex) {
            min = this.list.min();
        }
        return min;
    }

    @Override // gnu.trove.list.TFloatList
    public float sum() {
        float sum;
        synchronized (this.mutex) {
            sum = this.list.sum();
        }
        return sum;
    }

    @Override // gnu.trove.list.TFloatList
    public boolean forEachDescending(TFloatProcedure procedure) {
        boolean forEachDescending;
        synchronized (this.mutex) {
            forEachDescending = this.list.forEachDescending(procedure);
        }
        return forEachDescending;
    }

    @Override // gnu.trove.list.TFloatList
    public void transformValues(TFloatFunction function) {
        synchronized (this.mutex) {
            this.list.transformValues(function);
        }
    }

    private Object readResolve() {
        return this.list instanceof RandomAccess ? new TSynchronizedRandomAccessFloatList(this.list) : this;
    }
}
