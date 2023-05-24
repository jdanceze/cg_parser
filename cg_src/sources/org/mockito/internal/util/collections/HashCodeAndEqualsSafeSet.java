package org.mockito.internal.util.collections;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.mockito.internal.util.Checks;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/collections/HashCodeAndEqualsSafeSet.class */
public class HashCodeAndEqualsSafeSet implements Set<Object> {
    private final HashSet<HashCodeAndEqualsMockWrapper> backingHashSet = new HashSet<>();
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !HashCodeAndEqualsSafeSet.class.desiredAssertionStatus();
    }

    @Override // java.util.Set, java.util.Collection, java.lang.Iterable
    public Iterator<Object> iterator() {
        return new Iterator<Object>() { // from class: org.mockito.internal.util.collections.HashCodeAndEqualsSafeSet.1
            private final Iterator<HashCodeAndEqualsMockWrapper> iterator;

            {
                this.iterator = HashCodeAndEqualsSafeSet.this.backingHashSet.iterator();
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.iterator.hasNext();
            }

            @Override // java.util.Iterator
            public Object next() {
                return this.iterator.next().get();
            }

            @Override // java.util.Iterator
            public void remove() {
                this.iterator.remove();
            }
        };
    }

    @Override // java.util.Set, java.util.Collection
    public int size() {
        return this.backingHashSet.size();
    }

    @Override // java.util.Set, java.util.Collection
    public boolean isEmpty() {
        return this.backingHashSet.isEmpty();
    }

    @Override // java.util.Set, java.util.Collection
    public boolean contains(Object mock) {
        return this.backingHashSet.contains(HashCodeAndEqualsMockWrapper.of(mock));
    }

    @Override // java.util.Set, java.util.Collection
    public boolean add(Object mock) {
        return this.backingHashSet.add(HashCodeAndEqualsMockWrapper.of(mock));
    }

    @Override // java.util.Set, java.util.Collection
    public boolean remove(Object mock) {
        return this.backingHashSet.remove(HashCodeAndEqualsMockWrapper.of(mock));
    }

    @Override // java.util.Set, java.util.Collection
    public void clear() {
        this.backingHashSet.clear();
    }

    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    @Override // java.util.Set, java.util.Collection
    public boolean equals(Object o) {
        if (!(o instanceof HashCodeAndEqualsSafeSet)) {
            return false;
        }
        HashCodeAndEqualsSafeSet that = (HashCodeAndEqualsSafeSet) o;
        return this.backingHashSet.equals(that.backingHashSet);
    }

    @Override // java.util.Set, java.util.Collection
    public int hashCode() {
        return this.backingHashSet.hashCode();
    }

    @Override // java.util.Set, java.util.Collection
    public Object[] toArray() {
        return unwrapTo(new Object[size()]);
    }

    private <T> T[] unwrapTo(T[] array) {
        Iterator<Object> iterator = iterator();
        int objectsLength = array.length;
        for (int i = 0; i < objectsLength; i++) {
            if (iterator.hasNext()) {
                array[i] = iterator.next();
            }
        }
        return array;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.Set, java.util.Collection
    public <T> T[] toArray(T[] typedArray) {
        T[] tArr;
        if (typedArray.length >= size()) {
            tArr = typedArray;
        } else {
            tArr = (Object[]) Array.newInstance(typedArray.getClass().getComponentType(), size());
        }
        T[] array = tArr;
        return (T[]) unwrapTo(array);
    }

    @Override // java.util.Set, java.util.Collection
    public boolean removeAll(Collection<?> mocks) {
        return this.backingHashSet.removeAll(asWrappedMocks(mocks));
    }

    @Override // java.util.Set, java.util.Collection
    public boolean containsAll(Collection<?> mocks) {
        return this.backingHashSet.containsAll(asWrappedMocks(mocks));
    }

    @Override // java.util.Set, java.util.Collection
    public boolean addAll(Collection<?> mocks) {
        return this.backingHashSet.addAll(asWrappedMocks(mocks));
    }

    @Override // java.util.Set, java.util.Collection
    public boolean retainAll(Collection<?> mocks) {
        return this.backingHashSet.retainAll(asWrappedMocks(mocks));
    }

    private HashSet<HashCodeAndEqualsMockWrapper> asWrappedMocks(Collection<?> mocks) {
        Checks.checkNotNull(mocks, "Passed collection should notify() be null");
        HashSet<HashCodeAndEqualsMockWrapper> hashSet = new HashSet<>();
        for (Object mock : mocks) {
            if (!$assertionsDisabled && (mock instanceof HashCodeAndEqualsMockWrapper)) {
                throw new AssertionError("WRONG");
            }
            hashSet.add(HashCodeAndEqualsMockWrapper.of(mock));
        }
        return hashSet;
    }

    public String toString() {
        return this.backingHashSet.toString();
    }

    public static HashCodeAndEqualsSafeSet of(Object... mocks) {
        return of((Iterable<Object>) Arrays.asList(mocks));
    }

    public static HashCodeAndEqualsSafeSet of(Iterable<Object> objects) {
        HashCodeAndEqualsSafeSet hashCodeAndEqualsSafeSet = new HashCodeAndEqualsSafeSet();
        if (objects != null) {
            for (Object mock : objects) {
                hashCodeAndEqualsSafeSet.add(mock);
            }
        }
        return hashCodeAndEqualsSafeSet;
    }
}
