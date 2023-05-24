package soot.util;

import java.util.Iterator;
import java.util.NoSuchElementException;
import soot.util.Numberable;
/* loaded from: gencallgraphv3.jar:soot/util/LargeNumberedMap.class */
public final class LargeNumberedMap<K extends Numberable, V> implements INumberedMap<K, V> {
    private final IterableNumberer<K> universe;
    private V[] values;

    public LargeNumberedMap(IterableNumberer<K> universe) {
        this.universe = universe;
        int size = universe.size();
        this.values = (V[]) newArray(size < 8 ? 8 : size);
    }

    private static <T> T[] newArray(int size) {
        return (T[]) new Object[size];
    }

    @Override // soot.util.INumberedMap
    public boolean put(K key, V value) {
        int number = key.getNumber();
        if (number == 0) {
            throw new RuntimeException(String.format("oops, forgot to initialize. Object is of type %s, and looks like this: %s", key.getClass().getName(), key.toString()));
        }
        if (number >= this.values.length) {
            Object[] oldValues = this.values;
            this.values = (V[]) newArray(Math.max(this.universe.size() * 2, number) + 5);
            System.arraycopy(oldValues, 0, this.values, 0, oldValues.length);
        }
        boolean ret = this.values[number] != value;
        this.values[number] = value;
        return ret;
    }

    @Override // soot.util.INumberedMap
    public V get(K key) {
        int i = key.getNumber();
        if (i >= this.values.length) {
            return null;
        }
        return this.values[i];
    }

    @Override // soot.util.INumberedMap
    public void remove(K key) {
        int i = key.getNumber();
        if (i < this.values.length) {
            this.values[i] = null;
        }
    }

    @Override // soot.util.INumberedMap
    public Iterator<K> keyIterator() {
        return (Iterator<K>) new Iterator<K>() { // from class: soot.util.LargeNumberedMap.1
            int cur = 0;

            private void advance() {
                while (this.cur < LargeNumberedMap.this.values.length && LargeNumberedMap.this.values[this.cur] == null) {
                    this.cur++;
                }
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                advance();
                return this.cur < LargeNumberedMap.this.values.length;
            }

            @Override // java.util.Iterator
            public K next() {
                if (hasNext()) {
                    IterableNumberer iterableNumberer = LargeNumberedMap.this.universe;
                    int i = this.cur;
                    this.cur = i + 1;
                    return (K) iterableNumberer.get(i);
                }
                throw new NoSuchElementException();
            }

            @Override // java.util.Iterator
            public void remove() {
                LargeNumberedMap.this.values[this.cur - 1] = null;
            }
        };
    }
}
