package gnu.trove;

import gnu.trove.iterator.TLongIterator;
import gnu.trove.procedure.TLongProcedure;
import java.util.Collection;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/TLongCollection.class */
public interface TLongCollection {
    public static final long serialVersionUID = 1;

    long getNoEntryValue();

    int size();

    boolean isEmpty();

    boolean contains(long j);

    TLongIterator iterator();

    long[] toArray();

    long[] toArray(long[] jArr);

    boolean add(long j);

    boolean remove(long j);

    boolean containsAll(Collection<?> collection);

    boolean containsAll(TLongCollection tLongCollection);

    boolean containsAll(long[] jArr);

    boolean addAll(Collection<? extends Long> collection);

    boolean addAll(TLongCollection tLongCollection);

    boolean addAll(long[] jArr);

    boolean retainAll(Collection<?> collection);

    boolean retainAll(TLongCollection tLongCollection);

    boolean retainAll(long[] jArr);

    boolean removeAll(Collection<?> collection);

    boolean removeAll(TLongCollection tLongCollection);

    boolean removeAll(long[] jArr);

    void clear();

    boolean forEach(TLongProcedure tLongProcedure);

    boolean equals(Object obj);

    int hashCode();
}
