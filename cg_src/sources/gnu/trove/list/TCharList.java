package gnu.trove.list;

import gnu.trove.TCharCollection;
import gnu.trove.function.TCharFunction;
import gnu.trove.procedure.TCharProcedure;
import java.util.Random;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/list/TCharList.class */
public interface TCharList extends TCharCollection {
    @Override // gnu.trove.TCharCollection
    char getNoEntryValue();

    @Override // gnu.trove.TCharCollection
    int size();

    @Override // gnu.trove.TCharCollection
    boolean isEmpty();

    @Override // gnu.trove.TCharCollection
    boolean add(char c);

    void add(char[] cArr);

    void add(char[] cArr, int i, int i2);

    void insert(int i, char c);

    void insert(int i, char[] cArr);

    void insert(int i, char[] cArr, int i2, int i3);

    char get(int i);

    char set(int i, char c);

    void set(int i, char[] cArr);

    void set(int i, char[] cArr, int i2, int i3);

    char replace(int i, char c);

    @Override // gnu.trove.TCharCollection
    void clear();

    @Override // gnu.trove.TCharCollection
    boolean remove(char c);

    char removeAt(int i);

    void remove(int i, int i2);

    void transformValues(TCharFunction tCharFunction);

    void reverse();

    void reverse(int i, int i2);

    void shuffle(Random random);

    TCharList subList(int i, int i2);

    @Override // gnu.trove.TCharCollection
    char[] toArray();

    char[] toArray(int i, int i2);

    @Override // gnu.trove.TCharCollection
    char[] toArray(char[] cArr);

    char[] toArray(char[] cArr, int i, int i2);

    char[] toArray(char[] cArr, int i, int i2, int i3);

    @Override // gnu.trove.TCharCollection
    boolean forEach(TCharProcedure tCharProcedure);

    boolean forEachDescending(TCharProcedure tCharProcedure);

    void sort();

    void sort(int i, int i2);

    void fill(char c);

    void fill(int i, int i2, char c);

    int binarySearch(char c);

    int binarySearch(char c, int i, int i2);

    int indexOf(char c);

    int indexOf(int i, char c);

    int lastIndexOf(char c);

    int lastIndexOf(int i, char c);

    @Override // gnu.trove.TCharCollection
    boolean contains(char c);

    TCharList grep(TCharProcedure tCharProcedure);

    TCharList inverseGrep(TCharProcedure tCharProcedure);

    char max();

    char min();

    char sum();
}
