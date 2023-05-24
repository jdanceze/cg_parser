package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Comparator;
import java.util.Iterator;
@Beta
@GwtCompatible
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/Comparators.class */
public final class Comparators {
    private Comparators() {
    }

    public static <T, S extends T> Comparator<Iterable<S>> lexicographical(Comparator<T> comparator) {
        return new LexicographicalOrdering((Comparator) Preconditions.checkNotNull(comparator));
    }

    public static <T> boolean isInOrder(Iterable<? extends T> iterable, Comparator<T> comparator) {
        Preconditions.checkNotNull(comparator);
        Iterator<? extends T> it = iterable.iterator();
        if (it.hasNext()) {
            T next = it.next();
            while (true) {
                T prev = next;
                if (it.hasNext()) {
                    T next2 = it.next();
                    if (comparator.compare(prev, next2) > 0) {
                        return false;
                    }
                    next = next2;
                } else {
                    return true;
                }
            }
        } else {
            return true;
        }
    }

    public static <T> boolean isInStrictOrder(Iterable<? extends T> iterable, Comparator<T> comparator) {
        Preconditions.checkNotNull(comparator);
        Iterator<? extends T> it = iterable.iterator();
        if (it.hasNext()) {
            T next = it.next();
            while (true) {
                T prev = next;
                if (it.hasNext()) {
                    T next2 = it.next();
                    if (comparator.compare(prev, next2) >= 0) {
                        return false;
                    }
                    next = next2;
                } else {
                    return true;
                }
            }
        } else {
            return true;
        }
    }
}
