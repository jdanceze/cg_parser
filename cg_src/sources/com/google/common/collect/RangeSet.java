package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import java.lang.Comparable;
import java.util.Set;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
@Beta
@GwtIncompatible
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/RangeSet.class */
public interface RangeSet<C extends Comparable> {
    boolean contains(C c);

    Range<C> rangeContaining(C c);

    boolean intersects(Range<C> range);

    boolean encloses(Range<C> range);

    boolean enclosesAll(RangeSet<C> rangeSet);

    boolean enclosesAll(Iterable<Range<C>> iterable);

    boolean isEmpty();

    Range<C> span();

    Set<Range<C>> asRanges();

    Set<Range<C>> asDescendingSetOfRanges();

    RangeSet<C> complement();

    RangeSet<C> subRangeSet(Range<C> range);

    void add(Range<C> range);

    void remove(Range<C> range);

    void clear();

    void addAll(RangeSet<C> rangeSet);

    void addAll(Iterable<Range<C>> iterable);

    void removeAll(RangeSet<C> rangeSet);

    void removeAll(Iterable<Range<C>> iterable);

    boolean equals(@NullableDecl Object obj);

    int hashCode();

    String toString();
}
