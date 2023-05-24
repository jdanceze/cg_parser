package gnu.trove.list;

import gnu.trove.TShortCollection;
import gnu.trove.function.TShortFunction;
import gnu.trove.procedure.TShortProcedure;
import java.util.Random;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/list/TShortList.class */
public interface TShortList extends TShortCollection {
    @Override // gnu.trove.TShortCollection
    short getNoEntryValue();

    @Override // gnu.trove.TShortCollection
    int size();

    @Override // gnu.trove.TShortCollection
    boolean isEmpty();

    @Override // gnu.trove.TShortCollection
    boolean add(short s);

    void add(short[] sArr);

    void add(short[] sArr, int i, int i2);

    void insert(int i, short s);

    void insert(int i, short[] sArr);

    void insert(int i, short[] sArr, int i2, int i3);

    short get(int i);

    short set(int i, short s);

    void set(int i, short[] sArr);

    void set(int i, short[] sArr, int i2, int i3);

    short replace(int i, short s);

    @Override // gnu.trove.TShortCollection
    void clear();

    @Override // gnu.trove.TShortCollection
    boolean remove(short s);

    short removeAt(int i);

    void remove(int i, int i2);

    void transformValues(TShortFunction tShortFunction);

    void reverse();

    void reverse(int i, int i2);

    void shuffle(Random random);

    TShortList subList(int i, int i2);

    @Override // gnu.trove.TShortCollection
    short[] toArray();

    short[] toArray(int i, int i2);

    @Override // gnu.trove.TShortCollection
    short[] toArray(short[] sArr);

    short[] toArray(short[] sArr, int i, int i2);

    short[] toArray(short[] sArr, int i, int i2, int i3);

    @Override // gnu.trove.TShortCollection
    boolean forEach(TShortProcedure tShortProcedure);

    boolean forEachDescending(TShortProcedure tShortProcedure);

    void sort();

    void sort(int i, int i2);

    void fill(short s);

    void fill(int i, int i2, short s);

    int binarySearch(short s);

    int binarySearch(short s, int i, int i2);

    int indexOf(short s);

    int indexOf(int i, short s);

    int lastIndexOf(short s);

    int lastIndexOf(int i, short s);

    @Override // gnu.trove.TShortCollection
    boolean contains(short s);

    TShortList grep(TShortProcedure tShortProcedure);

    TShortList inverseGrep(TShortProcedure tShortProcedure);

    short max();

    short min();

    short sum();
}
