package gnu.trove;

import gnu.trove.iterator.TShortIterator;
import gnu.trove.procedure.TShortProcedure;
import java.util.Collection;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/TShortCollection.class */
public interface TShortCollection {
    public static final long serialVersionUID = 1;

    short getNoEntryValue();

    int size();

    boolean isEmpty();

    boolean contains(short s);

    TShortIterator iterator();

    short[] toArray();

    short[] toArray(short[] sArr);

    boolean add(short s);

    boolean remove(short s);

    boolean containsAll(Collection<?> collection);

    boolean containsAll(TShortCollection tShortCollection);

    boolean containsAll(short[] sArr);

    boolean addAll(Collection<? extends Short> collection);

    boolean addAll(TShortCollection tShortCollection);

    boolean addAll(short[] sArr);

    boolean retainAll(Collection<?> collection);

    boolean retainAll(TShortCollection tShortCollection);

    boolean retainAll(short[] sArr);

    boolean removeAll(Collection<?> collection);

    boolean removeAll(TShortCollection tShortCollection);

    boolean removeAll(short[] sArr);

    void clear();

    boolean forEach(TShortProcedure tShortProcedure);

    boolean equals(Object obj);

    int hashCode();
}
