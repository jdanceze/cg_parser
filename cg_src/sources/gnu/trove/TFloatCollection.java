package gnu.trove;

import gnu.trove.iterator.TFloatIterator;
import gnu.trove.procedure.TFloatProcedure;
import java.util.Collection;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/TFloatCollection.class */
public interface TFloatCollection {
    public static final long serialVersionUID = 1;

    float getNoEntryValue();

    int size();

    boolean isEmpty();

    boolean contains(float f);

    TFloatIterator iterator();

    float[] toArray();

    float[] toArray(float[] fArr);

    boolean add(float f);

    boolean remove(float f);

    boolean containsAll(Collection<?> collection);

    boolean containsAll(TFloatCollection tFloatCollection);

    boolean containsAll(float[] fArr);

    boolean addAll(Collection<? extends Float> collection);

    boolean addAll(TFloatCollection tFloatCollection);

    boolean addAll(float[] fArr);

    boolean retainAll(Collection<?> collection);

    boolean retainAll(TFloatCollection tFloatCollection);

    boolean retainAll(float[] fArr);

    boolean removeAll(Collection<?> collection);

    boolean removeAll(TFloatCollection tFloatCollection);

    boolean removeAll(float[] fArr);

    void clear();

    boolean forEach(TFloatProcedure tFloatProcedure);

    boolean equals(Object obj);

    int hashCode();
}
