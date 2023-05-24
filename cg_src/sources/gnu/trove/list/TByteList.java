package gnu.trove.list;

import gnu.trove.TByteCollection;
import gnu.trove.function.TByteFunction;
import gnu.trove.procedure.TByteProcedure;
import java.util.Random;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/list/TByteList.class */
public interface TByteList extends TByteCollection {
    @Override // gnu.trove.TByteCollection
    byte getNoEntryValue();

    @Override // gnu.trove.TByteCollection
    int size();

    @Override // gnu.trove.TByteCollection
    boolean isEmpty();

    @Override // gnu.trove.TByteCollection
    boolean add(byte b);

    void add(byte[] bArr);

    void add(byte[] bArr, int i, int i2);

    void insert(int i, byte b);

    void insert(int i, byte[] bArr);

    void insert(int i, byte[] bArr, int i2, int i3);

    byte get(int i);

    byte set(int i, byte b);

    void set(int i, byte[] bArr);

    void set(int i, byte[] bArr, int i2, int i3);

    byte replace(int i, byte b);

    @Override // gnu.trove.TByteCollection
    void clear();

    @Override // gnu.trove.TByteCollection
    boolean remove(byte b);

    byte removeAt(int i);

    void remove(int i, int i2);

    void transformValues(TByteFunction tByteFunction);

    void reverse();

    void reverse(int i, int i2);

    void shuffle(Random random);

    TByteList subList(int i, int i2);

    @Override // gnu.trove.TByteCollection
    byte[] toArray();

    byte[] toArray(int i, int i2);

    @Override // gnu.trove.TByteCollection
    byte[] toArray(byte[] bArr);

    byte[] toArray(byte[] bArr, int i, int i2);

    byte[] toArray(byte[] bArr, int i, int i2, int i3);

    @Override // gnu.trove.TByteCollection
    boolean forEach(TByteProcedure tByteProcedure);

    boolean forEachDescending(TByteProcedure tByteProcedure);

    void sort();

    void sort(int i, int i2);

    void fill(byte b);

    void fill(int i, int i2, byte b);

    int binarySearch(byte b);

    int binarySearch(byte b, int i, int i2);

    int indexOf(byte b);

    int indexOf(int i, byte b);

    int lastIndexOf(byte b);

    int lastIndexOf(int i, byte b);

    @Override // gnu.trove.TByteCollection
    boolean contains(byte b);

    TByteList grep(TByteProcedure tByteProcedure);

    TByteList inverseGrep(TByteProcedure tByteProcedure);

    byte max();

    byte min();

    byte sum();
}
