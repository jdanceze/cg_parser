package gnu.trove.impl.unmodifiable;

import gnu.trove.function.TShortFunction;
import gnu.trove.list.TShortList;
import gnu.trove.procedure.TShortProcedure;
import java.util.Random;
import java.util.RandomAccess;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableShortList.class */
public class TUnmodifiableShortList extends TUnmodifiableShortCollection implements TShortList {
    static final long serialVersionUID = -283967356065247728L;
    final TShortList list;

    public TUnmodifiableShortList(TShortList list) {
        super(list);
        this.list = list;
    }

    @Override // gnu.trove.TShortCollection
    public boolean equals(Object o) {
        return o == this || this.list.equals(o);
    }

    @Override // gnu.trove.TShortCollection
    public int hashCode() {
        return this.list.hashCode();
    }

    @Override // gnu.trove.list.TShortList
    public short get(int index) {
        return this.list.get(index);
    }

    @Override // gnu.trove.list.TShortList
    public int indexOf(short o) {
        return this.list.indexOf(o);
    }

    @Override // gnu.trove.list.TShortList
    public int lastIndexOf(short o) {
        return this.list.lastIndexOf(o);
    }

    @Override // gnu.trove.list.TShortList
    public short[] toArray(int offset, int len) {
        return this.list.toArray(offset, len);
    }

    @Override // gnu.trove.list.TShortList
    public short[] toArray(short[] dest, int offset, int len) {
        return this.list.toArray(dest, offset, len);
    }

    @Override // gnu.trove.list.TShortList
    public short[] toArray(short[] dest, int source_pos, int dest_pos, int len) {
        return this.list.toArray(dest, source_pos, dest_pos, len);
    }

    @Override // gnu.trove.list.TShortList
    public boolean forEachDescending(TShortProcedure procedure) {
        return this.list.forEachDescending(procedure);
    }

    @Override // gnu.trove.list.TShortList
    public int binarySearch(short value) {
        return this.list.binarySearch(value);
    }

    @Override // gnu.trove.list.TShortList
    public int binarySearch(short value, int fromIndex, int toIndex) {
        return this.list.binarySearch(value, fromIndex, toIndex);
    }

    @Override // gnu.trove.list.TShortList
    public int indexOf(int offset, short value) {
        return this.list.indexOf(offset, value);
    }

    @Override // gnu.trove.list.TShortList
    public int lastIndexOf(int offset, short value) {
        return this.list.lastIndexOf(offset, value);
    }

    @Override // gnu.trove.list.TShortList
    public TShortList grep(TShortProcedure condition) {
        return this.list.grep(condition);
    }

    @Override // gnu.trove.list.TShortList
    public TShortList inverseGrep(TShortProcedure condition) {
        return this.list.inverseGrep(condition);
    }

    @Override // gnu.trove.list.TShortList
    public short max() {
        return this.list.max();
    }

    @Override // gnu.trove.list.TShortList
    public short min() {
        return this.list.min();
    }

    @Override // gnu.trove.list.TShortList
    public short sum() {
        return this.list.sum();
    }

    public TShortList subList(int fromIndex, int toIndex) {
        return new TUnmodifiableShortList(this.list.subList(fromIndex, toIndex));
    }

    private Object readResolve() {
        return this.list instanceof RandomAccess ? new TUnmodifiableRandomAccessShortList(this.list) : this;
    }

    @Override // gnu.trove.list.TShortList
    public void add(short[] vals) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TShortList
    public void add(short[] vals, int offset, int length) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TShortList
    public short removeAt(int offset) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TShortList
    public void remove(int offset, int length) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TShortList
    public void insert(int offset, short value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TShortList
    public void insert(int offset, short[] values) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TShortList
    public void insert(int offset, short[] values, int valOffset, int len) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TShortList
    public short set(int offset, short val) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TShortList
    public void set(int offset, short[] values) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TShortList
    public void set(int offset, short[] values, int valOffset, int length) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TShortList
    public short replace(int offset, short val) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TShortList
    public void transformValues(TShortFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TShortList
    public void reverse() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TShortList
    public void reverse(int from, int to) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TShortList
    public void shuffle(Random rand) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TShortList
    public void sort() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TShortList
    public void sort(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TShortList
    public void fill(short val) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TShortList
    public void fill(int fromIndex, int toIndex, short val) {
        throw new UnsupportedOperationException();
    }
}
