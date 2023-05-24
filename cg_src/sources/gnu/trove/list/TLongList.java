package gnu.trove.list;

import gnu.trove.TLongCollection;
import gnu.trove.function.TLongFunction;
import gnu.trove.procedure.TLongProcedure;
import java.util.Random;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/list/TLongList.class */
public interface TLongList extends TLongCollection {
    @Override // gnu.trove.TLongCollection
    long getNoEntryValue();

    @Override // gnu.trove.TLongCollection
    int size();

    @Override // gnu.trove.TLongCollection
    boolean isEmpty();

    @Override // gnu.trove.TLongCollection
    boolean add(long j);

    void add(long[] jArr);

    void add(long[] jArr, int i, int i2);

    void insert(int i, long j);

    void insert(int i, long[] jArr);

    void insert(int i, long[] jArr, int i2, int i3);

    long get(int i);

    long set(int i, long j);

    void set(int i, long[] jArr);

    void set(int i, long[] jArr, int i2, int i3);

    long replace(int i, long j);

    @Override // gnu.trove.TLongCollection
    void clear();

    @Override // gnu.trove.TLongCollection
    boolean remove(long j);

    long removeAt(int i);

    void remove(int i, int i2);

    void transformValues(TLongFunction tLongFunction);

    void reverse();

    void reverse(int i, int i2);

    void shuffle(Random random);

    TLongList subList(int i, int i2);

    @Override // gnu.trove.TLongCollection
    long[] toArray();

    long[] toArray(int i, int i2);

    @Override // gnu.trove.TLongCollection
    long[] toArray(long[] jArr);

    long[] toArray(long[] jArr, int i, int i2);

    long[] toArray(long[] jArr, int i, int i2, int i3);

    @Override // gnu.trove.TLongCollection
    boolean forEach(TLongProcedure tLongProcedure);

    boolean forEachDescending(TLongProcedure tLongProcedure);

    void sort();

    void sort(int i, int i2);

    void fill(long j);

    void fill(int i, int i2, long j);

    int binarySearch(long j);

    int binarySearch(long j, int i, int i2);

    int indexOf(long j);

    int indexOf(int i, long j);

    int lastIndexOf(long j);

    int lastIndexOf(int i, long j);

    @Override // gnu.trove.TLongCollection
    boolean contains(long j);

    TLongList grep(TLongProcedure tLongProcedure);

    TLongList inverseGrep(TLongProcedure tLongProcedure);

    long max();

    long min();

    long sum();
}
