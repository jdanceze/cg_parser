package gnu.trove.list;

import gnu.trove.TFloatCollection;
import gnu.trove.function.TFloatFunction;
import gnu.trove.procedure.TFloatProcedure;
import java.util.Random;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/list/TFloatList.class */
public interface TFloatList extends TFloatCollection {
    @Override // gnu.trove.TFloatCollection
    float getNoEntryValue();

    @Override // gnu.trove.TFloatCollection
    int size();

    @Override // gnu.trove.TFloatCollection
    boolean isEmpty();

    @Override // gnu.trove.TFloatCollection
    boolean add(float f);

    void add(float[] fArr);

    void add(float[] fArr, int i, int i2);

    void insert(int i, float f);

    void insert(int i, float[] fArr);

    void insert(int i, float[] fArr, int i2, int i3);

    float get(int i);

    float set(int i, float f);

    void set(int i, float[] fArr);

    void set(int i, float[] fArr, int i2, int i3);

    float replace(int i, float f);

    @Override // gnu.trove.TFloatCollection
    void clear();

    @Override // gnu.trove.TFloatCollection
    boolean remove(float f);

    float removeAt(int i);

    void remove(int i, int i2);

    void transformValues(TFloatFunction tFloatFunction);

    void reverse();

    void reverse(int i, int i2);

    void shuffle(Random random);

    TFloatList subList(int i, int i2);

    @Override // gnu.trove.TFloatCollection
    float[] toArray();

    float[] toArray(int i, int i2);

    @Override // gnu.trove.TFloatCollection
    float[] toArray(float[] fArr);

    float[] toArray(float[] fArr, int i, int i2);

    float[] toArray(float[] fArr, int i, int i2, int i3);

    @Override // gnu.trove.TFloatCollection
    boolean forEach(TFloatProcedure tFloatProcedure);

    boolean forEachDescending(TFloatProcedure tFloatProcedure);

    void sort();

    void sort(int i, int i2);

    void fill(float f);

    void fill(int i, int i2, float f);

    int binarySearch(float f);

    int binarySearch(float f, int i, int i2);

    int indexOf(float f);

    int indexOf(int i, float f);

    int lastIndexOf(float f);

    int lastIndexOf(int i, float f);

    @Override // gnu.trove.TFloatCollection
    boolean contains(float f);

    TFloatList grep(TFloatProcedure tFloatProcedure);

    TFloatList inverseGrep(TFloatProcedure tFloatProcedure);

    float max();

    float min();

    float sum();
}
