package gnu.trove;

import gnu.trove.iterator.TIntIterator;
import gnu.trove.procedure.TIntProcedure;
import java.util.Collection;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/TIntCollection.class */
public interface TIntCollection {
    public static final long serialVersionUID = 1;

    int getNoEntryValue();

    int size();

    boolean isEmpty();

    boolean contains(int i);

    TIntIterator iterator();

    int[] toArray();

    int[] toArray(int[] iArr);

    boolean add(int i);

    boolean remove(int i);

    boolean containsAll(Collection<?> collection);

    boolean containsAll(TIntCollection tIntCollection);

    boolean containsAll(int[] iArr);

    boolean addAll(Collection<? extends Integer> collection);

    boolean addAll(TIntCollection tIntCollection);

    boolean addAll(int[] iArr);

    boolean retainAll(Collection<?> collection);

    boolean retainAll(TIntCollection tIntCollection);

    boolean retainAll(int[] iArr);

    boolean removeAll(Collection<?> collection);

    boolean removeAll(TIntCollection tIntCollection);

    boolean removeAll(int[] iArr);

    void clear();

    boolean forEach(TIntProcedure tIntProcedure);

    boolean equals(Object obj);

    int hashCode();
}
