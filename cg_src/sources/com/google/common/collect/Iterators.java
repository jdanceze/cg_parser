package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.primitives.Ints;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Queue;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
@GwtCompatible(emulated = true)
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/Iterators.class */
public final class Iterators {
    private Iterators() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> UnmodifiableIterator<T> emptyIterator() {
        return emptyListIterator();
    }

    static <T> UnmodifiableListIterator<T> emptyListIterator() {
        return (UnmodifiableListIterator<T>) ArrayItr.EMPTY;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/Iterators$EmptyModifiableIterator.class */
    public enum EmptyModifiableIterator implements Iterator<Object> {
        INSTANCE;

        @Override // java.util.Iterator
        public boolean hasNext() {
            return false;
        }

        @Override // java.util.Iterator
        public Object next() {
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            CollectPreconditions.checkRemove(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> Iterator<T> emptyModifiableIterator() {
        return EmptyModifiableIterator.INSTANCE;
    }

    public static <T> UnmodifiableIterator<T> unmodifiableIterator(final Iterator<? extends T> iterator) {
        Preconditions.checkNotNull(iterator);
        if (iterator instanceof UnmodifiableIterator) {
            UnmodifiableIterator<T> result = (UnmodifiableIterator) iterator;
            return result;
        }
        return new UnmodifiableIterator<T>() { // from class: com.google.common.collect.Iterators.1
            @Override // java.util.Iterator
            public boolean hasNext() {
                return iterator.hasNext();
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [T, java.lang.Object] */
            @Override // java.util.Iterator
            public T next() {
                return iterator.next();
            }
        };
    }

    @Deprecated
    public static <T> UnmodifiableIterator<T> unmodifiableIterator(UnmodifiableIterator<T> iterator) {
        return (UnmodifiableIterator) Preconditions.checkNotNull(iterator);
    }

    public static int size(Iterator<?> iterator) {
        long j = 0;
        while (true) {
            long count = j;
            if (iterator.hasNext()) {
                iterator.next();
                j = count + 1;
            } else {
                return Ints.saturatedCast(count);
            }
        }
    }

    public static boolean contains(Iterator<?> iterator, @NullableDecl Object element) {
        if (element == null) {
            while (iterator.hasNext()) {
                if (iterator.next() == null) {
                    return true;
                }
            }
            return false;
        }
        while (iterator.hasNext()) {
            if (element.equals(iterator.next())) {
                return true;
            }
        }
        return false;
    }

    @CanIgnoreReturnValue
    public static boolean removeAll(Iterator<?> removeFrom, Collection<?> elementsToRemove) {
        Preconditions.checkNotNull(elementsToRemove);
        boolean result = false;
        while (removeFrom.hasNext()) {
            if (elementsToRemove.contains(removeFrom.next())) {
                removeFrom.remove();
                result = true;
            }
        }
        return result;
    }

    @CanIgnoreReturnValue
    public static <T> boolean removeIf(Iterator<T> removeFrom, Predicate<? super T> predicate) {
        Preconditions.checkNotNull(predicate);
        boolean modified = false;
        while (removeFrom.hasNext()) {
            if (predicate.apply(removeFrom.next())) {
                removeFrom.remove();
                modified = true;
            }
        }
        return modified;
    }

    @CanIgnoreReturnValue
    public static boolean retainAll(Iterator<?> removeFrom, Collection<?> elementsToRetain) {
        Preconditions.checkNotNull(elementsToRetain);
        boolean result = false;
        while (removeFrom.hasNext()) {
            if (!elementsToRetain.contains(removeFrom.next())) {
                removeFrom.remove();
                result = true;
            }
        }
        return result;
    }

    public static boolean elementsEqual(Iterator<?> iterator1, Iterator<?> iterator2) {
        while (iterator1.hasNext()) {
            if (!iterator2.hasNext()) {
                return false;
            }
            Object o1 = iterator1.next();
            Object o2 = iterator2.next();
            if (!Objects.equal(o1, o2)) {
                return false;
            }
        }
        return !iterator2.hasNext();
    }

    public static String toString(Iterator<?> iterator) {
        StringBuilder sb = new StringBuilder().append('[');
        boolean first = true;
        while (iterator.hasNext()) {
            if (!first) {
                sb.append(", ");
            }
            first = false;
            sb.append(iterator.next());
        }
        return sb.append(']').toString();
    }

    public static <T> T getOnlyElement(Iterator<T> iterator) {
        T first = iterator.next();
        if (!iterator.hasNext()) {
            return first;
        }
        StringBuilder sb = new StringBuilder().append("expected one element but was: <").append(first);
        for (int i = 0; i < 4 && iterator.hasNext(); i++) {
            sb.append(", ").append(iterator.next());
        }
        if (iterator.hasNext()) {
            sb.append(", ...");
        }
        sb.append('>');
        throw new IllegalArgumentException(sb.toString());
    }

    @NullableDecl
    public static <T> T getOnlyElement(Iterator<? extends T> iterator, @NullableDecl T defaultValue) {
        return iterator.hasNext() ? (T) getOnlyElement(iterator) : defaultValue;
    }

    @GwtIncompatible
    public static <T> T[] toArray(Iterator<? extends T> iterator, Class<T> type) {
        List<T> list = Lists.newArrayList(iterator);
        return (T[]) Iterables.toArray(list, type);
    }

    @CanIgnoreReturnValue
    public static <T> boolean addAll(Collection<T> addTo, Iterator<? extends T> iterator) {
        Preconditions.checkNotNull(addTo);
        Preconditions.checkNotNull(iterator);
        boolean z = false;
        while (true) {
            boolean wasModified = z;
            if (iterator.hasNext()) {
                z = wasModified | addTo.add(iterator.next());
            } else {
                return wasModified;
            }
        }
    }

    public static int frequency(Iterator<?> iterator, @NullableDecl Object element) {
        int count = 0;
        while (contains(iterator, element)) {
            count++;
        }
        return count;
    }

    public static <T> Iterator<T> cycle(final Iterable<T> iterable) {
        Preconditions.checkNotNull(iterable);
        return new Iterator<T>() { // from class: com.google.common.collect.Iterators.2
            Iterator<T> iterator = Iterators.emptyModifiableIterator();

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.iterator.hasNext() || iterable.iterator().hasNext();
            }

            /* JADX WARN: Type inference failed for: r0v5, types: [T, java.lang.Object] */
            @Override // java.util.Iterator
            public T next() {
                if (!this.iterator.hasNext()) {
                    this.iterator = iterable.iterator();
                    if (!this.iterator.hasNext()) {
                        throw new NoSuchElementException();
                    }
                }
                return this.iterator.next();
            }

            @Override // java.util.Iterator
            public void remove() {
                this.iterator.remove();
            }
        };
    }

    @SafeVarargs
    public static <T> Iterator<T> cycle(T... elements) {
        return cycle(Lists.newArrayList(elements));
    }

    private static <T> Iterator<T> consumingForArray(final T... elements) {
        return new UnmodifiableIterator<T>() { // from class: com.google.common.collect.Iterators.3
            int index = 0;

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.index < elements.length;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.util.Iterator
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                T result = elements[this.index];
                elements[this.index] = null;
                this.index++;
                return result;
            }
        };
    }

    public static <T> Iterator<T> concat(Iterator<? extends T> a, Iterator<? extends T> b) {
        Preconditions.checkNotNull(a);
        Preconditions.checkNotNull(b);
        return concat(consumingForArray(a, b));
    }

    public static <T> Iterator<T> concat(Iterator<? extends T> a, Iterator<? extends T> b, Iterator<? extends T> c) {
        Preconditions.checkNotNull(a);
        Preconditions.checkNotNull(b);
        Preconditions.checkNotNull(c);
        return concat(consumingForArray(a, b, c));
    }

    public static <T> Iterator<T> concat(Iterator<? extends T> a, Iterator<? extends T> b, Iterator<? extends T> c, Iterator<? extends T> d) {
        Preconditions.checkNotNull(a);
        Preconditions.checkNotNull(b);
        Preconditions.checkNotNull(c);
        Preconditions.checkNotNull(d);
        return concat(consumingForArray(a, b, c, d));
    }

    public static <T> Iterator<T> concat(Iterator<? extends T>... inputs) {
        return concatNoDefensiveCopy((Iterator[]) Arrays.copyOf(inputs, inputs.length));
    }

    public static <T> Iterator<T> concat(Iterator<? extends Iterator<? extends T>> inputs) {
        return new ConcatenatedIterator(inputs);
    }

    static <T> Iterator<T> concatNoDefensiveCopy(Iterator<? extends T>... inputs) {
        Iterator<? extends T>[] itArr;
        for (Iterator<? extends T> input : (Iterator[]) Preconditions.checkNotNull(inputs)) {
            Preconditions.checkNotNull(input);
        }
        return concat(consumingForArray(inputs));
    }

    public static <T> UnmodifiableIterator<List<T>> partition(Iterator<T> iterator, int size) {
        return partitionImpl(iterator, size, false);
    }

    public static <T> UnmodifiableIterator<List<T>> paddedPartition(Iterator<T> iterator, int size) {
        return partitionImpl(iterator, size, true);
    }

    private static <T> UnmodifiableIterator<List<T>> partitionImpl(final Iterator<T> iterator, final int size, final boolean pad) {
        Preconditions.checkNotNull(iterator);
        Preconditions.checkArgument(size > 0);
        return new UnmodifiableIterator<List<T>>() { // from class: com.google.common.collect.Iterators.4
            @Override // java.util.Iterator
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override // java.util.Iterator
            public List<T> next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                Object[] array = new Object[size];
                int count = 0;
                while (count < size && iterator.hasNext()) {
                    array[count] = iterator.next();
                    count++;
                }
                for (int i = count; i < size; i++) {
                    array[i] = null;
                }
                List<T> list = Collections.unmodifiableList(Arrays.asList(array));
                return (pad || count == size) ? list : list.subList(0, count);
            }
        };
    }

    public static <T> UnmodifiableIterator<T> filter(final Iterator<T> unfiltered, final Predicate<? super T> retainIfTrue) {
        Preconditions.checkNotNull(unfiltered);
        Preconditions.checkNotNull(retainIfTrue);
        return new AbstractIterator<T>() { // from class: com.google.common.collect.Iterators.5
            /* JADX WARN: Type inference failed for: r0v7, types: [T, java.lang.Object] */
            @Override // com.google.common.collect.AbstractIterator
            protected T computeNext() {
                while (unfiltered.hasNext()) {
                    ?? next = unfiltered.next();
                    if (retainIfTrue.apply(next)) {
                        return next;
                    }
                }
                return endOfData();
            }
        };
    }

    @GwtIncompatible
    public static <T> UnmodifiableIterator<T> filter(Iterator<?> unfiltered, Class<T> desiredType) {
        return filter(unfiltered, Predicates.instanceOf(desiredType));
    }

    public static <T> boolean any(Iterator<T> iterator, Predicate<? super T> predicate) {
        return indexOf(iterator, predicate) != -1;
    }

    public static <T> boolean all(Iterator<T> iterator, Predicate<? super T> predicate) {
        Preconditions.checkNotNull(predicate);
        while (iterator.hasNext()) {
            T element = iterator.next();
            if (!predicate.apply(element)) {
                return false;
            }
        }
        return true;
    }

    public static <T> T find(Iterator<T> iterator, Predicate<? super T> predicate) {
        Preconditions.checkNotNull(iterator);
        Preconditions.checkNotNull(predicate);
        while (iterator.hasNext()) {
            T t = iterator.next();
            if (predicate.apply(t)) {
                return t;
            }
        }
        throw new NoSuchElementException();
    }

    /* JADX WARN: Type inference failed for: r0v8, types: [T, java.lang.Object] */
    @NullableDecl
    public static <T> T find(Iterator<? extends T> iterator, Predicate<? super T> predicate, @NullableDecl T defaultValue) {
        Preconditions.checkNotNull(iterator);
        Preconditions.checkNotNull(predicate);
        while (iterator.hasNext()) {
            T next = iterator.next();
            if (predicate.apply(next)) {
                return next;
            }
        }
        return defaultValue;
    }

    public static <T> Optional<T> tryFind(Iterator<T> iterator, Predicate<? super T> predicate) {
        Preconditions.checkNotNull(iterator);
        Preconditions.checkNotNull(predicate);
        while (iterator.hasNext()) {
            T t = iterator.next();
            if (predicate.apply(t)) {
                return Optional.of(t);
            }
        }
        return Optional.absent();
    }

    public static <T> int indexOf(Iterator<T> iterator, Predicate<? super T> predicate) {
        Preconditions.checkNotNull(predicate, "predicate");
        int i = 0;
        while (iterator.hasNext()) {
            T current = iterator.next();
            if (!predicate.apply(current)) {
                i++;
            } else {
                return i;
            }
        }
        return -1;
    }

    public static <F, T> Iterator<T> transform(Iterator<F> fromIterator, final Function<? super F, ? extends T> function) {
        Preconditions.checkNotNull(function);
        return new TransformedIterator<F, T>(fromIterator) { // from class: com.google.common.collect.Iterators.6
            /* JADX INFO: Access modifiers changed from: package-private */
            /* JADX WARN: Type inference failed for: r0v2, types: [T, java.lang.Object] */
            @Override // com.google.common.collect.TransformedIterator
            public T transform(F from) {
                return function.apply(from);
            }
        };
    }

    public static <T> T get(Iterator<T> iterator, int position) {
        checkNonnegative(position);
        int skipped = advance(iterator, position);
        if (!iterator.hasNext()) {
            throw new IndexOutOfBoundsException("position (" + position + ") must be less than the number of elements that remained (" + skipped + ")");
        }
        return iterator.next();
    }

    @NullableDecl
    public static <T> T get(Iterator<? extends T> iterator, int position, @NullableDecl T defaultValue) {
        checkNonnegative(position);
        advance(iterator, position);
        return (T) getNext(iterator, defaultValue);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void checkNonnegative(int position) {
        if (position < 0) {
            throw new IndexOutOfBoundsException("position (" + position + ") must not be negative");
        }
    }

    @NullableDecl
    public static <T> T getNext(Iterator<? extends T> iterator, @NullableDecl T defaultValue) {
        return iterator.hasNext() ? iterator.next() : defaultValue;
    }

    public static <T> T getLast(Iterator<T> iterator) {
        T current;
        do {
            current = iterator.next();
        } while (iterator.hasNext());
        return current;
    }

    @NullableDecl
    public static <T> T getLast(Iterator<? extends T> iterator, @NullableDecl T defaultValue) {
        return iterator.hasNext() ? (T) getLast(iterator) : defaultValue;
    }

    @CanIgnoreReturnValue
    public static int advance(Iterator<?> iterator, int numberToAdvance) {
        Preconditions.checkNotNull(iterator);
        Preconditions.checkArgument(numberToAdvance >= 0, "numberToAdvance must be nonnegative");
        int i = 0;
        while (i < numberToAdvance && iterator.hasNext()) {
            iterator.next();
            i++;
        }
        return i;
    }

    public static <T> Iterator<T> limit(final Iterator<T> iterator, final int limitSize) {
        Preconditions.checkNotNull(iterator);
        Preconditions.checkArgument(limitSize >= 0, "limit is negative");
        return new Iterator<T>() { // from class: com.google.common.collect.Iterators.7
            private int count;

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.count < limitSize && iterator.hasNext();
            }

            /* JADX WARN: Type inference failed for: r0v5, types: [T, java.lang.Object] */
            @Override // java.util.Iterator
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                this.count++;
                return iterator.next();
            }

            @Override // java.util.Iterator
            public void remove() {
                iterator.remove();
            }
        };
    }

    public static <T> Iterator<T> consumingIterator(final Iterator<T> iterator) {
        Preconditions.checkNotNull(iterator);
        return new UnmodifiableIterator<T>() { // from class: com.google.common.collect.Iterators.8
            @Override // java.util.Iterator
            public boolean hasNext() {
                return iterator.hasNext();
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [T, java.lang.Object] */
            @Override // java.util.Iterator
            public T next() {
                ?? next = iterator.next();
                iterator.remove();
                return next;
            }

            public String toString() {
                return "Iterators.consumingIterator(...)";
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @NullableDecl
    public static <T> T pollNext(Iterator<T> iterator) {
        if (iterator.hasNext()) {
            T result = iterator.next();
            iterator.remove();
            return result;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void clear(Iterator<?> iterator) {
        Preconditions.checkNotNull(iterator);
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }
    }

    @SafeVarargs
    public static <T> UnmodifiableIterator<T> forArray(T... array) {
        return forArray(array, 0, array.length, 0);
    }

    static <T> UnmodifiableListIterator<T> forArray(T[] array, int offset, int length, int index) {
        Preconditions.checkArgument(length >= 0);
        int end = offset + length;
        Preconditions.checkPositionIndexes(offset, end, array.length);
        Preconditions.checkPositionIndex(index, length);
        if (length == 0) {
            return emptyListIterator();
        }
        return new ArrayItr(array, offset, length, index);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/Iterators$ArrayItr.class */
    public static final class ArrayItr<T> extends AbstractIndexedListIterator<T> {
        static final UnmodifiableListIterator<Object> EMPTY = new ArrayItr(new Object[0], 0, 0, 0);
        private final T[] array;
        private final int offset;

        ArrayItr(T[] array, int offset, int length, int index) {
            super(length, index);
            this.array = array;
            this.offset = offset;
        }

        @Override // com.google.common.collect.AbstractIndexedListIterator
        protected T get(int index) {
            return this.array[this.offset + index];
        }
    }

    public static <T> UnmodifiableIterator<T> singletonIterator(@NullableDecl final T value) {
        return new UnmodifiableIterator<T>() { // from class: com.google.common.collect.Iterators.9
            boolean done;

            @Override // java.util.Iterator
            public boolean hasNext() {
                return !this.done;
            }

            /* JADX WARN: Type inference failed for: r0v4, types: [T, java.lang.Object] */
            @Override // java.util.Iterator
            public T next() {
                if (this.done) {
                    throw new NoSuchElementException();
                }
                this.done = true;
                return value;
            }
        };
    }

    public static <T> UnmodifiableIterator<T> forEnumeration(final Enumeration<T> enumeration) {
        Preconditions.checkNotNull(enumeration);
        return new UnmodifiableIterator<T>() { // from class: com.google.common.collect.Iterators.10
            @Override // java.util.Iterator
            public boolean hasNext() {
                return enumeration.hasMoreElements();
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [T, java.lang.Object] */
            @Override // java.util.Iterator
            public T next() {
                return enumeration.nextElement();
            }
        };
    }

    public static <T> Enumeration<T> asEnumeration(final Iterator<T> iterator) {
        Preconditions.checkNotNull(iterator);
        return new Enumeration<T>() { // from class: com.google.common.collect.Iterators.11
            @Override // java.util.Enumeration
            public boolean hasMoreElements() {
                return iterator.hasNext();
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [T, java.lang.Object] */
            @Override // java.util.Enumeration
            public T nextElement() {
                return iterator.next();
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/Iterators$PeekingImpl.class */
    public static class PeekingImpl<E> implements PeekingIterator<E> {
        private final Iterator<? extends E> iterator;
        private boolean hasPeeked;
        @NullableDecl
        private E peekedElement;

        public PeekingImpl(Iterator<? extends E> iterator) {
            this.iterator = (Iterator) Preconditions.checkNotNull(iterator);
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.hasPeeked || this.iterator.hasNext();
        }

        @Override // com.google.common.collect.PeekingIterator, java.util.Iterator
        public E next() {
            if (!this.hasPeeked) {
                return this.iterator.next();
            }
            E result = this.peekedElement;
            this.hasPeeked = false;
            this.peekedElement = null;
            return result;
        }

        @Override // com.google.common.collect.PeekingIterator, java.util.Iterator
        public void remove() {
            Preconditions.checkState(!this.hasPeeked, "Can't remove after you've peeked at next");
            this.iterator.remove();
        }

        @Override // com.google.common.collect.PeekingIterator
        public E peek() {
            if (!this.hasPeeked) {
                this.peekedElement = this.iterator.next();
                this.hasPeeked = true;
            }
            return this.peekedElement;
        }
    }

    public static <T> PeekingIterator<T> peekingIterator(Iterator<? extends T> iterator) {
        if (iterator instanceof PeekingImpl) {
            PeekingImpl<T> peeking = (PeekingImpl) iterator;
            return peeking;
        }
        return new PeekingImpl(iterator);
    }

    @Deprecated
    public static <T> PeekingIterator<T> peekingIterator(PeekingIterator<T> iterator) {
        return (PeekingIterator) Preconditions.checkNotNull(iterator);
    }

    @Beta
    public static <T> UnmodifiableIterator<T> mergeSorted(Iterable<? extends Iterator<? extends T>> iterators, Comparator<? super T> comparator) {
        Preconditions.checkNotNull(iterators, "iterators");
        Preconditions.checkNotNull(comparator, "comparator");
        return new MergingIterator(iterators, comparator);
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/Iterators$MergingIterator.class */
    private static class MergingIterator<T> extends UnmodifiableIterator<T> {
        final Queue<PeekingIterator<T>> queue;

        public MergingIterator(Iterable<? extends Iterator<? extends T>> iterators, final Comparator<? super T> itemComparator) {
            Comparator<PeekingIterator<T>> heapComparator = new Comparator<PeekingIterator<T>>() { // from class: com.google.common.collect.Iterators.MergingIterator.1
                @Override // java.util.Comparator
                public /* bridge */ /* synthetic */ int compare(Object obj, Object obj2) {
                    return compare((PeekingIterator) ((PeekingIterator) obj), (PeekingIterator) ((PeekingIterator) obj2));
                }

                public int compare(PeekingIterator<T> o1, PeekingIterator<T> o2) {
                    return itemComparator.compare(o1.peek(), o2.peek());
                }
            };
            this.queue = new PriorityQueue(2, heapComparator);
            for (Iterator<? extends T> iterator : iterators) {
                if (iterator.hasNext()) {
                    this.queue.add(Iterators.peekingIterator(iterator));
                }
            }
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return !this.queue.isEmpty();
        }

        @Override // java.util.Iterator
        public T next() {
            PeekingIterator<T> nextIter = this.queue.remove();
            T next = nextIter.next();
            if (nextIter.hasNext()) {
                this.queue.add(nextIter);
            }
            return next;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/Iterators$ConcatenatedIterator.class */
    public static class ConcatenatedIterator<T> implements Iterator<T> {
        @NullableDecl
        private Iterator<? extends T> toRemove;
        private Iterator<? extends T> iterator = Iterators.emptyIterator();
        private Iterator<? extends Iterator<? extends T>> topMetaIterator;
        @NullableDecl
        private Deque<Iterator<? extends Iterator<? extends T>>> metaIterators;

        ConcatenatedIterator(Iterator<? extends Iterator<? extends T>> metaIterator) {
            this.topMetaIterator = (Iterator) Preconditions.checkNotNull(metaIterator);
        }

        @NullableDecl
        private Iterator<? extends Iterator<? extends T>> getTopMetaIterator() {
            while (true) {
                if (this.topMetaIterator == null || !this.topMetaIterator.hasNext()) {
                    if (this.metaIterators != null && !this.metaIterators.isEmpty()) {
                        this.topMetaIterator = this.metaIterators.removeFirst();
                    } else {
                        return null;
                    }
                } else {
                    return this.topMetaIterator;
                }
            }
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            while (!((Iterator) Preconditions.checkNotNull(this.iterator)).hasNext()) {
                this.topMetaIterator = getTopMetaIterator();
                if (this.topMetaIterator == null) {
                    return false;
                }
                this.iterator = this.topMetaIterator.next();
                if (this.iterator instanceof ConcatenatedIterator) {
                    ConcatenatedIterator<T> topConcat = (ConcatenatedIterator) this.iterator;
                    this.iterator = topConcat.iterator;
                    if (this.metaIterators == null) {
                        this.metaIterators = new ArrayDeque();
                    }
                    this.metaIterators.addFirst(this.topMetaIterator);
                    if (topConcat.metaIterators != null) {
                        while (!topConcat.metaIterators.isEmpty()) {
                            this.metaIterators.addFirst(topConcat.metaIterators.removeLast());
                        }
                    }
                    this.topMetaIterator = topConcat.topMetaIterator;
                }
            }
            return true;
        }

        @Override // java.util.Iterator
        public T next() {
            if (hasNext()) {
                this.toRemove = this.iterator;
                return this.iterator.next();
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            CollectPreconditions.checkRemove(this.toRemove != null);
            this.toRemove.remove();
            this.toRemove = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> ListIterator<T> cast(Iterator<T> iterator) {
        return (ListIterator) iterator;
    }
}
