package gnu.trove.impl.unmodifiable;

import gnu.trove.function.TByteFunction;
import gnu.trove.list.TByteList;
import gnu.trove.procedure.TByteProcedure;
import java.util.Random;
import java.util.RandomAccess;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableByteList.class */
public class TUnmodifiableByteList extends TUnmodifiableByteCollection implements TByteList {
    static final long serialVersionUID = -283967356065247728L;
    final TByteList list;

    public TUnmodifiableByteList(TByteList list) {
        super(list);
        this.list = list;
    }

    @Override // gnu.trove.TByteCollection
    public boolean equals(Object o) {
        return o == this || this.list.equals(o);
    }

    @Override // gnu.trove.TByteCollection
    public int hashCode() {
        return this.list.hashCode();
    }

    @Override // gnu.trove.list.TByteList
    public byte get(int index) {
        return this.list.get(index);
    }

    @Override // gnu.trove.list.TByteList
    public int indexOf(byte o) {
        return this.list.indexOf(o);
    }

    @Override // gnu.trove.list.TByteList
    public int lastIndexOf(byte o) {
        return this.list.lastIndexOf(o);
    }

    @Override // gnu.trove.list.TByteList
    public byte[] toArray(int offset, int len) {
        return this.list.toArray(offset, len);
    }

    @Override // gnu.trove.list.TByteList
    public byte[] toArray(byte[] dest, int offset, int len) {
        return this.list.toArray(dest, offset, len);
    }

    @Override // gnu.trove.list.TByteList
    public byte[] toArray(byte[] dest, int source_pos, int dest_pos, int len) {
        return this.list.toArray(dest, source_pos, dest_pos, len);
    }

    @Override // gnu.trove.list.TByteList
    public boolean forEachDescending(TByteProcedure procedure) {
        return this.list.forEachDescending(procedure);
    }

    @Override // gnu.trove.list.TByteList
    public int binarySearch(byte value) {
        return this.list.binarySearch(value);
    }

    @Override // gnu.trove.list.TByteList
    public int binarySearch(byte value, int fromIndex, int toIndex) {
        return this.list.binarySearch(value, fromIndex, toIndex);
    }

    @Override // gnu.trove.list.TByteList
    public int indexOf(int offset, byte value) {
        return this.list.indexOf(offset, value);
    }

    @Override // gnu.trove.list.TByteList
    public int lastIndexOf(int offset, byte value) {
        return this.list.lastIndexOf(offset, value);
    }

    @Override // gnu.trove.list.TByteList
    public TByteList grep(TByteProcedure condition) {
        return this.list.grep(condition);
    }

    @Override // gnu.trove.list.TByteList
    public TByteList inverseGrep(TByteProcedure condition) {
        return this.list.inverseGrep(condition);
    }

    @Override // gnu.trove.list.TByteList
    public byte max() {
        return this.list.max();
    }

    @Override // gnu.trove.list.TByteList
    public byte min() {
        return this.list.min();
    }

    @Override // gnu.trove.list.TByteList
    public byte sum() {
        return this.list.sum();
    }

    @Override // gnu.trove.list.TByteList
    public TByteList subList(int fromIndex, int toIndex) {
        return new TUnmodifiableByteList(this.list.subList(fromIndex, toIndex));
    }

    private Object readResolve() {
        return this.list instanceof RandomAccess ? new TUnmodifiableRandomAccessByteList(this.list) : this;
    }

    @Override // gnu.trove.list.TByteList
    public void add(byte[] vals) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TByteList
    public void add(byte[] vals, int offset, int length) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TByteList
    public byte removeAt(int offset) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TByteList
    public void remove(int offset, int length) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TByteList
    public void insert(int offset, byte value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TByteList
    public void insert(int offset, byte[] values) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TByteList
    public void insert(int offset, byte[] values, int valOffset, int len) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TByteList
    public byte set(int offset, byte val) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TByteList
    public void set(int offset, byte[] values) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TByteList
    public void set(int offset, byte[] values, int valOffset, int length) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TByteList
    public byte replace(int offset, byte val) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TByteList
    public void transformValues(TByteFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TByteList
    public void reverse() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TByteList
    public void reverse(int from, int to) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TByteList
    public void shuffle(Random rand) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TByteList
    public void sort() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TByteList
    public void sort(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TByteList
    public void fill(byte val) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TByteList
    public void fill(int fromIndex, int toIndex, byte val) {
        throw new UnsupportedOperationException();
    }
}
