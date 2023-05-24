package gnu.trove.impl.unmodifiable;

import gnu.trove.function.TDoubleFunction;
import gnu.trove.list.TDoubleList;
import gnu.trove.procedure.TDoubleProcedure;
import java.util.Random;
import java.util.RandomAccess;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/impl/unmodifiable/TUnmodifiableDoubleList.class */
public class TUnmodifiableDoubleList extends TUnmodifiableDoubleCollection implements TDoubleList {
    static final long serialVersionUID = -283967356065247728L;
    final TDoubleList list;

    public TUnmodifiableDoubleList(TDoubleList list) {
        super(list);
        this.list = list;
    }

    @Override // gnu.trove.TDoubleCollection
    public boolean equals(Object o) {
        return o == this || this.list.equals(o);
    }

    @Override // gnu.trove.TDoubleCollection
    public int hashCode() {
        return this.list.hashCode();
    }

    @Override // gnu.trove.list.TDoubleList
    public double get(int index) {
        return this.list.get(index);
    }

    @Override // gnu.trove.list.TDoubleList
    public int indexOf(double o) {
        return this.list.indexOf(o);
    }

    @Override // gnu.trove.list.TDoubleList
    public int lastIndexOf(double o) {
        return this.list.lastIndexOf(o);
    }

    @Override // gnu.trove.list.TDoubleList
    public double[] toArray(int offset, int len) {
        return this.list.toArray(offset, len);
    }

    @Override // gnu.trove.list.TDoubleList
    public double[] toArray(double[] dest, int offset, int len) {
        return this.list.toArray(dest, offset, len);
    }

    @Override // gnu.trove.list.TDoubleList
    public double[] toArray(double[] dest, int source_pos, int dest_pos, int len) {
        return this.list.toArray(dest, source_pos, dest_pos, len);
    }

    @Override // gnu.trove.list.TDoubleList
    public boolean forEachDescending(TDoubleProcedure procedure) {
        return this.list.forEachDescending(procedure);
    }

    @Override // gnu.trove.list.TDoubleList
    public int binarySearch(double value) {
        return this.list.binarySearch(value);
    }

    @Override // gnu.trove.list.TDoubleList
    public int binarySearch(double value, int fromIndex, int toIndex) {
        return this.list.binarySearch(value, fromIndex, toIndex);
    }

    @Override // gnu.trove.list.TDoubleList
    public int indexOf(int offset, double value) {
        return this.list.indexOf(offset, value);
    }

    @Override // gnu.trove.list.TDoubleList
    public int lastIndexOf(int offset, double value) {
        return this.list.lastIndexOf(offset, value);
    }

    @Override // gnu.trove.list.TDoubleList
    public TDoubleList grep(TDoubleProcedure condition) {
        return this.list.grep(condition);
    }

    @Override // gnu.trove.list.TDoubleList
    public TDoubleList inverseGrep(TDoubleProcedure condition) {
        return this.list.inverseGrep(condition);
    }

    @Override // gnu.trove.list.TDoubleList
    public double max() {
        return this.list.max();
    }

    @Override // gnu.trove.list.TDoubleList
    public double min() {
        return this.list.min();
    }

    @Override // gnu.trove.list.TDoubleList
    public double sum() {
        return this.list.sum();
    }

    @Override // gnu.trove.list.TDoubleList
    public TDoubleList subList(int fromIndex, int toIndex) {
        return new TUnmodifiableDoubleList(this.list.subList(fromIndex, toIndex));
    }

    private Object readResolve() {
        return this.list instanceof RandomAccess ? new TUnmodifiableRandomAccessDoubleList(this.list) : this;
    }

    @Override // gnu.trove.list.TDoubleList
    public void add(double[] vals) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TDoubleList
    public void add(double[] vals, int offset, int length) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TDoubleList
    public double removeAt(int offset) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TDoubleList
    public void remove(int offset, int length) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TDoubleList
    public void insert(int offset, double value) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TDoubleList
    public void insert(int offset, double[] values) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TDoubleList
    public void insert(int offset, double[] values, int valOffset, int len) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TDoubleList
    public double set(int offset, double val) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TDoubleList
    public void set(int offset, double[] values) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TDoubleList
    public void set(int offset, double[] values, int valOffset, int length) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TDoubleList
    public double replace(int offset, double val) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TDoubleList
    public void transformValues(TDoubleFunction function) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TDoubleList
    public void reverse() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TDoubleList
    public void reverse(int from, int to) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TDoubleList
    public void shuffle(Random rand) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TDoubleList
    public void sort() {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TDoubleList
    public void sort(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TDoubleList
    public void fill(double val) {
        throw new UnsupportedOperationException();
    }

    @Override // gnu.trove.list.TDoubleList
    public void fill(int fromIndex, int toIndex, double val) {
        throw new UnsupportedOperationException();
    }
}
