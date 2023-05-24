package soot.util;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import soot.util.Numberable;
/* loaded from: gencallgraphv3.jar:soot/util/ArrayNumberer.class */
public class ArrayNumberer<E extends Numberable> implements IterableNumberer<E> {
    protected E[] numberToObj;
    protected int lastNumber;
    protected BitSet freeNumbers;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // soot.util.Numberer
    public /* bridge */ /* synthetic */ long get(Object obj) {
        return get((ArrayNumberer<E>) ((Numberable) obj));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // soot.util.Numberer
    public /* bridge */ /* synthetic */ boolean remove(Object obj) {
        return remove((ArrayNumberer<E>) ((Numberable) obj));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // soot.util.Numberer
    public /* bridge */ /* synthetic */ void add(Object obj) {
        add((ArrayNumberer<E>) ((Numberable) obj));
    }

    public ArrayNumberer() {
        this.numberToObj = (E[]) new Numberable[1024];
        this.lastNumber = 0;
    }

    public ArrayNumberer(E[] eArr) {
        this.numberToObj = eArr;
        this.lastNumber = eArr.length;
    }

    private void resize(int n) {
        this.numberToObj = (E[]) ((Numberable[]) Arrays.copyOf(this.numberToObj, n));
    }

    public synchronized void add(E o) {
        int ns;
        if (o.getNumber() != 0) {
            return;
        }
        int chosenNumber = -1;
        if (this.freeNumbers != null && (ns = this.freeNumbers.nextSetBit(0)) != -1) {
            chosenNumber = ns;
            this.freeNumbers.clear(ns);
        }
        if (chosenNumber == -1) {
            int i = this.lastNumber + 1;
            this.lastNumber = i;
            chosenNumber = i;
        }
        if (chosenNumber >= this.numberToObj.length) {
            resize(this.numberToObj.length * 2);
        }
        this.numberToObj[chosenNumber] = o;
        o.setNumber(chosenNumber);
    }

    public long get(E o) {
        if (o == null) {
            return 0L;
        }
        int ret = o.getNumber();
        if (ret == 0) {
            throw new RuntimeException("unnumbered: " + o);
        }
        return ret;
    }

    @Override // soot.util.Numberer
    public E get(long number) {
        E ret;
        if (number == 0 || (ret = this.numberToObj[(int) number]) == null) {
            return null;
        }
        return ret;
    }

    @Override // soot.util.Numberer
    public int size() {
        return this.lastNumber;
    }

    @Override // soot.util.IterableNumberer, java.lang.Iterable
    public Iterator<E> iterator() {
        return (Iterator<E>) new Iterator<E>() { // from class: soot.util.ArrayNumberer.1
            int cur = 1;

            @Override // java.util.Iterator
            public final boolean hasNext() {
                return this.cur <= ArrayNumberer.this.lastNumber && this.cur < ArrayNumberer.this.numberToObj.length && ArrayNumberer.this.numberToObj[this.cur] != null;
            }

            @Override // java.util.Iterator
            public final E next() {
                if (hasNext()) {
                    E[] eArr = ArrayNumberer.this.numberToObj;
                    int i = this.cur;
                    this.cur = i + 1;
                    return eArr[i];
                }
                throw new NoSuchElementException();
            }

            @Override // java.util.Iterator
            public final void remove() {
                ArrayNumberer.this.remove((ArrayNumberer) ArrayNumberer.this.numberToObj[this.cur - 1]);
            }
        };
    }

    public boolean remove(E o) {
        int num;
        if (o == null || (num = o.getNumber()) == 0) {
            return false;
        }
        if (this.freeNumbers == null) {
            this.freeNumbers = new BitSet(2 * num);
        }
        this.numberToObj[num] = null;
        o.setNumber(0);
        this.freeNumbers.set(num);
        return true;
    }
}
