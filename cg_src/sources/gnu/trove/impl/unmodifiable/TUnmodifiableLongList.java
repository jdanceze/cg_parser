package gnu.trove.impl.unmodifiable;

import gnu.trove.function.TLongFunction;
import gnu.trove.list.TLongList;
import gnu.trove.procedure.TLongProcedure;
import java.util.Random;
import java.util.RandomAccess;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableLongList.class */
public class TUnmodifiableLongList extends TUnmodifiableLongCollection implements TLongList {
    static final long serialVersionUID = -283967356065247728L;
    final TLongList list;

    public TUnmodifiableLongList(TLongList list) {
        super(list);
        this.list = list;
    }

    @Override // gnu.trove.TLongCollection
    public boolean equals(Object o) {
        return o == this || this.list.equals(o);
    }

    @Override // gnu.trove.TLongCollection
    public int hashCode() {
        return this.list.hashCode();
    }

    @Override // gnu.trove.list.TLongList
    public long get(int index) {
        return this.list.get(index);
    }

    @Override // gnu.trove.list.TLongList
    public int indexOf(long o) {
        return this.list.indexOf(o);
    }

    @Override // gnu.trove.list.TLongList
    public int lastIndexOf(long o) {
        return this.list.lastIndexOf(o);
    }

    @Override // gnu.trove.list.TLongList
    public long[] toArray(int offset, int len) {
        return this.list.toArray(offset, len);
    }

    @Override // gnu.trove.list.TLongList
    public long[] toArray(long[] dest, int offset, int len) {
        return this.list.toArray(dest, offset, len);
    }

    @Override // gnu.trove.list.TLongList
    public long[] toArray(long[] dest, int source_pos, int dest_pos, int len) {
        return this.list.toArray(dest, source_pos, dest_pos, len);
    }

    @Override // gnu.trove.list.TLongList
    public boolean forEachDescending(TLongProcedure procedure) {
        return this.list.forEachDescending(procedure);
    }

    @Override // gnu.trove.list.TLongList
    public int binarySearch(long value) {
        return this.list.binarySearch(value);
    }

    @Override // gnu.trove.list.TLongList
    public int binarySearch(long value, int fromIndex, int toIndex) {
        return this.list.binarySearch(value, fromIndex, toIndex);
    }

    @Override // gnu.trove.list.TLongList
    public int indexOf(int offset, long value) {
        return this.list.indexOf(offset, value);
    }

    @Override // gnu.trove.list.TLongList
    public int lastIndexOf(int offset, long value) {
        return this.list.lastIndexOf(offset, value);
    }

    @Override // gnu.trove.list.TLongList
    public TLongList grep(TLongProcedure condition) {
        return this.list.grep(condition);
    }

    @Override // gnu.trove.list.TLongList
    public TLongList inverseGrep(TLongProcedure condition) {
        return this.list.inverseGrep(condition);
    }

    @Override // gnu.trove.list.TLongList
    public long max() {
        return this.list.max();
    }

    @Override // gnu.trove.list.TLongList
    public long min() {
        return this.list.min();
    }

    @Override // gnu.trove.list.TLongList
    public long sum() {
        return this.list.sum();
    }

    @Override // gnu.trove.list.TLongList
    public TLongList subList(int fromIndex, int toIndex) {
        return new TUnmodifiableLongList(this.list.subList(fromIndex, toIndex));
    }

    private Object readResolve() {
        return this.list instanceof RandomAccess ? new TUnmodifiableRandomAccessLongList(this.list) : this;
    }

    @Override // gnu.trove.list.TLongList
    public void add(long[] vals) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TLongList
    public void add(long[] vals, int offset, int length) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TLongList
    public long removeAt(int offset) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TLongList
    public void remove(int offset, int length) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TLongList
    public void insert(int offset, long value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TLongList
    public void insert(int offset, long[] values) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TLongList
    public void insert(int offset, long[] values, int valOffset, int len) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TLongList
    public long set(int offset, long val) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TLongList
    public void set(int offset, long[] values) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TLongList
    public void set(int offset, long[] values, int valOffset, int length) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TLongList
    public long replace(int offset, long val) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TLongList
    public void transformValues(TLongFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TLongList
    public void reverse() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TLongList
    public void reverse(int from, int to) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TLongList
    public void shuffle(Random rand) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TLongList
    public void sort() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TLongList
    public void sort(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TLongList
    public void fill(long val) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TLongList
    public void fill(int fromIndex, int toIndex, long val) {
        throw new UnsupportedOperationException();
    }
}
