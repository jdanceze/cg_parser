package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import java.io.Serializable;
import java.lang.Comparable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
@GwtCompatible
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/Range.class */
public final class Range<C extends Comparable> extends RangeGwtSerializationDependencies implements Predicate<C>, Serializable {
    private static final Range<Comparable> ALL = new Range<>(Cut.belowAll(), Cut.aboveAll());
    final Cut<C> lowerBound;
    final Cut<C> upperBound;
    private static final long serialVersionUID = 0;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.base.Predicate
    @Deprecated
    public /* bridge */ /* synthetic */ boolean apply(Object obj) {
        return apply((Range<C>) ((Comparable) obj));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/Range$LowerBoundFn.class */
    public static class LowerBoundFn implements Function<Range, Cut> {
        static final LowerBoundFn INSTANCE = new LowerBoundFn();

        LowerBoundFn() {
        }

        @Override // com.google.common.base.Function
        public Cut apply(Range range) {
            return range.lowerBound;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/Range$UpperBoundFn.class */
    public static class UpperBoundFn implements Function<Range, Cut> {
        static final UpperBoundFn INSTANCE = new UpperBoundFn();

        UpperBoundFn() {
        }

        @Override // com.google.common.base.Function
        public Cut apply(Range range) {
            return range.upperBound;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <C extends Comparable<?>> Function<Range<C>, Cut<C>> lowerBoundFn() {
        return LowerBoundFn.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <C extends Comparable<?>> Function<Range<C>, Cut<C>> upperBoundFn() {
        return UpperBoundFn.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <C extends Comparable<?>> Ordering<Range<C>> rangeLexOrdering() {
        return (Ordering<Range<C>>) RangeLexOrdering.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <C extends Comparable<?>> Range<C> create(Cut<C> lowerBound, Cut<C> upperBound) {
        return new Range<>(lowerBound, upperBound);
    }

    public static <C extends Comparable<?>> Range<C> open(C lower, C upper) {
        return create(Cut.aboveValue(lower), Cut.belowValue(upper));
    }

    public static <C extends Comparable<?>> Range<C> closed(C lower, C upper) {
        return create(Cut.belowValue(lower), Cut.aboveValue(upper));
    }

    public static <C extends Comparable<?>> Range<C> closedOpen(C lower, C upper) {
        return create(Cut.belowValue(lower), Cut.belowValue(upper));
    }

    public static <C extends Comparable<?>> Range<C> openClosed(C lower, C upper) {
        return create(Cut.aboveValue(lower), Cut.aboveValue(upper));
    }

    public static <C extends Comparable<?>> Range<C> range(C lower, BoundType lowerType, C upper, BoundType upperType) {
        Preconditions.checkNotNull(lowerType);
        Preconditions.checkNotNull(upperType);
        Cut<C> lowerBound = lowerType == BoundType.OPEN ? Cut.aboveValue(lower) : Cut.belowValue(lower);
        Cut<C> upperBound = upperType == BoundType.OPEN ? Cut.belowValue(upper) : Cut.aboveValue(upper);
        return create(lowerBound, upperBound);
    }

    public static <C extends Comparable<?>> Range<C> lessThan(C endpoint) {
        return create(Cut.belowAll(), Cut.belowValue(endpoint));
    }

    public static <C extends Comparable<?>> Range<C> atMost(C endpoint) {
        return create(Cut.belowAll(), Cut.aboveValue(endpoint));
    }

    public static <C extends Comparable<?>> Range<C> upTo(C endpoint, BoundType boundType) {
        switch (boundType) {
            case OPEN:
                return lessThan(endpoint);
            case CLOSED:
                return atMost(endpoint);
            default:
                throw new AssertionError();
        }
    }

    public static <C extends Comparable<?>> Range<C> greaterThan(C endpoint) {
        return create(Cut.aboveValue(endpoint), Cut.aboveAll());
    }

    public static <C extends Comparable<?>> Range<C> atLeast(C endpoint) {
        return create(Cut.belowValue(endpoint), Cut.aboveAll());
    }

    public static <C extends Comparable<?>> Range<C> downTo(C endpoint, BoundType boundType) {
        switch (boundType) {
            case OPEN:
                return greaterThan(endpoint);
            case CLOSED:
                return atLeast(endpoint);
            default:
                throw new AssertionError();
        }
    }

    public static <C extends Comparable<?>> Range<C> all() {
        return (Range<C>) ALL;
    }

    public static <C extends Comparable<?>> Range<C> singleton(C value) {
        return closed(value, value);
    }

    public static <C extends Comparable<?>> Range<C> encloseAll(Iterable<C> values) {
        Preconditions.checkNotNull(values);
        if (values instanceof SortedSet) {
            SortedSet<? extends C> set = cast(values);
            Comparator<?> comparator = set.comparator();
            if (Ordering.natural().equals(comparator) || comparator == null) {
                return closed((Comparable) set.first(), (Comparable) set.last());
            }
        }
        Iterator<C> valueIterator = values.iterator();
        Comparable comparable = (Comparable) Preconditions.checkNotNull(valueIterator.next());
        Comparable comparable2 = comparable;
        while (true) {
            Comparable comparable3 = comparable2;
            if (valueIterator.hasNext()) {
                Comparable comparable4 = (Comparable) Preconditions.checkNotNull(valueIterator.next());
                comparable = (Comparable) Ordering.natural().min(comparable, comparable4);
                comparable2 = (Comparable) Ordering.natural().max(comparable3, comparable4);
            } else {
                return closed(comparable, comparable3);
            }
        }
    }

    private Range(Cut<C> lowerBound, Cut<C> upperBound) {
        this.lowerBound = (Cut) Preconditions.checkNotNull(lowerBound);
        this.upperBound = (Cut) Preconditions.checkNotNull(upperBound);
        if (lowerBound.compareTo((Cut) upperBound) > 0 || lowerBound == Cut.aboveAll() || upperBound == Cut.belowAll()) {
            throw new IllegalArgumentException("Invalid range: " + toString(lowerBound, upperBound));
        }
    }

    public boolean hasLowerBound() {
        return this.lowerBound != Cut.belowAll();
    }

    public C lowerEndpoint() {
        return this.lowerBound.endpoint();
    }

    public BoundType lowerBoundType() {
        return this.lowerBound.typeAsLowerBound();
    }

    public boolean hasUpperBound() {
        return this.upperBound != Cut.aboveAll();
    }

    public C upperEndpoint() {
        return this.upperBound.endpoint();
    }

    public BoundType upperBoundType() {
        return this.upperBound.typeAsUpperBound();
    }

    public boolean isEmpty() {
        return this.lowerBound.equals(this.upperBound);
    }

    public boolean contains(C value) {
        Preconditions.checkNotNull(value);
        return this.lowerBound.isLessThan(value) && !this.upperBound.isLessThan(value);
    }

    @Deprecated
    public boolean apply(C input) {
        return contains(input);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public boolean containsAll(Iterable<? extends C> values) {
        if (Iterables.isEmpty(values)) {
            return true;
        }
        if (values instanceof SortedSet) {
            SortedSet<? extends C> set = cast(values);
            Comparator<?> comparator = set.comparator();
            if (Ordering.natural().equals(comparator) || comparator == null) {
                return contains((Comparable) set.first()) && contains((Comparable) set.last());
            }
        }
        for (C value : values) {
            if (!contains(value)) {
                return false;
            }
        }
        return true;
    }

    public boolean encloses(Range<C> other) {
        return this.lowerBound.compareTo((Cut) other.lowerBound) <= 0 && this.upperBound.compareTo((Cut) other.upperBound) >= 0;
    }

    public boolean isConnected(Range<C> other) {
        return this.lowerBound.compareTo((Cut) other.upperBound) <= 0 && other.lowerBound.compareTo((Cut) this.upperBound) <= 0;
    }

    public Range<C> intersection(Range<C> connectedRange) {
        int lowerCmp = this.lowerBound.compareTo((Cut) connectedRange.lowerBound);
        int upperCmp = this.upperBound.compareTo((Cut) connectedRange.upperBound);
        if (lowerCmp >= 0 && upperCmp <= 0) {
            return this;
        }
        if (lowerCmp <= 0 && upperCmp >= 0) {
            return connectedRange;
        }
        Cut<C> newLower = lowerCmp >= 0 ? this.lowerBound : connectedRange.lowerBound;
        Cut<C> newUpper = upperCmp <= 0 ? this.upperBound : connectedRange.upperBound;
        return create(newLower, newUpper);
    }

    public Range<C> gap(Range<C> otherRange) {
        boolean isThisFirst = this.lowerBound.compareTo((Cut) otherRange.lowerBound) < 0;
        Range<C> firstRange = isThisFirst ? this : otherRange;
        Range<C> secondRange = isThisFirst ? otherRange : this;
        return create(firstRange.upperBound, secondRange.lowerBound);
    }

    public Range<C> span(Range<C> other) {
        int lowerCmp = this.lowerBound.compareTo((Cut) other.lowerBound);
        int upperCmp = this.upperBound.compareTo((Cut) other.upperBound);
        if (lowerCmp <= 0 && upperCmp >= 0) {
            return this;
        }
        if (lowerCmp >= 0 && upperCmp <= 0) {
            return other;
        }
        Cut<C> newLower = lowerCmp <= 0 ? this.lowerBound : other.lowerBound;
        Cut<C> newUpper = upperCmp >= 0 ? this.upperBound : other.upperBound;
        return create(newLower, newUpper);
    }

    public Range<C> canonical(DiscreteDomain<C> domain) {
        Preconditions.checkNotNull(domain);
        Cut<C> lower = this.lowerBound.canonical(domain);
        Cut<C> upper = this.upperBound.canonical(domain);
        return (lower == this.lowerBound && upper == this.upperBound) ? this : create(lower, upper);
    }

    @Override // com.google.common.base.Predicate
    public boolean equals(@NullableDecl Object object) {
        if (object instanceof Range) {
            Range<?> other = (Range) object;
            return this.lowerBound.equals(other.lowerBound) && this.upperBound.equals(other.upperBound);
        }
        return false;
    }

    public int hashCode() {
        return (this.lowerBound.hashCode() * 31) + this.upperBound.hashCode();
    }

    public String toString() {
        return toString(this.lowerBound, this.upperBound);
    }

    private static String toString(Cut<?> lowerBound, Cut<?> upperBound) {
        StringBuilder sb = new StringBuilder(16);
        lowerBound.describeAsLowerBound(sb);
        sb.append("..");
        upperBound.describeAsUpperBound(sb);
        return sb.toString();
    }

    private static <T> SortedSet<T> cast(Iterable<T> iterable) {
        return (SortedSet) iterable;
    }

    Object readResolve() {
        if (equals(ALL)) {
            return all();
        }
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int compareOrThrow(Comparable left, Comparable right) {
        return left.compareTo(right);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/Range$RangeLexOrdering.class */
    public static class RangeLexOrdering extends Ordering<Range<?>> implements Serializable {
        static final Ordering<Range<?>> INSTANCE = new RangeLexOrdering();
        private static final long serialVersionUID = 0;

        private RangeLexOrdering() {
        }

        @Override // com.google.common.collect.Ordering, java.util.Comparator
        public int compare(Range<?> left, Range<?> right) {
            return ComparisonChain.start().compare(left.lowerBound, right.lowerBound).compare(left.upperBound, right.upperBound).result();
        }
    }
}
