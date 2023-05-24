package gnu.trove.list;

import gnu.trove.TDoubleCollection;
import gnu.trove.function.TDoubleFunction;
import gnu.trove.procedure.TDoubleProcedure;
import java.util.Random;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/list/TDoubleList.class */
public interface TDoubleList extends TDoubleCollection {
    @Override // gnu.trove.TDoubleCollection
    double getNoEntryValue();

    @Override // gnu.trove.TDoubleCollection
    int size();

    @Override // gnu.trove.TDoubleCollection
    boolean isEmpty();

    @Override // gnu.trove.TDoubleCollection
    boolean add(double d);

    void add(double[] dArr);

    void add(double[] dArr, int i, int i2);

    void insert(int i, double d);

    void insert(int i, double[] dArr);

    void insert(int i, double[] dArr, int i2, int i3);

    double get(int i);

    double set(int i, double d);

    void set(int i, double[] dArr);

    void set(int i, double[] dArr, int i2, int i3);

    double replace(int i, double d);

    @Override // gnu.trove.TDoubleCollection
    void clear();

    @Override // gnu.trove.TDoubleCollection
    boolean remove(double d);

    double removeAt(int i);

    void remove(int i, int i2);

    void transformValues(TDoubleFunction tDoubleFunction);

    void reverse();

    void reverse(int i, int i2);

    void shuffle(Random random);

    TDoubleList subList(int i, int i2);

    @Override // gnu.trove.TDoubleCollection
    double[] toArray();

    double[] toArray(int i, int i2);

    @Override // gnu.trove.TDoubleCollection
    double[] toArray(double[] dArr);

    double[] toArray(double[] dArr, int i, int i2);

    double[] toArray(double[] dArr, int i, int i2, int i3);

    @Override // gnu.trove.TDoubleCollection
    boolean forEach(TDoubleProcedure tDoubleProcedure);

    boolean forEachDescending(TDoubleProcedure tDoubleProcedure);

    void sort();

    void sort(int i, int i2);

    void fill(double d);

    void fill(int i, int i2, double d);

    int binarySearch(double d);

    int binarySearch(double d, int i, int i2);

    int indexOf(double d);

    int indexOf(int i, double d);

    int lastIndexOf(double d);

    int lastIndexOf(int i, double d);

    @Override // gnu.trove.TDoubleCollection
    boolean contains(double d);

    TDoubleList grep(TDoubleProcedure tDoubleProcedure);

    TDoubleList inverseGrep(TDoubleProcedure tDoubleProcedure);

    double max();

    double min();

    double sum();
}
