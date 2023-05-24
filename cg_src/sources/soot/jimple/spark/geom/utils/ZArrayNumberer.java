package soot.jimple.spark.geom.utils;

import dalvik.bytecode.Opcodes;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import soot.util.IterableNumberer;
import soot.util.Numberable;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/geom/utils/ZArrayNumberer.class */
public class ZArrayNumberer<E extends Numberable> implements IterableNumberer<E>, Iterable<E> {
    final Map<E, E> objContainer;
    Numberable[] numberToObj;
    int lastNumber;
    int filledCells;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // soot.util.Numberer
    public /* bridge */ /* synthetic */ long get(Object obj) {
        return get((ZArrayNumberer<E>) ((Numberable) obj));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // soot.util.Numberer
    public /* bridge */ /* synthetic */ boolean remove(Object obj) {
        return remove((ZArrayNumberer<E>) ((Numberable) obj));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // soot.util.Numberer
    public /* bridge */ /* synthetic */ void add(Object obj) {
        add((ZArrayNumberer<E>) ((Numberable) obj));
    }

    public ZArrayNumberer() {
        this.lastNumber = 0;
        this.filledCells = 0;
        this.numberToObj = new Numberable[Opcodes.OP_NEW_INSTANCE_JUMBO];
        this.objContainer = new HashMap((int) Opcodes.OP_NEW_INSTANCE_JUMBO);
    }

    public ZArrayNumberer(int initSize) {
        this.lastNumber = 0;
        this.filledCells = 0;
        this.numberToObj = new Numberable[initSize];
        this.objContainer = new HashMap(initSize);
    }

    public void add(E o) {
        if (o.getNumber() != -1 && this.numberToObj[o.getNumber()] == o) {
            return;
        }
        this.numberToObj[this.lastNumber] = o;
        o.setNumber(this.lastNumber);
        this.objContainer.put(o, o);
        this.lastNumber++;
        this.filledCells++;
        if (this.lastNumber >= this.numberToObj.length) {
            Numberable[] newnto = new Numberable[this.numberToObj.length * 2];
            System.arraycopy(this.numberToObj, 0, newnto, 0, this.numberToObj.length);
            this.numberToObj = newnto;
        }
    }

    public void clear() {
        for (int i = 0; i < this.lastNumber; i++) {
            this.numberToObj[i] = null;
        }
        this.lastNumber = 0;
        this.filledCells = 0;
        this.objContainer.clear();
    }

    public long get(E o) {
        if (o == null) {
            return -1L;
        }
        return o.getNumber();
    }

    @Override // soot.util.Numberer
    public E get(long number) {
        E ret = (E) this.numberToObj[(int) number];
        return ret;
    }

    public E searchFor(E o) {
        return this.objContainer.get(o);
    }

    public boolean remove(E o) {
        int id = o.getNumber();
        if (id < 0 || this.numberToObj[id] != o) {
            return false;
        }
        this.numberToObj[id] = null;
        o.setNumber(-1);
        this.filledCells--;
        return true;
    }

    @Override // soot.util.Numberer
    public int size() {
        return this.filledCells;
    }

    public void reassign() {
        int i = 0;
        int j = this.lastNumber - 1;
        while (i < j) {
            if (this.numberToObj[i] == null) {
                while (j > i && this.numberToObj[j] == null) {
                    j--;
                }
                if (i == j) {
                    break;
                }
                this.numberToObj[i] = this.numberToObj[j];
                this.numberToObj[i].setNumber(i);
                this.numberToObj[j] = null;
            }
            i++;
        }
        this.lastNumber = i;
    }

    @Override // soot.util.IterableNumberer, java.lang.Iterable
    public Iterator<E> iterator() {
        return new NumbererIterator();
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/spark/geom/utils/ZArrayNumberer$NumbererIterator.class */
    final class NumbererIterator implements Iterator<E> {
        int cur = 0;
        E lastElement = null;

        NumbererIterator() {
        }

        @Override // java.util.Iterator
        public final boolean hasNext() {
            while (this.cur < ZArrayNumberer.this.lastNumber && ZArrayNumberer.this.numberToObj[this.cur] == null) {
                this.cur++;
            }
            return this.cur < ZArrayNumberer.this.lastNumber;
        }

        @Override // java.util.Iterator
        public final E next() {
            Numberable[] numberableArr = ZArrayNumberer.this.numberToObj;
            int i = this.cur;
            this.cur = i + 1;
            E temp = (E) numberableArr[i];
            this.lastElement = temp;
            return temp;
        }

        @Override // java.util.Iterator
        public final void remove() {
            ZArrayNumberer.this.remove((ZArrayNumberer) this.lastElement);
        }
    }
}
