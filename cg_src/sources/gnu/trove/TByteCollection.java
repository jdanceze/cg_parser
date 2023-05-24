package gnu.trove;

import gnu.trove.iterator.TByteIterator;
import gnu.trove.procedure.TByteProcedure;
import java.util.Collection;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/TByteCollection.class */
public interface TByteCollection {
    public static final long serialVersionUID = 1;

    byte getNoEntryValue();

    int size();

    boolean isEmpty();

    boolean contains(byte b);

    TByteIterator iterator();

    byte[] toArray();

    byte[] toArray(byte[] bArr);

    boolean add(byte b);

    boolean remove(byte b);

    boolean containsAll(Collection<?> collection);

    boolean containsAll(TByteCollection tByteCollection);

    boolean containsAll(byte[] bArr);

    boolean addAll(Collection<? extends Byte> collection);

    boolean addAll(TByteCollection tByteCollection);

    boolean addAll(byte[] bArr);

    boolean retainAll(Collection<?> collection);

    boolean retainAll(TByteCollection tByteCollection);

    boolean retainAll(byte[] bArr);

    boolean removeAll(Collection<?> collection);

    boolean removeAll(TByteCollection tByteCollection);

    boolean removeAll(byte[] bArr);

    void clear();

    boolean forEach(TByteProcedure tByteProcedure);

    boolean equals(Object obj);

    int hashCode();
}
