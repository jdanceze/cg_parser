package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
@GwtCompatible(serializable = true)
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/CompoundOrdering.class */
final class CompoundOrdering<T> extends Ordering<T> implements Serializable {
    final Comparator<? super T>[] comparators;
    private static final long serialVersionUID = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public CompoundOrdering(Comparator<? super T> primary, Comparator<? super T> secondary) {
        this.comparators = new Comparator[]{primary, secondary};
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CompoundOrdering(Iterable<? extends Comparator<? super T>> comparators) {
        this.comparators = (Comparator[]) Iterables.toArray(comparators, new Comparator[0]);
    }

    @Override // com.google.common.collect.Ordering, java.util.Comparator
    public int compare(T left, T right) {
        for (int i = 0; i < this.comparators.length; i++) {
            int result = this.comparators[i].compare(left, right);
            if (result != 0) {
                return result;
            }
        }
        return 0;
    }

    @Override // java.util.Comparator
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof CompoundOrdering) {
            CompoundOrdering<?> that = (CompoundOrdering) object;
            return Arrays.equals(this.comparators, that.comparators);
        }
        return false;
    }

    public int hashCode() {
        return Arrays.hashCode(this.comparators);
    }

    public String toString() {
        return "Ordering.compound(" + Arrays.toString(this.comparators) + ")";
    }
}
