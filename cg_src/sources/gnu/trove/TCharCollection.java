package gnu.trove;

import gnu.trove.iterator.TCharIterator;
import gnu.trove.procedure.TCharProcedure;
import java.util.Collection;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/TCharCollection.class */
public interface TCharCollection {
    public static final long serialVersionUID = 1;

    char getNoEntryValue();

    int size();

    boolean isEmpty();

    boolean contains(char c);

    TCharIterator iterator();

    char[] toArray();

    char[] toArray(char[] cArr);

    boolean add(char c);

    boolean remove(char c);

    boolean containsAll(Collection<?> collection);

    boolean containsAll(TCharCollection tCharCollection);

    boolean containsAll(char[] cArr);

    boolean addAll(Collection<? extends Character> collection);

    boolean addAll(TCharCollection tCharCollection);

    boolean addAll(char[] cArr);

    boolean retainAll(Collection<?> collection);

    boolean retainAll(TCharCollection tCharCollection);

    boolean retainAll(char[] cArr);

    boolean removeAll(Collection<?> collection);

    boolean removeAll(TCharCollection tCharCollection);

    boolean removeAll(char[] cArr);

    void clear();

    boolean forEach(TCharProcedure tCharProcedure);

    boolean equals(Object obj);

    int hashCode();
}
