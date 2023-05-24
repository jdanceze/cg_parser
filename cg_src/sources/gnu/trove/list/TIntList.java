package gnu.trove.list;

import gnu.trove.TIntCollection;
import gnu.trove.function.TIntFunction;
import gnu.trove.procedure.TIntProcedure;
import java.util.Random;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/list/TIntList.class */
public interface TIntList extends TIntCollection {
    @Override // gnu.trove.TIntCollection
    int getNoEntryValue();

    @Override // gnu.trove.TIntCollection
    int size();

    @Override // gnu.trove.TIntCollection
    boolean isEmpty();

    @Override // gnu.trove.TIntCollection
    boolean add(int i);

    void add(int[] iArr);

    void add(int[] iArr, int i, int i2);

    void insert(int i, int i2);

    void insert(int i, int[] iArr);

    void insert(int i, int[] iArr, int i2, int i3);

    int get(int i);

    int set(int i, int i2);

    void set(int i, int[] iArr);

    void set(int i, int[] iArr, int i2, int i3);

    int replace(int i, int i2);

    @Override // gnu.trove.TIntCollection
    void clear();

    @Override // gnu.trove.TIntCollection
    boolean remove(int i);

    int removeAt(int i);

    void remove(int i, int i2);

    void transformValues(TIntFunction tIntFunction);

    void reverse();

    void reverse(int i, int i2);

    void shuffle(Random random);

    TIntList subList(int i, int i2);

    @Override // gnu.trove.TIntCollection
    int[] toArray();

    int[] toArray(int i, int i2);

    @Override // gnu.trove.TIntCollection
    int[] toArray(int[] iArr);

    int[] toArray(int[] iArr, int i, int i2);

    int[] toArray(int[] iArr, int i, int i2, int i3);

    @Override // gnu.trove.TIntCollection
    boolean forEach(TIntProcedure tIntProcedure);

    boolean forEachDescending(TIntProcedure tIntProcedure);

    void sort();

    void sort(int i, int i2);

    void fill(int i);

    void fill(int i, int i2, int i3);

    int binarySearch(int i);

    int binarySearch(int i, int i2, int i3);

    int indexOf(int i);

    int indexOf(int i, int i2);

    int lastIndexOf(int i);

    int lastIndexOf(int i, int i2);

    @Override // gnu.trove.TIntCollection
    boolean contains(int i);

    TIntList grep(TIntProcedure tIntProcedure);

    TIntList inverseGrep(TIntProcedure tIntProcedure);

    int max();

    int min();

    int sum();
}
