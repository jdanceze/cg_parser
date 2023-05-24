package soot.util;

import java.lang.reflect.Array;
import java.util.Iterator;
import soot.util.Numberable;
/* loaded from: gencallgraphv3.jar:soot/util/SmallNumberedMap.class */
public final class SmallNumberedMap<K extends Numberable, V> implements INumberedMap<K, V> {
    private K[] array = (K[]) ((Numberable[]) newArray(Numberable.class, 8));
    private V[] values = (V[]) newArray(Object.class, 8);
    private int size = 0;

    private static <T> T[] newArray(Class<? super T> componentType, int length) {
        return (T[]) ((Object[]) Array.newInstance(componentType, length));
    }

    @Override // soot.util.INumberedMap
    public boolean put(K key, V value) {
        int pos = findPosition(key);
        if (this.array[pos] == key) {
            if (this.values[pos] == value) {
                return false;
            }
            this.values[pos] = value;
            return true;
        }
        this.size++;
        if (this.size * 3 > this.array.length * 2) {
            doubleSize();
            pos = findPosition(key);
        }
        this.array[pos] = key;
        this.values[pos] = value;
        return true;
    }

    @Override // soot.util.INumberedMap
    public V get(K key) {
        return this.values[findPosition(key)];
    }

    @Override // soot.util.INumberedMap
    public void remove(K key) {
        int pos = findPosition(key);
        if (this.array[pos] == key) {
            this.array[pos] = null;
            this.values[pos] = null;
            this.size--;
        }
    }

    public int nonNullSize() {
        V[] vArr;
        int ret = 0;
        for (V element : this.values) {
            if (element != null) {
                ret++;
            }
        }
        return ret;
    }

    @Override // soot.util.INumberedMap
    public Iterator<K> keyIterator() {
        return new SmallNumberedMapIterator(this.array);
    }

    public Iterator<V> iterator() {
        return new SmallNumberedMapIterator(this.values);
    }

    /* loaded from: gencallgraphv3.jar:soot/util/SmallNumberedMap$SmallNumberedMapIterator.class */
    private class SmallNumberedMapIterator<C> implements Iterator<C> {
        private final C[] data;
        private int cur = 0;

        SmallNumberedMapIterator(C[] cArr) {
            this.data = cArr;
            seekNext();
        }

        protected final void seekNext() {
            Object[] temp = SmallNumberedMap.this.values;
            while (temp[this.cur] == null) {
                try {
                    this.cur++;
                } catch (ArrayIndexOutOfBoundsException e) {
                    this.cur = -1;
                    return;
                }
            }
        }

        @Override // java.util.Iterator
        public final void remove() {
            SmallNumberedMap.this.array[this.cur - 1] = null;
            SmallNumberedMap.this.values[this.cur - 1] = null;
        }

        @Override // java.util.Iterator
        public final boolean hasNext() {
            return this.cur != -1;
        }

        @Override // java.util.Iterator
        public final C next() {
            C ret = this.data[this.cur];
            this.cur++;
            seekNext();
            return ret;
        }
    }

    private int findPosition(K o) {
        int number;
        int number2 = o.getNumber();
        if (number2 == 0) {
            throw new RuntimeException("unnumbered");
        }
        int i = number2;
        int length = this.array.length;
        while (true) {
            number = i & (length - 1);
            K key = this.array[number];
            if (key == o || key == null) {
                break;
            }
            i = number + 1;
            length = this.array.length;
        }
        return number;
    }

    private void doubleSize() {
        K[] kArr = this.array;
        Object[] oldValues = this.values;
        int oldLength = kArr.length;
        int newLength = oldLength * 2;
        this.array = (K[]) ((Numberable[]) newArray(Numberable.class, newLength));
        this.values = (V[]) newArray(Object.class, newLength);
        for (int i = 0; i < oldLength; i++) {
            K element = kArr[i];
            if (element != null) {
                int pos = findPosition(element);
                this.array[pos] = element;
                this.values[pos] = oldValues[i];
            }
        }
    }
}
