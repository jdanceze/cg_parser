package gnu.trove;

import gnu.trove.iterator.TDoubleIterator;
import gnu.trove.procedure.TDoubleProcedure;
import java.util.Collection;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/TDoubleCollection.class */
public interface TDoubleCollection {
    public static final long serialVersionUID = 1;

    double getNoEntryValue();

    int size();

    boolean isEmpty();

    boolean contains(double d);

    TDoubleIterator iterator();

    double[] toArray();

    double[] toArray(double[] dArr);

    boolean add(double d);

    boolean remove(double d);

    boolean containsAll(Collection<?> collection);

    boolean containsAll(TDoubleCollection tDoubleCollection);

    boolean containsAll(double[] dArr);

    boolean addAll(Collection<? extends Double> collection);

    boolean addAll(TDoubleCollection tDoubleCollection);

    boolean addAll(double[] dArr);

    boolean retainAll(Collection<?> collection);

    boolean retainAll(TDoubleCollection tDoubleCollection);

    boolean retainAll(double[] dArr);

    boolean removeAll(Collection<?> collection);

    boolean removeAll(TDoubleCollection tDoubleCollection);

    boolean removeAll(double[] dArr);

    void clear();

    boolean forEach(TDoubleProcedure tDoubleProcedure);

    boolean equals(Object obj);

    int hashCode();
}
